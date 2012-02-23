package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	public static void index() {
		List<NoteRow> noterows = NoteRow.find("order by id desc").fetch();
		String cssStr = createCSSNameStr(noterows, "");
		String cssStr2 = createCSSNameStr(noterows, " li");
		render(noterows, cssStr, cssStr2);
	}

	
	public static void addNewNote(String title, String noterowtitle) {
		
		NoteRow noteRow = NoteRow.find("byTitle", noterowtitle).first();
		Note note = Note.find("noteRow.title = ? order by positionInRow desc", noteRow.title).first();
		
		noteRow.addNote(title, title, note.positionInRow + 1);
	}
	
	private static String createCSSNameStr(List<NoteRow> noterows, String ext) {
		String tmp = "";
		for (NoteRow noteRow : noterows) {
			tmp += "#sortable" + String.valueOf(noteRow.getId().intValue()) + ext + ",";	
		}
		return tmp.substring(0, tmp.length() - 1);
	}

}