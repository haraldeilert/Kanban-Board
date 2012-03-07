package models;

import java.util.ArrayList;
import java.util.List;

import siena.Column;
import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Table;
import siena.core.Many;
import siena.core.Owned;

@Table("boards")
public class Board extends Model {
	
	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	
	@Column("title")
	public String title;
	
	@Owned
	public Many<NoteRow> noteRows;

	public Board(String title) {
		this.title = title;
	}
	
	public Board addNoteRow(String title, int position) {
	    NoteRow newNoteRow = new NoteRow(this, title, position);
	    this.noteRows.asList().add(newNoteRow);
	    newNoteRow.insert();
	    this.insert();
	    return this;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return title;
	}
}
