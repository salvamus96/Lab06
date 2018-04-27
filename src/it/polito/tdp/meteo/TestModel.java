package it.polito.tdp.meteo;

import java.time.Month;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		System.out.println(m.getUmiditaMedia(Month.MAY));
		
		System.out.println(m.trovaSequenza(Month.MAY));
		
//		System.out.println(m.trovaSequenza(4));
	}

}
