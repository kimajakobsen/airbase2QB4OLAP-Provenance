package dk.aau.cs.qweb.qboairbase.provenance.provo;

import java.util.ArrayList;

import java.util.List;
import java.util.Map.Entry;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.qboairbase.Config;
import dk.aau.cs.qweb.qboairbase.types.Object;
import dk.aau.cs.qweb.qboairbase.types.Quad;
import dk.aau.cs.qweb.qboairbase.vocabulary.PROVvocabulary;

public class Software extends Agent implements PROV {

	public Software(String string) {
		subject = Config.getProvenanceGraphLabel()+"software/"+string;
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		list.addAll(super.getType());
		Quad agent = new Quad(subject, RDF.type.toString(),new Object(PROVvocabulary.SoftwareAgent),Config.getProvenanceGraphLabel());
		
		for (Entry<String, Object> entry : customProperties.entrySet()) {
			list.add(new Quad(subject, entry.getKey(),entry.getValue(),Config.getProvenanceGraphLabel()));
		}
		
		list.add(agent);
		return list;
	}
	
	
}
