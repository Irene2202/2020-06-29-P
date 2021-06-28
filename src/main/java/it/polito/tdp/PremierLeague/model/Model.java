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
	
	private List<Match> percorsoMigliore;
	private int pesoMax;
	
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
	
	public List<Match> getCollegamento(Match m1, Match m2) {
		percorsoMigliore=new ArrayList<>();
		pesoMax=0;
		
		List<Match> parziale=new ArrayList<>();
		parziale.add(m1);
		int pesoParziale=0;
		
		cerca(m2, parziale, pesoParziale);
		
		return percorsoMigliore;
	}
	
	public int getPesoCollegamento() {
		return pesoMax;
	}

	private void cerca(Match destinazione, List<Match> parziale, int pesoParziale) {
		//caso terminale
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(pesoParziale>pesoMax) {
				this.percorsoMigliore=new ArrayList<>(parziale);
				pesoMax=pesoParziale;
			}
			return;
		}
		
		
		//ricorsione
		for(DefaultWeightedEdge e:grafo.edgesOf(parziale.get(parziale.size()-1))) {
			//controllo che il vertice che vado ad inserire non sia già in parziale,
			//perchè in quel caso avrei un ciclo
			Match vicino=Graphs.getOppositeVertex(grafo, e, parziale.get(parziale.size()-1));
			if(!parziale.contains(vicino)) {
				
				//devo anche controllare che non passi da match T1vsT2 a match con T2vsT1
				
				int casaPartenza=parziale.get(parziale.size()-1).teamHomeID;
				int casaArrivo=vicino.teamHomeID;
				int ospitiPartenza=parziale.get(parziale.size()-1).teamAwayID;
				int ospitiArrivo=vicino.teamAwayID;
				
				if(!(casaPartenza==ospitiArrivo || ospitiPartenza==casaArrivo)) {
					parziale.add(vicino);
					pesoParziale+=grafo.getEdgeWeight(e);
					cerca(destinazione, parziale, pesoParziale);
					pesoParziale-=grafo.getEdgeWeight(e);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}

	
}
