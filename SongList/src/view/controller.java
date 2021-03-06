package view;

import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class controller {

	@FXML ListView<String> listView;                
	@FXML Button add;
	@FXML Button delete;
	@FXML Button edit;
	@FXML Button help;
	@FXML TextField song;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	
	public ArrayList<SongDetail> songlist = new ArrayList<SongDetail>();  
	public ObservableList<String> user_display = FXCollections.observableArrayList();
	public Json jsonFile = new Json("SongLib.json");
	
	public void start(Stage mainStage) {     
		jsonFile.initListView(songlist, user_display);
		songlist = SortSongs(songlist, songlist.size());
		// Update user_display for alphabetical order
		user_display = FXCollections.observableArrayList();
		for(SongDetail songer : songlist) {
			user_display.add(songer.GetSongName());
		}			
		listView.setItems(user_display);
		listView.getSelectionModel().select(0);
		if(user_display.size() > 0) {
			int index = listView.getSelectionModel().getSelectedIndex();
			if(index < 0) {
				return;
			}
			SongDetail selectedSong = songlist.get(index);
			song.setText(selectedSong.GetSongName());
			artist.setText(selectedSong.GetArtistName());
			if(selectedSong.GetAlbumName() != null) {
				album.setText(selectedSong.GetAlbumName());
			}
			if(selectedSong.GetYear() != 0){
				year.setText(""+selectedSong.GetYear());
			}
		}
		//getting an exception in thread
		//TODO figure out why there is an exception in thread
		listView.getSelectionModel().selectedIndexProperty().addListener((index, x, y) -> 
		selectItem());
				
			

	}
	
	private void selectItem() {
		int index = listView.getSelectionModel().getSelectedIndex();
		if(index < 0) {
			song.setText("");
			artist.setText("");
			album.setText("");
			year.setText("");
			return;
		}
		SongDetail selectedSong = songlist.get(index);
		song.setText(selectedSong.GetSongName());
		artist.setText(selectedSong.GetArtistName());
		if(selectedSong.GetAlbumName() != null) {
			album.setText(selectedSong.GetAlbumName());
		}else {
			album.setText("");
		}
		if(selectedSong.GetYear() != 0){
			year.setText(""+selectedSong.GetYear());
		}else {
			year.setText("");
		}
	}
	
	public void help(ActionEvent e) {
		Alert alert = new Alert(AlertType.INFORMATION);	 
		alert.setTitle(" How to Use this Application!");
		alert.setHeaderText("1) To add a song, populate the text fields and hit the add button. \n2) To delete, select a song and click delete."
				+ "\n3)To edit, select a song and enter the fields that you would like to change and then click the 'Edit' button.");
		alert.setContentText("");
		 
		alert.showAndWait();
		return;
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
		// Ask user for confirmation of the edit
			Alert alert = new Alert(AlertType.CONFIRMATION);
			 
			alert.setTitle("Confirmation");
			alert.setHeaderText("Are you sure you would like to add this item?");
			alert.setContentText("");
			Optional<ButtonType> ans = alert.showAndWait();
			if(ans.get() == ButtonType.CANCEL) {
				return;
			}
			// Check if the song name and the artist name exists within songlist
			for (SongDetail songdetail : songlist) {
				if(song.getText().equals(songdetail.GetSongName()) && artist.getText().equals(songdetail.GetArtistName())) {
					// TODO error dialog
					alert = new Alert(AlertType.ERROR);
					 
					alert.setTitle("Found duplicate artist name and song name!");
					alert.setHeaderText("Cannot have enter existing artist name and song name. Sorry!");
					alert.setContentText("");
					 
					alert.showAndWait();
					return;
				}
			}
			String album_name = album.getText();
			String song_year = year.getText();
			SongDetail new_song;
			// create new SongDetail and input into both songlist and obsonglist.
			if(song_year.isEmpty() == false) {
				if (song_year.matches("[0-9]+") != true) {
					alert = new Alert(AlertType.ERROR);
					 
					alert.setTitle("Wrong Input!");
					alert.setHeaderText("Please enter only numbers for the year");
					alert.setContentText("");
					 
					alert.showAndWait();
					return;
				}
			}
			if(album_name.isEmpty() && song_year.isEmpty()) {
				new_song = new SongDetail(song.getText(), artist.getText());
				songlist.add(new_song);
				//user_display.add(new_song.GetSongName());
			}
			else if(album_name.isEmpty()) {
				new_song = new SongDetail(song.getText(), artist.getText(), Integer.valueOf(song_year));
				songlist.add(new_song);
			}
			else if(song_year.isEmpty()) {
				new_song = new SongDetail(song.getText(), artist.getText(), album_name);
				songlist.add(new_song);
			}
			else {
				new_song = new SongDetail(song.getText(), artist.getText(), album.getText(), Integer.valueOf(year.getText()));
				songlist.add(new_song);
			}
			songlist = SortSongs(songlist, songlist.size());
			
			
			// get the index where it was newly inserted
			int count = 0;
			for(SongDetail songdetail : songlist) {
				if(song.getText().equals(songdetail.GetSongName()) && artist.getText().equals(songdetail.GetArtistName())) {
					break;
				}
				count++;
			}
			
			// Update user_display for alphabetical order
			user_display.add(count, song.getText());

			listView.setItems(user_display);
			listView.getSelectionModel().select(count);
			jsonFile.AddSongToJSONFile(new_song);

		
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
					else if(songlist.get(i).GetSongName().compareToIgnoreCase(songlist.get(j).GetSongName())==0
							&& songlist.get(i).GetArtistName().compareToIgnoreCase(songlist.get(j).GetArtistName())>0){
						temp = songlist.get(i);
						songlist.set(i, songlist.get(j));
						songlist.set(j, temp);
					}
			}
		}
		
		return songlist;
	}
	
	
	public void delete(ActionEvent e) {
		int index = listView.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			// User has not selected anything to edit
			// Alert User
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot delete a song if empty");
			alert.setHeaderText("Add songs and delete afterwards if you really want to");
			alert.setContentText("");
			 
			alert.showAndWait();
			return;
		}
		// Ask user for confirmation of the edit
		Alert alert = new Alert(AlertType.CONFIRMATION);
		 
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure you would like to delete this item?");
		alert.setContentText("");
		Optional<ButtonType> ans = alert.showAndWait();
		if(ans.get() == ButtonType.CANCEL) {
			return;
		}
		int index_to_select = 0;	
		
		if(index == 0) {
			// then display nothing
			index_to_select = 0;
			
		}
		
		// select previous one
		else if((index) == (songlist.size() - 1)) {
			index_to_select = index - 1;
		}
		
		else {
			index_to_select = index;
		}
		
		jsonFile.DeleteJSONObject(songlist.get(index));
		
		songlist.remove(index);
		user_display.remove(index);
		listView.setItems(user_display);
		listView.getSelectionModel().clearSelection();
		listView.getSelectionModel().select(index_to_select);

		selectItem();

		return;
		
	}
	
	public void edit(ActionEvent e) {
		int index = listView.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			// User has not selected anything to edit
			// Alert User
			Alert alert = new Alert(AlertType.ERROR);
			 
			alert.setTitle("Item not selected");
			alert.setHeaderText("Please select a song to edit!");
			alert.setContentText("");
			 
			alert.showAndWait();
			return;
		}else {
			// Ask user for confirmation of the edit
			Alert alert = new Alert(AlertType.CONFIRMATION);
			 
			alert.setTitle("Confirmation");
			alert.setHeaderText("Are you sure you would like to edit this item?");
			alert.setContentText("");
			Optional<ButtonType> ans = alert.showAndWait();
			if(ans.get() == ButtonType.CANCEL) {
				return;
			}
		}
		String song_year = year.getText();
		if(song_year.isEmpty() == false) {
			if (song_year.matches("[0-9]+") != true) {
				Alert alert = new Alert(AlertType.ERROR);
				 
				alert.setTitle("Wrong Input!");
				alert.setHeaderText("Please enter only numbers for the year");
				alert.setContentText("");
				 
				alert.showAndWait();
				return;
			}
		}
		SongDetail edit_song = songlist.get(index);
		if(song.getText().equals(edit_song.GetSongName())) {  // Seeing if the selected song name equals the song name in the text field
			if(artist.getText().equals(edit_song.GetArtistName())) {  // Seeing if the selected song's artist name equals the artist name in the text field
				if(album.getText().length() == 0 || album.getText().equals(edit_song.GetAlbumName())) {  // Seeing if the selected song's album name equals the album name in the text field 
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) != edit_song.GetYear()) {  // Seeing if the selected song's year equals the year in the text field
 						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));  // Updated the year of the selected song
					}else if(year.getText().length() == 0) {
						songlist.get(index).UpdateYear(0);
					}
				}else {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) != edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));  // Updated the year of the selected song
					}else if(year.getText().length() == 0) {
						songlist.get(index).UpdateYear(0);
					}
					songlist.get(index).UpdateAlbumName(album.getText());  // Updated the album of the selected song
				}
			}else {
				// User has elected to change the artist name of the selected song
				// First check if the new song if edit will make two items have the same artist name and song name
				for(SongDetail songDetail : songlist){
					// Check if song name is the same
					if(songDetail.GetSongName().equals(edit_song.GetSongName())) {
						if(songDetail.GetArtistName().equals(artist.getText())) {
							// There's a duplicate
							// Alert user
							Alert alert = new Alert(AlertType.ERROR);
							 
							alert.setTitle("Found duplicate artist name and song name!");
							alert.setHeaderText("Cannot have enter existing artist name and song name. Sorry!");
							alert.setContentText("");
							 
							alert.showAndWait();
							return;
						}
					}
				}
				// No duplicates if edit is completed
				if(album.getText().length() == 0 || album.getText().equals(edit_song.GetAlbumName())) {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) != edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));  // Updated the year of the selected song
					}else if(year.getText().length() == 0) {
						songlist.get(index).UpdateYear(0);
					}
				}else {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) != edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));  // Updated the year of the selected song
					}else if(year.getText().length() == 0) {
						songlist.get(index).UpdateYear(0);
					}
					songlist.get(index).UpdateAlbumName(album.getText());  // Updated the album of the selected song
				}
				songlist.get(index).UpdateArtistName(artist.getText());  // Updated the artist name of the selected song
			}
		}else {
			if(artist.getText().equals(edit_song.GetArtistName())) {
				// User has elected to change just the song name
				// Need to check if the new song name and artist name are already in the songlist
				for(SongDetail songDetail : songlist){
					// Check if song name is the same
					if(songDetail.GetSongName().equals(song.getText())) {
						if(songDetail.GetArtistName().equals(edit_song.GetArtistName())) {
							// There's a duplicate
							// Alert the user
							Alert alert = new Alert(AlertType.ERROR);
							 
							alert.setTitle("Found duplicate artist name and song name!");
							alert.setHeaderText("Cannot have enter existing artist name and song name. Sorry!");
							alert.setContentText("");
							 
							alert.showAndWait();
							return;
						}
					}
				}
				if(album.getText().length() == 0 || album.getText().equals(edit_song.GetAlbumName())) {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) != edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));  // Updated the year of the selected song
					}else if(year.getText().length() == 0) {
						songlist.get(index).UpdateYear(0);
					}
				}else {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) != edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));  // Updated the year of the selected song
					}else if(year.getText().length() == 0) {
						songlist.get(index).UpdateYear(0);
					}
					songlist.get(index).UpdateAlbumName(album.getText());  //Updated the album of the selected song
				}
			}else {
				// User has elected to change the song name and the artist name				
				// First check if the new song if edit will make two items have the same artist name and song name
				for(SongDetail songDetail : songlist){
					// Check if song name is the same
					if(songDetail.GetSongName().equals(song.getText())) {
						if(songDetail.GetArtistName().equals(artist.getText())) {
							// There's a duplicate
							// Alert the user
							Alert alert = new Alert(AlertType.ERROR);
							 
							alert.setTitle("Found duplicate artist name and song name!");
							alert.setHeaderText("Cannot have enter existing artist name and song name. Sorry!");
							alert.setContentText("");
							 
							alert.showAndWait();
							return;
						}
					}
				}
				// No duplicates if edit is completed
				if(album.getText().length() == 0 || album.getText().equals(edit_song.GetAlbumName())) {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) != edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));   // Updated the year of the selected song
					}else if(year.getText().length() == 0) {
						songlist.get(index).UpdateYear(0);
					}
				}else {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) != edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));  // Updated the year of the selected song
					}else if(year.getText().length() == 0) {
						songlist.get(index).UpdateYear(0);
					}
					songlist.get(index).UpdateAlbumName(album.getText());  // Updated the album of the selected song
				}
				songlist.get(index).UpdateArtistName(artist.getText());  //Updated the artist name of the selected song
			}
			songlist.get(index).UpdateSongName(song.getText());  //Updated the song name of the selected song
		}
		if(album.getText().length() == 0) {
			songlist.get(index).UpdateAlbumName(null);
		}
		SortSongs(songlist, songlist.size());  // Sorted the song list
		user_display = FXCollections.observableArrayList(); 
		// Remapping the songs in their new order
		for(SongDetail song : songlist) {
			user_display.add(song.GetSongName());
		}
		// Updating the listView
		listView.setItems(user_display);
		//  Selecting the edited song
		listView.getSelectionModel().select(index);
		jsonFile.UpdateJSONFile(edit_song, songlist.get(index));  // Updating JSON file
	}
}
