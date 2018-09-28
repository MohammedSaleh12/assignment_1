package view;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class controller {

	@FXML         
	ListView<String> listView;                
	@FXML Button add;
	@FXML Button delete;
	@FXML Button edit;
	private ObservableList<String> obsList;              

	public void start(Stage mainStage) {                
		// create an ObservableList 
		// from an ArrayList  
		

	}
	
	
	
	public void add(ActionEvent e) {
		
	}
	
	public void delete(ActionEvent e) {
		
	}
	
	public void edit(ActionEvent e) {
		
	}

}


