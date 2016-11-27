package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class YesNo2TrueFalse implements CallBack {


	@Override
	public String callBackMethod(String string, Tuple tuple) {
		if (string.equals("yes")) {
			return "\"True\""+XSD.booleanType;
		} else if (string.equals("no")) {
			return "\"False\""+XSD.booleanType;
		}
		throw new IllegalArgumentException("the parameter "+ string +" must be yes or no");
	}
}
