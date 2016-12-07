package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.ArrayList;
import dk.aau.cs.qweb.airbase.types.Object;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.provenance.ProvenanceIndex;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.vocabulary.PROVvocabulary;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class Agent implements PROV {
	protected String subject = "";
	protected Map<String,Object> customProperties = new HashMap<String,Object>();
	private Object atLocation = null;
	
	public Agent(String string) {
		subject = Config.getProvenanceGraphLabel()+"agent/"+Config.getCountryCode()+string;
	}
	
	protected Agent() {}

	@Override
	public Set<Quad> getQuads() {
		Set<Quad> quads = new HashSet<Quad>();
		if (ProvenanceIndex.contains(subject)) {
			return quads;
		} else {
			ProvenanceIndex.add(subject);
		}
		
		quads.addAll(getType());
		
		for (Entry<String, Object> entry : customProperties.entrySet()) {
			quads.add(new Quad(subject, entry.getKey(),entry.getValue(),Config.getProvenanceGraphLabel()));
		}
		
		if (atLocation != null) {
			quads.add(getAtLocation());
		}
		
		return quads;
	}
	
	private Quad getAtLocation() {
		return new Quad(subject,PROVvocabulary.atLocation,new Object(Config.getProvenanceGraphLabel()));
	}

	@Override
	public void setCustomProperty(String key, Object value) {
		customProperties.put(key, value);
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		Quad agent = new Quad(subject, RDF.type.toString(),new Object(PROVvocabulary.Agent),Config.getProvenanceGraphLabel());
		list.add(agent);
		return list;
	}

	public void atLocation(Object string) {
		this.atLocation = string;
	}

	public void setCustomProperty(String key, String string2) {
		Object object =  new Object(string2,XSD.stringType);
		customProperties.put(key, object);
	}

	public String getSubject() {
		return subject;
	}
	
	@Override
	public String toString() {
		return subject;
	}
}
