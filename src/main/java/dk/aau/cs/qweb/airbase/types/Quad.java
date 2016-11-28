package dk.aau.cs.qweb.airbase.types;

public class Quad {

	private String subject;
	private String predicate;
	private Object object;
	private String graphLabel;

	public Quad(String subject, String predicate, Object object, String graphLabel) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		this.graphLabel = graphLabel;
	}
	
	public Quad(String subject, String predicate, Object object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		this.graphLabel = null;
	}

	public void setGraphLabel(String graphLabel2) {
		graphLabel = graphLabel2;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getGraphLabel() {
		return graphLabel;
	}
	
	@Override
	public String toString() {
		return subject +" "+predicate + " " + object + " " + graphLabel;
	}
}
