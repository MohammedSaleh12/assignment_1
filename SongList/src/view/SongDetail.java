package view;

public class SongDetail {
	private String songName;
	private String artistName;
	private String album;
	private int year;
	
	
	public SongDetail(String songName, String artistName) {
		this.songName = songName;
		this.artistName = artistName;
	}
	
	public SongDetail(String songName, String artistName, String album) {
		this.songName = songName;
		this.artistName = artistName;
		this.album = album;
	}
	
	public SongDetail(String songName, String artistName, int year) {
		this.songName = songName;
		this.artistName = artistName;
		this.year = year;
	}
	
	public SongDetail(String songName, String artistName, String album, int year) {
		this.songName = songName;
		this.artistName = artistName;
		this.album = album;
		this.year = year;
	}
	
	public String GetSongName() {
		return this.songName;
	}
	
	public String GetArtistName() {
		return this.artistName;
	}
	
	public String GetAlbumName() {
		return this.album;
	}
	
	public int GetYear() {
		return this.year;
	}
	
}
