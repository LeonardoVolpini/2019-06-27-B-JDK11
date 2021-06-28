/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo...\n");
    	String categoria= this.boxCategoria.getValue();
    	if (categoria==null) {
    		this.txtResult.setText("Selezionare una categoria");
    		return;
    	}
    	Integer mese= this.boxMese.getValue();
    	if (mese==null) {
    		this.txtResult.setText("Selezionare un mese");
    		return;
    	}
    	this.model.creaGrafo(categoria,mese);
    	this.txtResult.appendText("GRAFO CREATO:\n");
    	this.txtResult.appendText("# Vertici: "+model.getNumVertici() );
    	this.txtResult.appendText("\n# Archi: "+model.getNumArchi() );
    	List<Adiacenza> elenco= model.migliori();
    	this.txtResult.appendText("\nGli archi che superano il peso medio di "+model.getPesoMedio()+" sono:\n");
    	for (Adiacenza a : elenco) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    	this.boxArco.getItems().clear(); //pulisco le varie comboBox dipendenti dal grafo
    	this.boxArco.getItems().addAll(elenco); //creo le varie comboBox dipendenti dal grafo
    }
    
    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n");
    	if (!model.isGrafoCreato()) { //con variabile booleane all'interno del model
    		this.txtResult.setText("Prima crea il grafo!!!");
    		return;
    	}
    	Adiacenza arco= this.boxArco.getValue();
    	if (arco==null) {
    		this.txtResult.setText("Selezionare un arco");
    		return;
    	}
    	List<String> best= this.model.percorsoMax(arco);
    	this.txtResult.appendText("\nPercorso migliore trovato:\n");
    	for (String s : best)
    		this.txtResult.appendText(s+"\n");
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(model.getAllCategories());
    	this.boxMese.getItems().addAll(model.getAllMonths());
    }
}
