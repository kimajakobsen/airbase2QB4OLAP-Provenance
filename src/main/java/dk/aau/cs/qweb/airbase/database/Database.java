package dk.aau.cs.qweb.airbase.database;

import java.io.IOException;
import java.util.Set;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.types.Quad;

public abstract class Database {

	public abstract void writeToDisk(Set<Quad> quads); 
	
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
