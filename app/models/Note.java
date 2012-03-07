package models;

import java.util.List;

import siena.Column;
import siena.Filter;
import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.Table;

@Table("notes")
public class Note extends Model {

	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	
	@Column("title")
	public String title;
	@Column("text")
	public String text;
	@Column("position_in_row")
	public int positionInRow;

	@Filter("note_rows")
	public NoteRow noteRow;
	
	public Note(NoteRow noteRow, String title, String text, int postionInRow) {
		this.noteRow = noteRow;
		this.title = title;
		this.text = text;
		this.positionInRow = postionInRow;
	}
	
	public static Query<Note> all() {
        return Model.all(Note.class);
    }
	
	public static List<Note> findByNoteRow(NoteRow noteRow) {
		return all().filter("noteRow", noteRow).fetch();
	}
	
	public static JsonNote editNote(Long id, String newTitle) {
	    Note newNote = all().filter("id", id).get();
	    newNote.setTitle(newTitle);
	    newNote.save();
	    
	    JsonNote jsonNote = new JsonNote(newNote.getId().intValue(), newTitle, newNote.getPositionInRow());
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getPositionInRow() {
		return positionInRow;
	}

	public void setPositionInRow(int positionInRow) {
		this.positionInRow = positionInRow;
	}

	public NoteRow getNoteRow() {
		return noteRow;
	}

	public void setNoteRow(NoteRow noteRow) {
		this.noteRow = noteRow;
	}
	
	public String toString() {
		return title;
	}
}
