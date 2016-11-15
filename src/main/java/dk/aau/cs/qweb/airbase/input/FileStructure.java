package dk.aau.cs.qweb.airbase.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import dk.aau.cs.qweb.airbase.types.Tuple;

public class FileStructure implements Iterator<Tuple> {
	private File file;
	private LineIterator it;
	private List<String> header;

	public FileStructure(String file) throws IOException {
		this.file = new File(file);
		it = FileUtils.lineIterator(this.file, "UTF-8");
		
		BufferedReader input = new BufferedReader(new FileReader(file));
		String line = input.readLine();
		this.header = Arrays.asList(line.split("\\s*\t\\s*"));
		input.close();
	}

	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public Tuple next() {
		String line = it.next();
		List<String> data = Arrays.asList(line.split("\\s*\t\\s*"));
		return new Tuple(data,header);
	}
}
