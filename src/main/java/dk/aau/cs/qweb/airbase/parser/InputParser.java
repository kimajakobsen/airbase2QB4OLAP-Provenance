package dk.aau.cs.qweb.airbase.parser;

import java.io.File;
import java.util.List;

import dk.aau.cs.qweb.airbase.types.Tuple;

public class InputParser {

	public InputParser(File file) {
		FileParser data = FileParserBuilder.build(file);
	}

	public List<Tuple> getTuples() {
		// TODO Auto-generated method stub
		return null;
	}

}
