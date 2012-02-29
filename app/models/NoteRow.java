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
	    JsonNote jsonNote = new JsonNote(newNote.getId().intValue(), title, newNote.getPositionInRow());
	    return jsonNote;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public String toString() {
		return title;
	}
}