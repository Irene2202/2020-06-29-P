package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private SimpleWeightedGraph<Match, DefaultWeightedEdge> grafo;
	private Map<Integer, Match> idMap;
	
	public Model() {
		dao=new PremierLeagueDAO();
		idMap=new HashMap<>();
		dao.listAllMatches(idMap);
	}
	
	public void creaGrafo(int mese, int minuti) {
		grafo=new SimpleWeightedGraph<Match, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(grafo, dao.getVertici(mese, idMap));
		
		//archi
		for(Arco a:dao.getArchi(mese, minuti, idMap)) {
			Graphs.addEdge(grafo, a.getM1(), a.getM2(), a.getPeso());
		}
		
		//System.out.println("vertici:"+grafo.vertexSet().size());
		//System.out.println("archi: "+grafo.edgeSet().size());
	}
	
	public Graph<Match, DefaultWeightedEdge> getGrafo(){
		return grafo;
	}
	
	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<ConnessioniMax> getConnessioniMax(){
		List<ConnessioniMax> result=new ArrayList<>();
		int maxGiocatori=0;
		
		for(DefaultWeightedEdge e: grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>maxGiocatori)
				maxGiocatori=(int) grafo.getEdgeWeight(e);
		}
		
		for(DefaultWeightedEdge e: grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)==maxGiocatori)
				result.add(new ConnessioniMax(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), grafo.getEdgeWeight(e)));
		}
		
		return result;
	}
	
}
