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
		object = String.valueOf(i);
		this.type = integerType;
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
		return this.object.isEmpty();
	}

	public boolean hasType() {
		if (!type.isEmpty()) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String object = this.object;
		if (!type.isEmpty()) {
			object = "\""+this.object+"\"";
		} else {
			object = "<"+this.object+">";
		}
		return object+type;
	}
}
