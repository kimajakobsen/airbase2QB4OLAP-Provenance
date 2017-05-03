package dk.aau.cs.qweb.airbase.provenance;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.Tuple;

public class ProvenanceSignature {

	private Quad quad;
	private String level;
	private LocalDate now;
	private List<String> files;
	private Tuple tuple;

	public ProvenanceSignature(Quad quad, String level, LocalDate now, List<String> files, Tuple tuple) {
		this.tuple = tuple;
		this.setQuad(quad);
		this.setLevel(level);
		this.setFiles(files);
		this.setNow(now);
	}
	
	public boolean isObservation() {
		return level.equals("http://qweb.cs.aau.dk/airbase/schema/value");
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

	public LocalDate getNow() {
		return now;
	}

	public void setNow(LocalDate now) {
		this.now = now;
	}
	
	public Tuple getTuple() {
		return tuple;
	}
	
	
	public String getRawDataFileName() {
		return "AirBase_"+tuple.getValue("country_iso_code")+"_v8Vrawdata.zip";
	}

	public String getFileName() {
		StringBuilder strBuilder = new StringBuilder();
		for (String file : files) {
			strBuilder.append("AirBase_"+tuple.getValue("country_iso_code")+"_v8_" + file + ".csv,");
		}
		strBuilder.deleteCharAt(strBuilder.length() - 1);
		return strBuilder.toString();
	}
	
	public String getFileName(String suffix) {
		return "AirBase_"+tuple.getValue("country_iso_code")+"_v8_" + suffix + ".csv";
	}
	
	public String getRemoteFileName(String suffix) {
		return "http://ftp.eea.europa.eu/www/AirBase_v8/AirBase_" + tuple.getValue("country_iso_code")+ "_v8.zip";
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> file) {
		this.files = file;
	}
	
	@Override
	public String toString() {
		return "[" + quad.toString() + ", " + level + ", " + tuple + "]";
	}

}
