package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
	//	Model m=new Model();
		
	//	m.creaGrafo(2005);
		
		List<Integer> tot=new ArrayList<>();
		List<Integer> lista=new ArrayList<>();
		tot.add(0);
		tot.add(2);
		tot.add(1);
		tot.add(3);
		tot.add(5);
		tot.add(4);
		
		lista.add(2);
		lista.add(3);
		lista.add(4);
		
		if(tot.containsAll(lista))
			System.out.println("VERO");
		else 
			System.out.println("FALSO");
		

	}

}
