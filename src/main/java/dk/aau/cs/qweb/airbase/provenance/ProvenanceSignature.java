package dk.aau.cs.qweb.airbase.provenance;

import java.time.LocalDate;
import java.util.Collection;

import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.Tuple;

public class ProvenanceSignature {

	private Quad quad;
	private String level;
	private LocalDate now;
	private Collection<String> files;
	private Tuple tuple;

	public ProvenanceSignature(Quad quad, String level, LocalDate now, Collection<String> files, Tuple tuple) {
		this.tuple = tuple;
		this.setQuad(quad);
		this.setLevel(level);
		this.setFiles(files);
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
		return "AirBase_"+tuple.getValue("country_iso_code")+"_v8Vrawdata.zip";
	}

	public Collection<String> getFiles() {
		return files;
	}

	public void setFiles(Collection<String> file) {
		this.files = file;
	}

}
