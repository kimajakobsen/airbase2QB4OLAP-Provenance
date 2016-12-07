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
import dk.aau.cs.qweb.airbase.provenance.ProvenanceSignature;
import dk.aau.cs.qweb.airbase.provenance.provo.PROV;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.vocabulary.PROVvocabulary;

public class Entity implements PROV {
	
	private List<Activity> wasGeneratedBy = new ArrayList<Activity>();
	private List<Agent> wasAttributedTo = new ArrayList<Agent>();
	private Map<String,Object> customProperties = new HashMap<String,Object>();
	private Object atLocation = null;
	protected String subject = "";
	private static int counter = 1;
	private Object generatedAtTime = null;

	public Entity(String string) {
		subject = Config.getNamespace()+string+"/"+Config.getCountryCode()+counter;
		counter++;
	}
	
	protected Entity() {
	}

	public Entity(String string, String rawDataFileName) {
		subject = Config.getNamespace()+string+"/"+rawDataFileName;
	}

	public Entity(String string, ProvenanceSignature signature) {
		subject = Config.getNamespace()+string+"/"+Config.getCurrentInputFileName()+signature.getTuple().getLineCount();
	}

	public void atLocation(Object string) {
		this.atLocation = string;
	}

	public void wasGeneratedBy(Activity aggregation) {
		wasGeneratedBy.add(aggregation);
	}

	public Set<Quad> getQuads() {
		Set<Quad> quads = new HashSet<Quad>();
		if (ProvenanceIndex.contains(subject)) {
			return quads;
		} else {
			ProvenanceIndex.add(subject);
		}
		
		quads.addAll(getType());
		
		for (Entry<String, Object> entry : customProperties.entrySet()) {
			quads.add(new Quad(subject, entry.getKey(),(entry.getValue()),Config.getProvenanceGraphLabel()));
		}
		
		if (atLocation != null) {
			quads.add(getAtLocation());
		}
		
		if (generatedAtTime != null) {
			quads.add(getGeneratedAtTime());
		}
		
		for (Activity activity : wasGeneratedBy) {
			quads.add(new Quad(subject, PROVvocabulary.wasGeneratedBy,new Object(activity.getSubject()),Config.getProvenanceGraphLabel()));
			quads.addAll(activity.getQuads());
		}
		
		for (Agent agent : wasAttributedTo) {
			quads.add(new Quad(subject, PROVvocabulary.wasAttributedTo,new Object(agent.getSubject()),Config.getProvenanceGraphLabel()));
			quads.addAll(agent.getQuads());
		}
		
		return quads;
	}

	private Quad getGeneratedAtTime() {
		return  new Quad(subject,PROVvocabulary.generatedAtTime,generatedAtTime,Config.getProvenanceGraphLabel());
	}

	private Quad getAtLocation() {
		return new Quad(subject,PROVvocabulary.atLocation,atLocation,Config.getProvenanceGraphLabel());
	}

	public void setCustomProperty(String key, Object value) {
		customProperties.put(key, value);
	}

	public void wasAttributedTo(Agent agent) {
		wasAttributedTo.add(agent);
	}

	public void wasAttributedTo(List<Agent> countryOrganizations) {
		for (Agent agent : countryOrganizations) {
			wasAttributedTo(agent);
		}
	}

	public void generatedAtTime(Object string) {
		generatedAtTime = string;
	}
	
	public String getSubject() {
		return subject;
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		Quad agent = new Quad(subject, RDF.type.toString(),new Object(PROVvocabulary.Entity),Config.getProvenanceGraphLabel());
		list.add(agent);
		return list;
	}
}
