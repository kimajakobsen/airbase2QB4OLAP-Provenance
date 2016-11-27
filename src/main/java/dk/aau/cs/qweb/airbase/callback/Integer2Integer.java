package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class Integer2Integer implements CallBack {

	@Override
	public String callBackMethod(String integer, Tuple tuple) {
		return integer+XSD.integerType;
	}

}
