package dk.aau.cs.qweb.airbase.provenance;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.Tuple;

public class Provenance {

	private static Provenance instance = null;
	private Map<ProvenanceSignature,String> provenanceMap = new HashMap<ProvenanceSignature,String>();
	private Map<String,ProvenanceGraph> provenanceGraphMap = new HashMap<String,ProvenanceGraph>();
	
	private Provenance() { }
	
	public static Provenance getInstance() {
		if(instance == null) {
			instance = new Provenance();
		}
		return instance;
	}

	public String getProvenanceIdentifier(Quad quad, String level, Collection<String> files, Tuple tuple) {
		ProvenanceSignature signature = new ProvenanceSignature(quad,level,LocalDate.now(), files, tuple);
		if (provenanceMap.containsKey(signature)) {
			return provenanceMap.get(signature);
		} else {
			String provenanceIdentifier = createProvenanceGraph(signature);
			provenanceMap.put(signature, provenanceIdentifier);
			
			return provenanceIdentifier;
		}
	}

	private String createProvenanceGraph(ProvenanceSignature signature) {
		ProvenanceGraph provenanceGraph = new ProvenanceGraph(signature);
		provenanceGraphMap.put(provenanceGraph.getProvenanceIdentifier(), provenanceGraph);
		return provenanceGraph.getProvenanceIdentifier();
	}

	public Set<Quad> getProvenanceTriples() {
		Set<Quad> provenanceQuads = new HashSet<Quad>();
		for (ProvenanceGraph provenanceGraph : provenanceGraphMap.values()) {
			 provenanceQuads.addAll(provenanceGraph.getQuads());
		}
		return provenanceQuads;
	}
	
	public Set<Quad> getProvenanceGraph(String provenanceIdentifier) {
		Set<Quad> provenanceQuads = new HashSet<Quad>();
		provenanceQuads.addAll(provenanceGraphMap.get(provenanceIdentifier).getQuads());
		return provenanceQuads;
	}
}
