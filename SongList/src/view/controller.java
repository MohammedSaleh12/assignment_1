package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		initListView();
		//getting an exception in thread
		//TODO figure out why there is an exception in thread
		/*listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) ->
			selectItem(mainStage));*/
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
			SongDetail new_song;
			// create new SongDetail and input into both songlist and obsonglist.
			if(album_name.isEmpty() && song_year.isEmpty()) {
				new_song = new SongDetail(song.getText(), artist.getText());
				songlist.add(new_song);
				user_display.add(new_song.GetSongName());
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
				new_song = new SongDetail(song.getText(), artist.getText());
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
			
			AddSongToJSONFile(new_song);

		
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
		int index = listView.getSelectionModel().getSelectedIndex();
		SongDetail edit_song = songlist.get(index);
		if(song.getText().equalsIgnoreCase(edit_song.GetSongName())) {
			if(artist.getText().equalsIgnoreCase(edit_song.GetArtistName())) {
				if(album.getText().equalsIgnoreCase(edit_song.GetAlbumName())) {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) == edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));
					}
				}else {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) == edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));
					}
					songlist.get(index).UpdateAlbumName(album.getText());
				}
			}else {
				//first check if the new song if edit will make two items have the same artist name and song name
				for(SongDetail songDetail : songlist){
					//check if song name is the same
					if(songDetail.GetSongName().equalsIgnoreCase(edit_song.GetSongName())) {
						if(songDetail.GetArtistName().equalsIgnoreCase(artist.getText())) {
							//there's a duplicate
							Alert alert = new Alert(AlertType.ERROR);
							 
							alert.setTitle("Found duplicate artist name and song name!");
							alert.setHeaderText("Cannot have enter existing artist name and song name. Sorry!");
							alert.setContentText("");
							 
							alert.showAndWait();
							return;
						}
					}
				}
				//no duplicates if edit is completed
				if(album.getText() == edit_song.GetAlbumName()) {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) == edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));
					}
				}else {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) == edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));
					}
					songlist.get(index).UpdateAlbumName(album.getText());
				}
				songlist.get(index).UpdateArtistName(artist.getText());
			}
		}else {
			if(artist.getText().equalsIgnoreCase(edit_song.GetArtistName())) {
				for(SongDetail songDetail : songlist){
					//check if song name is the same
					if(songDetail.GetSongName().equalsIgnoreCase(song.getText())) {
						if(songDetail.GetArtistName().equalsIgnoreCase(edit_song.GetArtistName())) {
							//there's a duplicate
							Alert alert = new Alert(AlertType.ERROR);
							 
							alert.setTitle("Found duplicate artist name and song name!");
							alert.setHeaderText("Cannot have enter existing artist name and song name. Sorry!");
							alert.setContentText("");
							 
							alert.showAndWait();
							return;
						}
					}
				}
				if(album.getText().equalsIgnoreCase(edit_song.GetAlbumName())) {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) == edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));
					}
				}else {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) == edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));
					}
					songlist.get(index).UpdateAlbumName(album.getText());
				}
			}else {
				//first check if the new song if edit will make two items have the same artist name and song name
				for(SongDetail songDetail : songlist){
					//check if song name is the same
					if(songDetail.GetSongName().equalsIgnoreCase(song.getText())) {
						if(songDetail.GetArtistName().equalsIgnoreCase(artist.getText())) {
							//there's a duplicate
							Alert alert = new Alert(AlertType.ERROR);
							 
							alert.setTitle("Found duplicate artist name and song name!");
							alert.setHeaderText("Cannot have enter existing artist name and song name. Sorry!");
							alert.setContentText("");
							 
							alert.showAndWait();
							return;
						}
					}
				}
				//no duplicates if edit is completed
				if(album.getText() == edit_song.GetAlbumName()) {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) == edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));
					}
				}else {
					if(year.getText().length() != 0 && Integer.parseInt(year.getText()) == edit_song.GetYear()) {
						songlist.get(index).UpdateYear(Integer.parseInt(year.getText()));
					}
					songlist.get(index).UpdateAlbumName(album.getText());
				}
				songlist.get(index).UpdateArtistName(artist.getText());
			}
			songlist.get(index).UpdateSongName(song.getText());
			user_display.set(index, song.getText());
		}
		listView.setItems(user_display);
		listView.getSelectionModel().select(index);
		UpdateJSONFile(edit_song, songlist.get(index));
	}
	
	
	private void UpdateJSONFile(SongDetail editSong, SongDetail newSong) {
		JSONParser parser = new JSONParser();
		JSONArray songArr;
		try {
			//converting JSON file to a JSONArray object
			File JSONFile = new File("SongLib.json");
			songArr = (JSONArray) parser.parse(new FileReader(JSONFile));
			JSONObject song = new JSONObject();
			for (Object o : songArr) {
				song = (JSONObject) o;
				if(editSong.GetSongName().equalsIgnoreCase((String)song.get("song_name")) &&
						editSong.GetArtistName().equalsIgnoreCase((String) song.get("artist_name"))){
					break;
				}
			}
			
			
			FileWriter filewriter = new FileWriter(JSONFile);
			songArr.remove(song);
			JSONObject newJSONSong = new JSONObject();
			newJSONSong.put("song_name", newSong.GetSongName());
			newJSONSong.put("artist_name", newSong.GetArtistName());
			newJSONSong.put("album_name", newSong.GetAlbumName());
			newJSONSong.put("year", Integer.toString(newSong.GetYear()));
			
			songArr.add(newJSONSong);
			
			filewriter.write(songArr.toString());
			filewriter.flush();
			filewriter.close();
			return;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void AddSongToJSONFile(SongDetail newSong) {
		JSONParser parser = new JSONParser();
		JSONArray songArr;
		try {
			//converting JSON file to a JSONArray object
			File JSONFile = new File("SongLib.json");
			if (JSONFile.length() == 0) {
				//file is empty
				songArr = new JSONArray();
				
				FileWriter filewriter = new FileWriter(JSONFile);
				
				//creating a new JSONObject that will contain the new song details
				JSONObject newJSONSong = new JSONObject();
				newJSONSong.put("song_name", newSong.GetSongName());
				newJSONSong.put("artist_name", newSong.GetArtistName());
				newJSONSong.put("album_name", newSong.GetAlbumName());
				newJSONSong.put("year", Integer.toString(newSong.GetYear()));
				
				songArr.add(newJSONSong);
				filewriter.write(songArr.toString());
				filewriter.flush();
				filewriter.close();
				return;
			}
			
			//file is not empty
			songArr = (JSONArray) parser.parse(new FileReader(JSONFile));
			
			//opening the file
			FileWriter filewriter = new FileWriter(JSONFile);
			
			//creating a new JSONObject that will contain the new song details
			JSONObject newJSONSong = new JSONObject();
			newJSONSong.put("song_name", newSong.GetSongName());
			newJSONSong.put("artist_name", newSong.GetArtistName());
			newJSONSong.put("album_name", newSong.GetAlbumName());
			newJSONSong.put("year", Integer.toString(newSong.GetYear()));
			
			songArr.add(newJSONSong);
			filewriter.write(songArr.toJSONString());
			filewriter.flush();
			filewriter.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initListView() {
		JSONParser parser = new JSONParser();
		JSONArray songArr;
		try {
			//converting JSON file to a JSONArray object
			File JSONFile = new File("SongLib.json");
			if (JSONFile.length() == 0) {
				//file is empty no need to populate listview
				return;
			}
			
			//file is not empty
			songArr = (JSONArray) parser.parse(new FileReader(JSONFile));
			for (Object o : songArr) {
				JSONObject song = (JSONObject) o;
				String name = (String) song.get("song_name");
				String artist = (String) song.get("artist_name");
				String album = (String) song.get("album_name");
				int year = Integer.parseInt((String) song.get("year"));
				
				user_display.add(name);
				if(album == null) {
					if(year == 0) {
						songlist.add(new SongDetail(name, artist));
					}else {
						songlist.add(new SongDetail(name, artist, year));
					}
				}else {
					if(year == 0) {
						songlist.add(new SongDetail(name, artist, album));
					}else {
						songlist.add(new SongDetail(name, artist, album, year));
					}
				}
			}
			songlist = SortSongs(songlist, songlist.size());
			// Update user_display for alphabetical order
			user_display = FXCollections.observableArrayList();
			for(SongDetail songer : songlist) {
				user_display.add(songer.GetSongName());
			}			
			listView.setItems(user_display);
			listView.getSelectionModel().select(0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}


