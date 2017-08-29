package dk.aau.cs.qweb.qboairbase.provenance;

import java.util.Set;

import dk.aau.cs.qweb.qboairbase.types.Quad;

public interface ProvenanceFlow {
	Set<Quad> getQuads();
	String getProvenanceIdentifier();
}
