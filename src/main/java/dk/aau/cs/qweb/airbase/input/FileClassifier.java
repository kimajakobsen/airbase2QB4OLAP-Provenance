package dk.aau.cs.qweb.airbase.input;

import java.util.List;

import dk.aau.cs.qweb.airbase.types.Tuple;

public class FileClassifier {
	
	private FileStructure fileStructure;
	
	public FileClassifier(String file) {
		fileStructure = FileSignatureBuilder.build(file);
	}

	public List<Tuple> getTuples() {
		// TODO Auto-generated method stub
		return null;
	}

}
