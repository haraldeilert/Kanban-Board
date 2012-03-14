package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Board extends Model {

	public String title;
	
	@OneToMany(mappedBy="board", cascade=CascadeType.ALL)
	public List<NoteRow> noteRows;
	
	@ManyToOne
	public User user;
	
	public Board(String title, User admin) {
		this.noteRows = new ArrayList<NoteRow>();
		this.title = title;
		this.user = admin;
	}
	
	public Board addNoteRow(String title, int position) {
	    NoteRow newNoteRow = new NoteRow(this, title, position).save();
	    this.noteRows.add(newNoteRow);
	    this.save();
	    return this;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<NoteRow> getNoteRows() {
		return noteRows;
	}

	public void setNoteRows(List<NoteRow> noteRows) {
		this.noteRows = noteRows;
	}

	public String toString() {
		return title;
	}
}
