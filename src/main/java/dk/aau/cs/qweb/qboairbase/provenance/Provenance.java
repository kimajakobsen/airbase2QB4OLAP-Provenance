package dk.aau.cs.qweb.qboairbase.provenance;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dk.aau.cs.qweb.qboairbase.types.Quad;
import dk.aau.cs.qweb.qboairbase.types.Tuple;

public class Provenance {

	private static Provenance instance = null;
	private Map<ProvenanceSignature,String> provenanceMap = new HashMap<ProvenanceSignature, String>();
	private Map<String,ProvenanceGraph> provenanceGraphMap = new HashMap<String, ProvenanceGraph>();
	private Set<String> subjects = new LinkedHashSet<>();
	
	
	private Provenance() { }
	
	public static Provenance getInstance() {
		if(instance == null) {
			instance = new Provenance();
		}
		return instance;
	}

	public String getProvenanceIdentifier(Quad quad, String level, List<String> files, Tuple tuple) {
		ProvenanceSignature signature = new ProvenanceSignature(quad, level, LocalDate.now(), files, tuple);
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

	public Set<Quad> getProvenanceQuads() {
		Set<Quad> provenanceQuads = new HashSet<Quad>();
		for (ProvenanceGraph provenanceGraph : provenanceGraphMap.values()) {
			if (!subjects.contains(provenanceGraph.getProvenanceIdentifier())) {
				Set<Quad> qds = provenanceGraph.getQuads();
				provenanceQuads.addAll(qds);
				subjects.add(provenanceGraph.getProvenanceIdentifier());
			}
			
		}
		return provenanceQuads;
	}
	
	public Set<Quad> getProvenanceGraph(String provenanceIdentifier) {
		Set<Quad> provenanceQuads = new HashSet<Quad>();
		provenanceQuads.addAll(provenanceGraphMap.get(provenanceIdentifier).getQuads());
		return provenanceQuads;
	}
	
	public boolean subjectExists(String subject) {
		return subjects.contains(subject);
	}
	
	public void registerSubject(String subject) {
		subjects.add(subject);
	}

	public void clearProvenance() {
		provenanceGraphMap.clear();
		provenanceMap.clear();
	}
	
}
