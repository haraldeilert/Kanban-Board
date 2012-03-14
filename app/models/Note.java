package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.data.validation.MaxSize;
import play.db.jpa.Model;

@Entity
public class Note extends Model {

	public String title;
	@Lob
    @MaxSize(10000)
    public String text;
	public int positionInRow;

	@ManyToOne
	public NoteRow noteRow;

	public Note(NoteRow noteRow, String title, String text, int postionInRow) {
		this.noteRow = noteRow;
		this.title = title;
		this.text = text;
		this.positionInRow = postionInRow;
	}
	
	public static JsonNote editNote(Long id, String newTitle) {
	    Note newNote = Note.findById(id);
	    newNote.setTitle(newTitle);
	    newNote.save();
	    
	    JsonNote jsonNote = new JsonNote(newNote.getId().intValue(), newTitle, newNote.getPositionInRow());
	    return jsonNote;
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
