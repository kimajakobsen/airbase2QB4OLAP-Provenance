package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.List;
import java.util.Set;

import dk.aau.cs.qweb.airbase.types.Quad;

public interface PROV {
	Set<Quad> getQuads();
	List<Quad> getType();
	void setCustomProperty(String property, String value);
	
	
}
