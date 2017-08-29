package dk.aau.cs.qweb.qboairbase.types;

public class Quad {

	private String subject;
	private String predicate;
	private Object object;
	private String graphLabel = "";

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
		this.graphLabel = "";
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((graphLabel == null) ? 0 : graphLabel.hashCode());
		result = prime * result + ((object == null) ? 0 : object.toString().hashCode());
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(java.lang.Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quad other = (Quad) obj;
		if (graphLabel == null) {
			if (other.graphLabel != null)
				return false;
		} else if (!graphLabel.equals(other.graphLabel))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.toString().equals(other.object.toString()))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String subject = "<"+this.subject+">";
		String predicate = "<"+this.predicate+">";
		String graph = "<"+graphLabel+">";
		
		return  subject + " " + predicate + " " + object.toString() + " " + graph + " .";
	}
}
