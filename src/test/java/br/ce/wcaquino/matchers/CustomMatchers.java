package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class CustomMatchers {

	public static DiaSemanaMatcher caiEm(Integer day) {
		return new DiaSemanaMatcher(day);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
}
