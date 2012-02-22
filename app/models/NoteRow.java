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
	
	@OneToMany(mappedBy="note", cascade=CascadeType.ALL)
	public List<Note> notes;
	
	public NoteRow(Board board, String title, int postion) {
		this.notes = new ArrayList<Note>();
		this.board = board;
		this.title = title;
		this.position = postion;
	}
	
	public NoteRow addNote(String title, String text, int position) {
	    Note newNote = new Note(this, title, text, position).save();
	    this.notes.add(newNote);
	    this.save();
	    return this;
	}
}
