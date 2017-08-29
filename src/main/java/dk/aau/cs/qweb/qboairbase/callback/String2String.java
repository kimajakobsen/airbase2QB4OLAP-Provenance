package dk.aau.cs.qweb.qboairbase.callback;

import dk.aau.cs.qweb.qboairbase.types.Object;
import dk.aau.cs.qweb.qboairbase.types.Tuple;
import dk.aau.cs.qweb.qboairbase.vocabulary.XSD;

public class String2String implements CallBack {

	@Override
	public Object callBackMethod(String string, Tuple tuple) {
		return new Object(string,XSD.stringType);
	}
}
