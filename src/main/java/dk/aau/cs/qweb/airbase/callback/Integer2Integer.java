package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class Integer2Integer implements CallBack {

	@Override
	public Object callBackMethod(String string, Tuple tuple) {
		return new Object(string,XSD.integerType);
	}
}
