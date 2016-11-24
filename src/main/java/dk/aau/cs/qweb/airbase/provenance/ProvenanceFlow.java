package dk.aau.cs.qweb.airbase.provenance;

import java.util.Set;

import dk.aau.cs.qweb.airbase.types.Quad;

public interface ProvenanceFlow {
	Set<Quad> getQuads();
	String getProvenanceIdentifier();
}
