package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		final String sql = "SELECT Data, Umidita " + 
						   "FROM situazione " + 
						   "WHERE localita = ? AND MONTH(Data) = ?";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setInt(2, mese);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(localita, rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return rilevamenti;
	}

	public String getAvgRilevamentiMese(Month mese) {

		final String sql = "SELECT Localita, AVG (Umidita) as avgUmidita " + 
						   "FROM situazione " + 
						   "WHERE MONTH(Data) = ? " + 
						   "GROUP BY Localita";

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			// si accede al mese come int attraverso il metodo getValue()
			st.setInt(1, mese.getValue());
			ResultSet rs = st.executeQuery();

			StringBuilder result = new StringBuilder(); 
			result.append("Umidità media nel mese di " + mese + "\n");
			
			while (rs.next()) {

// UTILE PER GESTIRE LE DATE				
//				Date data = rs.getDate("Data");
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(data);
//				cal.get(Calendar.MONTH)

				result.append(String.format("%s \t%f \n", rs.getString("Localita"), rs.getDouble("avgUmidita")));
			}

			conn.close();
			return result.toString();

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	public List<String> getCities() {

		final String sql = "SELECT DISTINCT Localita " + 
				   		   "FROM situazione";
		
		List <String> cities = new ArrayList <> ();
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
		
			
			while (rs.next()) 
				cities.add(rs.getString("Localita"));	
			
			conn.close();
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
		return cities;
	}
}