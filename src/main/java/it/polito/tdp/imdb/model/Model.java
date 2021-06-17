package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Map<Integer,Director> mapRegisti;
	private Graph<Director,DefaultWeightedEdge> grafo;
	private List<Director> percorso;
	
	public Model() {
		dao=new ImdbDAO();
		mapRegisti=new HashMap<>();
		dao.listAllDirectors(mapRegisti);
	}
	
	public void creaGrafo(Integer anno) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(grafo, dao.getVertici(anno, mapRegisti));
		
		//archi
		for(Adiacenza a:dao.getArchi(anno, mapRegisti)) {
			if(!grafo.containsEdge(a.getD1(), a.getD2()))
			Graphs.addEdge(grafo, a.getD1(), a.getD2(), a.getPeso());
		}
		
		System.out.println("#vertici: "+grafo.vertexSet().size());
		System.out.println("#archi: "+grafo.edgeSet().size());
	}
	
	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}
	
	public Set<Director> getRegisti(){
		return grafo.vertexSet();
	}
	
	public List<Adiacenza> getRegistiAdiacenti(Director d){
		Set<DefaultWeightedEdge> uscenti=grafo.outgoingEdgesOf(d);
		List<Adiacenza> result=new ArrayList<>();
		for(DefaultWeightedEdge di:uscenti) {
			Adiacenza a=new Adiacenza(d,grafo.getEdgeTarget(di),(int)grafo.getEdgeWeight(di));
			result.add(a);
		}
		Collections.sort(result);
		return result;
	}
	
	public List<Director> trovaPercorso(Director d, Integer c) {
		percorso=new ArrayList<>();
		percorso.add(d);
		List<Director> parziale=new ArrayList<>();
		parziale.add(d);
		faiRicorsione(d,parziale,0,c);
		return percorso;
	}
	
	private void faiRicorsione(Director d, List<Director> parziale, Integer attori,Integer c) {
		if(attori==c) {
			if(percorso.size()<parziale.size()) {
				percorso=new ArrayList<>(parziale);
			}
			return;
		}
		
		if(Graphs.neighborListOf(grafo, d)==null) {
			if(percorso.size()<parziale.size()) {
				percorso=new ArrayList<>(parziale);
			}
			return;
		}
		
		int a=0;
		for(Director director: Graphs.neighborListOf(grafo, d)) {
			if(!parziale.contains(director)) {
			a=1;
			if(attori+(int)grafo.getEdgeWeight(grafo.getEdge(d, director))<=c) {
			parziale.add(director);			
			
			faiRicorsione(director,parziale,attori+(int)grafo.getEdgeWeight(grafo.getEdge(d, director)),c);
			
			parziale.remove(parziale.size()-1);}
			}
		}
		if(a==0) {
			if(percorso.size()<parziale.size()) {
				percorso=new ArrayList<>(parziale);
			}
			return;
		}
	}
	
}
