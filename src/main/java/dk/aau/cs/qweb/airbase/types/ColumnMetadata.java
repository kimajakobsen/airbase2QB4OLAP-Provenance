package dk.aau.cs.qweb.airbase.types;

import java.util.HashMap;
import java.util.Map;

import dk.aau.cs.qweb.airbase.callback.CallBack;

public class ColumnMetadata {

	private String name = "";
	private boolean isPartOfCube = false;
	private Map<String,Boolean> levels = new HashMap<String,Boolean>();
	private CallBack callback = new CallBackDefault();

	class CallBackDefault implements CallBack {
		@Override
		public Object callBackMethod(String string, Tuple tuple) {
			return new Object(string);
		}
	}
	
	public ColumnMetadata() {
		this.isPartOfCube = false;
	}
	
	public ColumnMetadata(String name, Map<String,Boolean> levels) {
		this.name = name;
		this.isPartOfCube = true;
		this.levels.putAll(levels);;
	}
	
	public ColumnMetadata(Map<String,Boolean> levels) {
		this.levels.putAll(levels);;
	}
	
	public ColumnMetadata(String name, Map<String,Boolean> levels, CallBack callback) {
		this.name = name;
		this.isPartOfCube = true;
		this.levels.putAll(levels);
		this.callback = callback;
	}
	
	public ColumnMetadata(Map<String,Boolean> levels, CallBack callback) {
		this.levels.putAll(levels);
		this.callback = callback;
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
}
