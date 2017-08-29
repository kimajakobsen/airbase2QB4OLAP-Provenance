package dk.aau.cs.qweb.qboairbase.provenance;

import java.util.Set;

import dk.aau.cs.qweb.qboairbase.types.Quad;

public class ProvenanceGraph {
	private ProvenanceFlow provenanceFlow = null;
	private String provenanceIdentifier = "";
	
	public ProvenanceGraph(ProvenanceSignature signature) {
		 provenanceFlow = new LongProvenanceWithSharedAncestors(signature);
		 provenanceIdentifier = provenanceFlow.getProvenanceIdentifier();
	}

	public Set<Quad> getQuads() {
		return provenanceFlow.getQuads();
	}

	public String getProvenanceIdentifier() {
		return provenanceIdentifier;
	}

}
