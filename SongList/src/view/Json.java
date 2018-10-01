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

import javafx.collections.ObservableList;

public class Json {
	private String fileName;
	
	public Json(String fileName) {
		this.fileName = fileName;
	}
	
	public void initListView(ArrayList<SongDetail> songlist, ObservableList<String> user_display) {
		JSONParser parser = new JSONParser();
		JSONArray songArr;
		try {
			//converting JSON file to a JSONArray object
			File JSONFile = new File(fileName);
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
	
	public void AddSongToJSONFile(SongDetail newSong) {
		JSONParser parser = new JSONParser();
		JSONArray songArr;
		try {
			//converting JSON file to a JSONArray object
			File JSONFile = new File(fileName);
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
	
	public void UpdateJSONFile(SongDetail editSong, SongDetail newSong) {
		JSONParser parser = new JSONParser();
		JSONArray songArr;
		try {
			//converting JSON file to a JSONArray object
			File JSONFile = new File(fileName);
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
}
