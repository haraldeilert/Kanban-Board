package models;

import java.util.ArrayList;
import java.util.List;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.Table;
import siena.core.Many;
import siena.core.Owned;

@Table("noterows")
public class NoteRow extends Model {
	
	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	public String title;
	public int position;
	
    public Board board;
	
    @Owned
	public Many<Note> notes;
	
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
	    this.notes.asList().add(newNote);
	    this.insert();
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