package dk.aau.cs.qweb.airbase.input;

import java.util.Iterator;

import dk.aau.cs.qweb.airbase.types.Tuple;

public interface FileStructure  extends Iterable<Tuple> {

	public Iterator<Tuple> iterator();
}
