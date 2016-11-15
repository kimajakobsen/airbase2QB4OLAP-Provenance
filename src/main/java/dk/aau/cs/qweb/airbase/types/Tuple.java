package dk.aau.cs.qweb.airbase.types;

import java.util.List;

public class Tuple {

	private List<String> data;
	private List<String> header;

	public Tuple(List<String> data, List<String> header) {
		this.data = data;
		this.header = header;
	}
	
	public List<String> getData() {
		return data;
	}
	
	public List<String> getHeader() {
		return header;
	}

}
