package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class Date2Date implements CallBack {

	@Override
	public Object callBackMethod(String value, Tuple tuple) {
		return new Object(value,XSD.dateType);
	}
}
