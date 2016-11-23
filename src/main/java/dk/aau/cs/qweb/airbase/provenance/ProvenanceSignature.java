package dk.aau.cs.qweb.airbase.provenance;

import java.time.LocalDate;

import dk.aau.cs.qweb.airbase.types.Quad;

public class ProvenanceSignature {

	private Quad quad;
	private String level;
	private String file;
	private LocalDate now;

	public ProvenanceSignature(Quad quad, String level, String file, LocalDate now) {
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

	public String getFile() {
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

}
