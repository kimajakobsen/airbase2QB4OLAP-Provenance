package dk.aau.cs.qweb.qboairbase.provenance.provo;

import java.util.List;
import java.util.Set;

import dk.aau.cs.qweb.qboairbase.types.Object;
import dk.aau.cs.qweb.qboairbase.types.Quad;

public interface PROV {
	Set<Quad> getQuads();
	List<Quad> getType();
	void setCustomProperty(String property, Object value);
	
	
}
