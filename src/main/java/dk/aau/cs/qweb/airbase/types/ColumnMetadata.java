package dk.aau.cs.qweb.airbase.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.aau.cs.qweb.airbase.callback.CallBack;

public class ColumnMetadata {

	private String name = "";
	private boolean isPartOfCube = false;
	private Map<String,Boolean> levels = new HashMap<String,Boolean>();
	private CallBack callback = new CallBackDefault();
	private List<String> files;

	class CallBackDefault implements CallBack {
		@Override
		public Object callBackMethod(String string, Tuple tuple) {
			return new Object(string);
		}
	}
	
	public ColumnMetadata() {
		this.isPartOfCube = false;
	}
	
	public ColumnMetadata(List<String> files) {
		this.isPartOfCube = false;
		this.files = new ArrayList<String>(files);
	}
	
	public ColumnMetadata(String name, Map<String,Boolean> levels, List<String> files) {
		this.name = name;
		this.isPartOfCube = true;
		this.levels.putAll(levels);
		this.files = new ArrayList<String>(files);
	}
	
	public ColumnMetadata(Map<String,Boolean> levels, List<String> files) {
		this.levels.putAll(levels);
		this.files = new ArrayList<String>(files);
	}
	
	public ColumnMetadata(String name, Map<String,Boolean> levels, CallBack callback, List<String> files) {
		this.name = name;
		this.isPartOfCube = true;
		this.levels.putAll(levels);
		this.callback = callback;
		this.files = new ArrayList<String>(files);
	}
	
	public ColumnMetadata(Map<String,Boolean> levels, CallBack callback, List<String> files) {
		this.levels.putAll(levels);
		this.callback = callback;
		this.files = new ArrayList<String>(files);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isPartOfCube() {
		return isPartOfCube;
	}
	
	public CallBack getCallBackFunction() {
		return callback;
	}
	
	public Map<String,Boolean> getLevels() {
		return levels;
	}
	
	public List<String> getFiles() {
		return files;
	}
}
