package dk.aau.cs.qweb.airbase.types;

public class Quad {

	private String subject;
	private String predicate;
	private String object;
	private String graphLabel;

	public Quad(String subject, String predicate, String object, String graphLabel) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		this.graphLabel = graphLabel;
	}
	
	public Quad(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		this.graphLabel = null;
	}

	public void setGraphLabel(String graphLabel2) {
		// TODO Auto-generated method stub
		
	}
}
