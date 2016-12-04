package dk.aau.cs.qweb.airbase.types;

import java.util.ArrayList;
import java.util.List;

public class Tuple {

	private List<String> data = new ArrayList<String>();
	private List<String> header = new ArrayList<String>();
	private int lineCount;

	public Tuple(List<String> data, List<String> header, int lineCount) {
		this.data = data;
		this.header = header;
		this.lineCount = lineCount;
	}
	
	public List<String> getData() {
		return data;
	}
	
	public List<String> getHeader() {
		return header;
	}
	
	public String getValue(String header) {
		int index = 0;
		String result = "";
		for (String string : this.header) {
			if (header.equals(string)) {
				result = data.get(index);
				return result;
			}
			index++;
		}
		return result;
	}
	
	@Override
	public String toString() {
		String string = "";
		int index = 0;
		for (String column : header) {
			string += column+": ";
			string += data.get(index)+", ";
			index++;
		}
		return string;
	}

	public int getLineCount() {
		return lineCount;
	}
}
