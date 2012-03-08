package models;

import java.util.ArrayList;

import siena.Column;
import siena.Filter;
import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.Table;

@Table("boards")
public class Board extends Model {

	@Id(Generator.AUTO_INCREMENT)
	public Long id;

	@Column("title")
	public String title;
	
	@Filter("board")
    public Query<NoteRow> noteRow;
	
	public Board(String title) {
		this.title = title;
	}

	public void addNoteRow(String title, int position) {
		NoteRow newNoteRow = new NoteRow(this, title, position);
		newNoteRow.insert();
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
