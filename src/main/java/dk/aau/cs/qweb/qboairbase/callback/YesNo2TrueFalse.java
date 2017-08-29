package dk.aau.cs.qweb.qboairbase.callback;

import dk.aau.cs.qweb.qboairbase.types.Object;
import dk.aau.cs.qweb.qboairbase.types.Tuple;
import dk.aau.cs.qweb.qboairbase.vocabulary.XSD;

public class YesNo2TrueFalse implements CallBack {

	@Override
	public Object callBackMethod(String string, Tuple tuple) {
		if (string.equals("yes")) {
			return new Object("True",XSD.booleanType);
		} else if (string.equals("no")) {
			return new Object("False",XSD.booleanType);
		} else {
			return new Object("");
		}
	}
}
