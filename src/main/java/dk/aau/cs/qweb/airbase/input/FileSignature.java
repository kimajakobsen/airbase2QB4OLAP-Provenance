package dk.aau.cs.qweb.airbase.input;

import java.util.List;

import dk.aau.cs.qweb.airbase.types.CubeStructure;
import dk.aau.cs.qweb.airbase.types.Tuple;

public class FileSignature {
	

	public FileSignature(String file, CubeStructure structure) {
		FileParser data = FileParserBuilder.build(file);
	}

	public List<Tuple> getTuples() {
		// TODO Auto-generated method stub
		return null;
	}

}
