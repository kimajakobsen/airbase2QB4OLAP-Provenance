package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class Decimal2Decimal implements CallBack {

	@Override
	public String callBackMethod(String decimal, Tuple tuple) {
		return decimal+XSD.decimalType;
	}

}
