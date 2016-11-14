package dk.aau.cs.qweb.airbase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.qweb.airbase.database.Database;
import dk.aau.cs.qweb.airbase.parser.InputParser;
import dk.aau.cs.qweb.airbase.types.CubeStructure;
import dk.aau.cs.qweb.airbase.types.Tuple;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Database dbConnection = new Database();
		
		File schema = null;
		CubeStructure structure = new CubeStructure(schema);
		
		List<File> files = new ArrayList<File>();
		for (File file : files) {
			InputParser parser = new InputParser(file);
			
			
			for (Tuple tuple : parser.getTuples()) {
				dbConnection.writeToDisk();
			}
		}
		
		
		
		
		
	}

}
