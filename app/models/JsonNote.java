package models;

public class JsonNote {
    
	public int id;
	public String title;
	public int positionInRow;
	
	
	public JsonNote(int id, String title, int positionInRow) {
		this.id = id;
		this.title = title;
		this.positionInRow = positionInRow;
	}
}
