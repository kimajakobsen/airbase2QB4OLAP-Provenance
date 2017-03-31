#!/usr/bin/python

import sys
import os

root = sys.argv[1]

def parseFile (filename) :
	inOrganization = False
	inPerson = False
	inNetworkDataSupp = False
	with open(filename, 'r') as fin :
		with open(filename.replace('.xml', '.parsed.xml'), 'w') as fout :
			fout.write('<network_data_supplier>')
			for rawline in fin :
				line = rawline.strip()
				if "<network_data_supplier>" in line :
					inNetworkDataSupp = True
				if "<organization>" in line :
					inOrganization = True
				elif "</organization>" in line : 
					if inNetworkDataSupp :
						fout.write(line)
					inOrganization = False
				elif "<person>" in line :
					inPerson = True
				elif "</person>" in line :
					if inNetworkDataSupp :
						fout.write(line)
					inPerson = False
				elif  "</network_data_supplier>" in line :
					inNetworkDataSupp = False			
			
				if inNetworkDataSupp and (inOrganization or inPerson) :
					fout.write('\n' + line)

			fout.write('\n</network_data_supplier>')
			
for country in os.listdir(root) :
	if os.path.isdir(country) :
		for f in os.listdir(country) :
			if f.endswith('meta.xml') :
				ifile = root + '/' + country + '/' + f
				print ifile
				parseFile(ifile)
		
