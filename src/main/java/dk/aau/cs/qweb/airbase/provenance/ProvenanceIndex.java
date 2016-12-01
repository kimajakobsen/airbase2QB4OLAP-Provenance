package dk.aau.cs.qweb.airbase.provenance;

import java.util.HashSet;
import java.util.Set;

public class ProvenanceIndex {
	static Set<String> existingProvenance = new HashSet<String>();

	public static boolean contains(String subject) {
		return existingProvenance.contains(subject);
	}

	public static void add(String subject) {
		existingProvenance.add(subject);
	}

}
