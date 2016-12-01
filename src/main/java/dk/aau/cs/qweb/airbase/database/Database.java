package dk.aau.cs.qweb.airbase.database;

import java.io.IOException;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.types.TripleContainer;

public abstract class Database {

	public abstract void writeToDisk(TripleContainer triples); 
	
	public abstract void cleanWrite() throws IOException;

	public static Database build() {
		if( Config.getDbType().equals("TDB")) {
			return new TDB();
		} else if (Config.getDbType().equals("File")) {
			return new TurtleFile();
		} else {
			throw new IllegalArgumentException("Storage type: " + Config.getDbType()+" is not known.");
		}
	}
	
}
