package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class NoteRow extends Model {
	
	public String title;
	public int position;
	
	@ManyToOne
    public Board board;
	
	@OneToMany(mappedBy="noteRow", cascade=CascadeType.ALL)
	@OrderBy("positionInRow ASC")
	public List<Note> notes;
	
	public NoteRow(Board board, String title, int postion) {
		this.notes = new ArrayList<Note>();
		this.board = board;
		this.title = title;
		this.position = postion;
	}
	
	public JsonNote addNote(String title, String text, int position) {
	    Note newNote = new Note(this, title, text, position).save();
	    this.notes.add(newNote);
	    this.save();
	    JsonNote jsonNote = new JsonNote(title);
	    return jsonNote;
	}
	
	public String toString() {
		return title;
	}
}