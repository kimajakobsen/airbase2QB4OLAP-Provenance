package dk.aau.cs.qweb.airbase.provenance;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dk.aau.cs.qweb.airbase.types.Quad;

public class ProvenanceIndex {

	private static ProvenanceIndex instance = null;
	private Map<ProvenanceSignature,String> provenanceMap = new HashMap<ProvenanceSignature,String>();
	private Map<String,ProvenanceGraph> provenanceGraphMap = new HashMap<String,ProvenanceGraph>();
	
	private ProvenanceIndex() { }
	
	public static ProvenanceIndex getInstance() {
		if(instance == null) {
			instance = new ProvenanceIndex();
		}
		return instance;
	}

	public String getProvenanceIdentifier(Quad quad, String level, String file) {
		ProvenanceSignature signature = new ProvenanceSignature(quad,level,file,LocalDate.now());
		if (provenanceMap.containsKey(signature)) {
			return provenanceMap.get(signature);
		} else {
			String provenanceIdentifier = createProvenanceGraph(quad,level,file,LocalDate.now());
			provenanceMap.put(signature, provenanceIdentifier);
			return provenanceIdentifier;
		}
	}

	private String createProvenanceGraph(Quad quad, String level, String file, LocalDate now) {
		ProvenanceGraph provenanceGraph = new ProvenanceGraph(quad,level,file,now);
		provenanceGraphMap.put(provenanceGraph.getProvenanceIdentifier(), provenanceGraph);
		ProvenanceSignature signature = new ProvenanceSignature (quad,level,file,now);
		provenanceMap.put(signature, provenanceGraph.getProvenanceIdentifier());
		return provenanceGraph.getProvenanceIdentifier();
	}

	public Set<Quad> getProvenanceTriples() {
		Set<Quad> provenanceQuads = new HashSet<Quad>();
		for (ProvenanceGraph provenanceGraph : provenanceGraphMap.values()) {
			 provenanceQuads.addAll(provenanceGraph.getQuads());
		}
		return provenanceQuads;
	}
}
