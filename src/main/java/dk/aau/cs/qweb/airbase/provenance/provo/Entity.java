package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.provenance.provo.PROV;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.vocabulary.PROVvocabulary;

public class Entity implements PROV {
	
	private List<Activity> wasGeneratedBy = new ArrayList<Activity>();
	private List<Agent> wasAttributedTo = new ArrayList<Agent>();
	private Map<String,String> customProperties = new HashMap<String,String>();
	private String atLocation = "";
	protected String subject = "";
	private static int counter = 1;
	private String generatedAtTime = "";

	public Entity(String string) {
		subject = Config.getNamespace()+string+"/"+counter;
		counter++;
	}
	
	protected Entity() {
	}

	public void atLocation(String string) {
		this.atLocation = string;
	}

	public void wasGeneratedBy(Activity aggregation) {
		wasGeneratedBy.add(aggregation);
	}

	public Set<Quad> getQuads() {
		Set<Quad> quads = new HashSet<Quad>();
		quads.addAll(getType());
		
		for (Entry<String, String> entry : customProperties.entrySet()) {
			quads.add(new Quad(subject, entry.getKey(),entry.getValue(),Config.getProvenanceGraphLabel()));
		}
		
		if (!atLocation.isEmpty()) {
			quads.add(getAtLocation());
		}
		
		if (!generatedAtTime.isEmpty()) {
			quads.add(getGeneratedAtTime());
		}
		
		for (Activity activity : wasGeneratedBy) {
			quads.addAll(activity.getQuads());
		}
		
		for (Agent agent : wasAttributedTo) {
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

	public void setCustomProperty(String key, String value) {
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

	public void generatedAtTime(String string) {
		generatedAtTime = string;
	}
	
	public String getSubject() {
		return "";
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		Quad agent = new Quad(subject, RDF.type.toString(),PROVvocabulary.Entity,Config.getProvenanceGraphLabel());
		list.add(agent);
		return list;
	}
}
