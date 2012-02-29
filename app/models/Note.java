package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Note extends Model {

	public String title;
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
