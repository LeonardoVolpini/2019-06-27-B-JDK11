package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> vertici;
	private List<Adiacenza> adiacenze;
	private boolean grafoCreato;
	private double pesoMedio;
	
	private List<String> percorsoBest;
	
	public Model() {
		this.dao = new EventsDao();
		this.vertici= new ArrayList<>();
		this.adiacenze= new ArrayList<>();
		this.grafoCreato=false;
		this.percorsoBest= new ArrayList<>(); 
	}
	
	public void creaGrafo(String categoria, int mese) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.vertici= dao.getVertici(categoria,mese);
		Graphs.addAllVertices(grafo, this.vertici);
		this.adiacenze= dao.getAdiacenza(categoria, mese);
		for (Adiacenza a : this.adiacenze) {
			if (grafo.vertexSet().contains(a.getV1()) && grafo.vertexSet().contains(a.getV2())) {
				Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2(), (double)a.getPeso());
			}
		}
		this.grafoCreato=true;
		//this.ci= new ConnectivityInspector<>(grafo);
	}
	
	public List<Integer> getAllMonths(){
		return this.dao.getAllMonths();
	}
	
	public List<String> getAllCategories(){
		return this.dao.getAllCategories();
	}
	
	public boolean isGrafoCreato() {
		return grafoCreato;
	}

	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}

	public List<Adiacenza> migliori(){ 
		List<Adiacenza> ris= new ArrayList<>();
		double medio=0.0;
		double tot=0.0;
		for (Adiacenza a : this.adiacenze) {
			tot+=a.getPeso();
		}
		medio=tot/((double)this.adiacenze.size());
		for (Adiacenza a : this.adiacenze) {
			if (a.getPeso()>medio)
				ris.add(a);
		}
		this.pesoMedio=medio;
		return ris;
	}

	public double getPesoMedio() {
		return pesoMedio;
	}
	
	public List<String> percorsoMax(Adiacenza arco){ 
		String partenza = arco.getV1();
    	String arrivo= arco.getV2();
		this.percorsoBest=null; 
		List<String> parziale= new ArrayList<>();
		parziale.add(partenza);  
		ricorsione(parziale, arrivo);
		return this.percorsoBest;
	}
	private void ricorsione(List<String> parziale, String arrivo){
		String ultimo = parziale.get(parziale.size()-1);
		if (ultimo.equals(arrivo)){ 
			if(this.percorsoBest==null || parziale.size()>this.percorsoBest.size()){ 
				this.percorsoBest= new ArrayList<>(parziale);
				return;
			}
			else //ho trovato una soluzione ma non è la migliore
				return;
		}
		//da qui faccio la ricorsione:
		for (String prossimo : Graphs.neighborListOf(grafo, ultimo)) {
			if(!parziale.contains(prossimo)) {
				parziale.add(prossimo);
				ricorsione(parziale,arrivo);
				parziale.remove(prossimo);
			}
		}
	}

}
