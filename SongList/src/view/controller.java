package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class controller {

	@FXML         
	ListView<String> listView;                
	@FXML Button add;
	@FXML Button delete;
	@FXML Button edit;
	@FXML TextField song;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	
	private ObservableList<String> obsList;              

	public void start(Stage mainStage) {                
		// create an ObservableList 
		// from an ArrayList  
		//name.setDisable(true);
		//artist.setDisable(true);
		//album.setDisable(true);
		//year.setDisable(true);

	}
	
	
	
	public void add(ActionEvent e) {
		
	}
	
	public void delete(ActionEvent e) {
		
	}
	
	public void edit(ActionEvent e) {
		
	}

}


