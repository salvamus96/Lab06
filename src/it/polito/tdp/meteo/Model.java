package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final int COST = 100;
	private final int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final int NUMERO_GIORNI_CITTA_MAX = 6;
	private final int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO meteoDAO;
	private List <Citta> cities;
	
	private List <SimpleCity> soluzione;
	private double minorPunteggio;
	
	public Model() {
		
		meteoDAO = new MeteoDAO ();
		cities = new ArrayList <Citta> ();
		
		// popolare la lista di citta interrogando il database
		for (String s : meteoDAO.getCities())
			cities.add(new Citta(s));
	}

	public String getUmiditaMedia(Month mese) {
		return meteoDAO.getAvgRilevamentiMese(mese);
	}

	public String trovaSequenza(Month mese) {
		
		this.minorPunteggio = Double.MAX_VALUE;
		this.soluzione = new ArrayList <> ();
		
		List <SimpleCity> parziale = new ArrayList <> ();
		
		// reset dei rilevamenti e del contatore delle città
		for (Citta c : cities) {
			c.setRilevamenti(this.meteoDAO.getAllRilevamentiLocalitaMese(mese.getValue(), c.getNome()));
			c.setCounter(0);
		}
		
		// la ricorsiva non ha bisogno della dimensione poichè è una costante
		recursive (parziale, 0);
		
		if(soluzione != null) {
			System.out.println("Punteggio della soluzione ottima: " + this.punteggioSoluzione(soluzione));
			return soluzione.toString();
		}
			
		return "Soluzione non trovata";	
	
	}

	private void recursive(List<SimpleCity> parziale, int livello) {
		
		// condizione di terminazione 
		if (livello >= this.NUMERO_GIORNI_TOTALI) {
			double score = this.punteggioSoluzione(parziale);
			if (score < minorPunteggio) {
				this.minorPunteggio = score;
				soluzione = new ArrayList <> (parziale);
				
			}
			return ;
		}
		
		
		for (Citta c : cities) {
			
			// dalla lista dei rilevamenti della citta considero l'umidità al livello = giorno
			int umidita = c.getRilevamenti().get(livello).getUmidita();
			
			SimpleCity sc = new SimpleCity (c.getNome(), umidita);
			parziale.add(sc);
			
			c.increaseCounter();
			
			// filtro
			if (this.controllaParziale(parziale))
				recursive (parziale, livello + 1);
			
			// backtracking
			parziale.remove(livello);
			c.decreaseCounter();
		}

	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		
//		if (soluzioneCandidata == null || soluzioneCandidata.size() == 0)
//			return Double.MAX_VALUE;
		
		// tutte le città devono essere visitate almeno una volta
		for (Citta c : cities) {
			SimpleCity sc = new SimpleCity(c.getNome());
			if (!soluzioneCandidata.contains(sc))
				return Double.MAX_VALUE;
		}
		
		SimpleCity precedente = soluzioneCandidata.get(0);
		double score = 0.0;
		
		
		for (SimpleCity sc : soluzioneCandidata) {
			if (!precedente.equals(sc))
				score += this.COST;
			
			precedente = sc;
			score += sc.getCosto();
		}
		
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		for (Citta c : cities)
			if (c.getCounter() > this.NUMERO_GIORNI_CITTA_MAX)
				return false;
		
		SimpleCity precedente = parziale.get(0);
		int permanenzaCitta = 0;
		
		for (SimpleCity sc : parziale) {
			if (!precedente.equals(sc)) {
				// tecnico cambia città
				if (permanenzaCitta < this.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					return false;

				precedente = sc;				
				// se il tecnico cambia citta allora rimane in tale città almeno un giorno
				permanenzaCitta = 1;
			
			}else
				permanenzaCitta ++;
		}
		
		return true;
	}

}
