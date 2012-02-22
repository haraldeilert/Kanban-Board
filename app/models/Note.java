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
	
	public Note(String title, String text, int postionInRow) {
		this.title = title;
		this.text = text;
		this.positionInRow = postionInRow;
	}
}
