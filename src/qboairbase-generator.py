#!/usr/bin/python

import sys
import multiprocessing
from collections import deque
import os
import threading
import time
import uuid

class ProvThread(threading.Thread) :
	lock = threading.Lock()
	def __init__(self, inputDir, newLines, queue):
		self.inputDir = inputDir
		self.newLines = newLines
		self.queue = queue
		threading.Thread.__init__(self)
    
	def run(self):
		while True: 
			ProvThread.lock.acquire()
			inputFile = None
			if len(self.queue) > 0 :
				inputFile = self.queue.pop() 
			ProvThread.lock.release()
			## If no more files return
			if inputFile is None :
				return

			print self, " is processing file ", inputFile
			rconfig = str(uuid.uuid4())
			with open(rconfig, 'w') as fconfig :
				for line in self.newLines :
					fconfig.write(line)
				fconfig.write('input: ' + self.inputDir + '/' +  inputFile)
		
			provenanceGen(rconfig, inputFile)
			os.unlink(rconfig)
			print self, " is done with file ", inputFile
	

def provenanceGen(configFile, inputFile) :
	cmdLine = 'java -jar airbase-gen.jar -c ' + configFile + ' > ' + inputFile + '.log'				
	print cmdLine
	os.system(cmdLine)



configFile = open(sys.argv[1], 'r')
lines = configFile.readlines()
configFile.close()

ncpus = multiprocessing.cpu_count()
if len(sys.argv) > 2 :
	ncpus = int(sys.argv[2])

inputDir = None
newlines = []
for line in lines :
	if line.startswith('input:') :
		inputDir = line.split(': ')[1].rstrip()
	else :
		newlines.append(line)	
		
		
if inputDir is not None :
	files = os.listdir(inputDir)
	print files
	queue = deque(files)
	njobs = min(len(queue), ncpus)
	threads = []
	rconfigs = []	
	for i in range(0, njobs) :
		th = ProvThread(inputDir, newlines, queue)
		th.start()
		threads.append(th)
		
	for t in threads :
		t.join()
