package dk.aau.cs.qweb.airbase.types;

public class Object {
	private String object = "";
	private String type = "";
	
	public Object(String string) {
		object = string;
	}
	
	public Object(String object, String type) {
		this.object = object;
		this.type = type;
	}

	public Object(int i, String integerType) {
		// TODO Auto-generated constructor stub
	}

	public String getliteral() {
		return object;
	}
	
	public void setObject(String object) {
		this.object = object;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public boolean isEmpty() {
		return object.isEmpty();
	}

	public boolean hasType() {
		// TODO Auto-generated method stub
		return false;
	}
}
