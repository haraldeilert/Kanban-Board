package models;

import java.util.ArrayList;
import java.util.List;

import siena.Column;
import siena.Filter;
import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.Table;

@Table("noterows")
public class NoteRow extends Model {
	
	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	@Column("title")
	public String title;
	@Column("position")
	public int position;
	
	@Column("board")
    public Board board;
	
	@Filter("notes")
	public Query<Note> notes;
	
	public NoteRow(Board board, String title, int postion) {
		this.board = board;
		this.title = title;
		this.position = postion;
	}
	
	public static Query<NoteRow> all() {
        return Model.all(NoteRow.class);
    }
	
	public JsonNote addNote(String title, String text, int position) {
	    Note newNote = new Note(this, title, text, position);
	    newNote.insert();
	    JsonNote jsonNote = new JsonNote(newNote.getId().intValue(), title, newNote.getPositionInRow());
	    return jsonNote;
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

	public String toString() {
		return title;
	}
}