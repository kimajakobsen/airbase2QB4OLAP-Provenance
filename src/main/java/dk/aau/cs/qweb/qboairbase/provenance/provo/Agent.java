package dk.aau.cs.qweb.qboairbase.provenance.provo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.qboairbase.Config;
import dk.aau.cs.qweb.qboairbase.provenance.Provenance;
import dk.aau.cs.qweb.qboairbase.types.Object;
import dk.aau.cs.qweb.qboairbase.types.Quad;
import dk.aau.cs.qweb.qboairbase.vocabulary.PROVvocabulary;
import dk.aau.cs.qweb.qboairbase.vocabulary.XSD;

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
		if (Provenance.getInstance().subjectExists(subject)) {
			return quads;
		}
		Provenance.getInstance().registerSubject(subject);
		quads.addAll(getType());
		
		for (String key : customProperties.keySet()) {
			quads.add(new Quad(subject, key, customProperties.get(key), Config.getProvenanceGraphLabel()));
		}
		
		if (atLocation != null) {
			quads.add(getAtLocation());
		}
		
		return quads;
	}
	
	private Quad getAtLocation() {
		return new Quad(subject,PROVvocabulary.atLocation, atLocation, Config.getProvenanceGraphLabel());
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
