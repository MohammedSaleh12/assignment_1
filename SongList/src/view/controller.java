package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	
	private ArrayList<SongDetail> songlist = new ArrayList<SongDetail>();  
	private ObservableList<String> user_display = FXCollections.observableArrayList();
	
	public void start(Stage mainStage) {                
		// create an ObservableList 
		// from an ArrayList  
		//name.setDisable(true);
		//artist.setDisable(true);
		//album.setDisable(true);
		//year.setDisable(true);

	}
	
	
	
	public void add(ActionEvent e) {
		if(song.getText().isEmpty()  || artist.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			 
			alert.setTitle("'Add' Alert!");
			alert.setHeaderText("Either song name or artist name missing. Please try again");
			alert.setContentText("");
			 
			alert.showAndWait();
			return;
		}
			// Check if the song name and the artist name exists within songlist
			for (SongDetail songdetail : songlist) {
				if(song.getText().equals(songdetail.GetSongName()) && artist.getText().equals(songdetail.GetArtistName())) {
					// TODO error dialog
					Alert alert = new Alert(AlertType.ERROR);
					 
					alert.setTitle("Found duplicate artist name and song name!");
					alert.setHeaderText("Cannot have enter existing artist name and song name. Sorry!");
					alert.setContentText("");
					 
					alert.showAndWait();
					return;
				}
			}
			String album_name = album.getText();
			String song_year = year.getText();
			// create new SongDetail and input into both songlist and obsonglist.
			if(album_name.isEmpty() && song_year.isEmpty()) {
				SongDetail new_song = new SongDetail(song.getText(), artist.getText());
				songlist.add(new_song);
				user_display.add(new_song.GetSongName());
			}
			else if(album_name.isEmpty()) {
				SongDetail new_song = new SongDetail(song.getText(), artist.getText(), Integer.valueOf(song_year));
				songlist.add(new_song);
			}
			else if(song_year.isEmpty()) {
				SongDetail new_song = new SongDetail(song.getText(), artist.getText(), album_name);
				songlist.add(new_song);
			}
			else {
				SongDetail new_song = new SongDetail(song.getText(), artist.getText());
				songlist.add(new_song);
			}
			songlist = SortSongs(songlist, songlist.size());
			
			// Update user_display for alphabetical order
			user_display = FXCollections.observableArrayList();
			for(SongDetail songer : songlist) {
				user_display.add(songer.GetSongName());
			}
			listView.setItems(user_display);

		
	}
	
	
	public ArrayList<SongDetail> SortSongs(ArrayList<SongDetail> songlist, int f){
		SongDetail temp = null;
		for(int i=0;i<f;i++){
			for(int j=i+1;j<f;j++){
					if(songlist.get(i).GetSongName().compareToIgnoreCase(songlist.get(j).GetSongName())>0){
						temp = songlist.get(i);
						songlist.set(i, songlist.get(j));
						songlist.set(j, temp);
					}
			}
		}
		
		return songlist;
	}
	
	
	public void delete(ActionEvent e) {
		
	}
	
	public void edit(ActionEvent e) {
		
	}
	

}


