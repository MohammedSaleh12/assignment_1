package view;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
*/
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

	@FXML ListView<String> listView;                
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
		/*JSONParser parser = new JSONParser();
		JSONArray arr;
		try {
			arr = (JSONArray) parser.parse(new FileReader("c:\\filesystem.json"));
			for (Object o : arr) {
				JSONObject song = (JSONObject) o;
				String name = (String) song.get("name");
				System.out.println(name);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		//getting an exception in thread
		//TODO figure out why there is an exception in thread
		/*listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
			if (oldVal.intValue() != -1) {
				selectItem(mainStage);
			}
		});*/
	}
	
	private void selectItem(Stage mainStage) {
		int index = listView.getSelectionModel().getSelectedIndex();
		if(index < 0) {
			return;
		}
		SongDetail selectedSong = songlist.get(index);
		song.setText(selectedSong.GetSongName());
		artist.setText(selectedSong.GetArtistName());
		album.setText(selectedSong.GetAlbumName());
		if(selectedSong.GetYear() != 0){
			year.setText(""+selectedSong.GetYear());
		}
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
			// get the index where it was newly inserted
			int count = 0;
			for(SongDetail songdetail : songlist) {
				if(song.getText().equals(songdetail.GetSongName()) && artist.getText().equals(songdetail.GetArtistName())) {
					break;
				}
				count++;
			}
			
			listView.setItems(user_display);
			listView.getSelectionModel().select(count);

		
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


