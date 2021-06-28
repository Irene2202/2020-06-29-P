package it.polito.tdp.PremierLeague.model;

public class Mese {
	
	private int meseN;
	private String meseS;
	
	public Mese(int meseN) {
		this.meseN=meseN;
		calcolaNomeMese();
	}

	public int getMeseN() {
		return meseN;
	}

	public String getMeseS() {
		return meseS;
	}

	private void calcolaNomeMese() {
		switch (meseN){
		case 1:
			meseS="Gennaio";
			break;
		case 2:
			meseS="Febbraio";
			break;
		case 3:
			meseS="Marzo";
			break;
		case 4:
			meseS="Aprile";
			break;
		case 5:
			meseS="Maggio";
			break;
		case 6:
			meseS="Giugno";
			break;
		case 7:
			meseS="Luglio";
			break;
		case 8:
			meseS="Agosto";
			break;
		case 9:
			meseS="Settembre";
			break;
		case 10:
			meseS="Ottobre";
			break;
		case 11:
			meseS="Novembre";
			break;
		case 12:
			meseS="Dicembre";
			break;
		}
	}

	@Override
	public String toString() {
		return meseS;
	}

}
