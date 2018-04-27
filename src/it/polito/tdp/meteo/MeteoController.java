package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {

	private Model model;
	
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Month> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;


	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		this.txtResult.clear();

		if (this.boxMese.getValue() == null)
			this.txtResult.setText("Selezionare un mese!");
		else {
			this.txtResult.appendText("Sequenza ottima per il mese " + this.boxMese.getValue() + "\n");
			this.txtResult.appendText(model.trovaSequenza(this.boxMese.getValue()));
		}
	}


	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		this.txtResult.clear();
		
		if (this.boxMese.getValue() == null)
			this.txtResult.setText("Selezionare un mese!");
		else
			this.txtResult.setText(model.getUmiditaMedia(this.boxMese.getValue()));
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
	
	}
	
	public void setModel(Model model) {
		this.model = model;
				
// si usa il metodo of per accedere al mese relativo al numero intero passato come parametro
		for (int i = 1; i <= 12; i++)
			this.boxMese.getItems().add(Month.of(i));
		
	}

}
