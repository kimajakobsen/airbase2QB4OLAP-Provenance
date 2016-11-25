package dk.aau.cs.qweb.airbase.database;

import java.util.Set;

import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.TripleContainer;

public class Database {

	public void writeToDisk(TripleContainer triples) {
		Set<Quad> informationTriples = triples.getInformationTriples();
		Set<Quad> metadataTriples = triples.getMetadataTriples();
		Set<Quad> provenanceTriples = triples.getProvenanceTriples();
		
	}

}
