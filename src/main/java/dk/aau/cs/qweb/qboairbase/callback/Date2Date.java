package dk.aau.cs.qweb.qboairbase.callback;

import dk.aau.cs.qweb.qboairbase.types.Object;
import dk.aau.cs.qweb.qboairbase.types.Tuple;
import dk.aau.cs.qweb.qboairbase.vocabulary.XSD;

public class Date2Date implements CallBack {

	@Override
	public Object callBackMethod(String value, Tuple tuple) {
		return new Object(value,XSD.dateType);
	}
}
