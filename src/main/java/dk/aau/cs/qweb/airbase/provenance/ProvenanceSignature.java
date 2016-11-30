package dk.aau.cs.qweb.airbase.provenance;

import java.time.LocalDate;

import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.Tuple;

public class ProvenanceSignature {

	private Quad quad;
	private String level;
	private String file;
	private LocalDate now;
	private Tuple tuple;

	public ProvenanceSignature(Quad quad, String level, String file, LocalDate now, Tuple tuple) {
		this.tuple = tuple;
		this.setQuad(quad);
		this.setLevel(level);
		this.setFile(file);
		this.setNow(now);
	}

	public Quad getQuad() {
		return quad;
	}

	public void setQuad(Quad quad) {
		this.quad = quad;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getFilePath() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public LocalDate getNow() {
		return now;
	}

	public void setNow(LocalDate now) {
		this.now = now;
	}
	
	public Tuple getTuple() {
		return tuple;
	}

	public String getRawDataFilePath() {
		String[] split = file.split("/");
		String path = "";
		for (int i = 0; i < split.length-1; i++) {
			path += split[i]+"/"; 
		}
		path += getRawDataFileName();
		return path;
	}

	public String getRawDataFileName() {
		String[] splitPath = file.split("/");
		String[] splitName = splitPath[splitPath.length-1].split("_");
		String name = "AirBase_"+splitName[1]+"_v8Vrawdata.zip";
		return name;
	}

	public String getFileName() {
		String[] split = file.split("/");
		return split[split.length-1];
	}

}
