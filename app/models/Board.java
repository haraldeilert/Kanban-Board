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

	public Board(String title) {
		this.noteRows = new ArrayList<NoteRow>();
		this.title = title;
	}
	
	public Board addNoteRow(String title, int position) {
	    NoteRow newNoteRow = new NoteRow(this, title, position).save();
	    this.noteRows.add(newNoteRow);
	    this.save();
	    return this;
	}
}
