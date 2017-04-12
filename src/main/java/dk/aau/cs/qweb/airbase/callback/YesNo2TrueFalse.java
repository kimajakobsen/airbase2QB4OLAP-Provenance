package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class YesNo2TrueFalse implements CallBack {

	@Override
	public Object callBackMethod(String string, Tuple tuple) {
		if (string.equals("yes")) {
			return new Object("true",XSD.booleanType);
		} else if (string.equals("no")) {
			return new Object("false",XSD.booleanType);
		} else {
			return new Object("");
		}
	}
}
