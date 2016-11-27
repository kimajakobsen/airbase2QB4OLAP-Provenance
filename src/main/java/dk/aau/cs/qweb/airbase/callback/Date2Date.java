package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class Date2Date implements CallBack {

	@Override
	public String callBackMethod(String value, Tuple tuple) {
		return value+XSD.dateType;
	}
}
