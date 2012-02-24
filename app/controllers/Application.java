package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	public static void index() {
		List<NoteRow> noterows = NoteRow.find("order by position asc").fetch();
		String cssStr = createCSSNameStr(noterows, "");
		String cssStr2 = createCSSNameStr(noterows, " li");
		render(noterows, cssStr, cssStr2);
	}

	public static void addNewNote(Long id, String title) {
		
		NoteRow noteRow = NoteRow.findById(id);

		JsonNote jsonNote = noteRow.addNote(title, "", findLastPos(id) + 1);
		renderJSON(jsonNote);
	}

	private static int findLastPos(Long noteRowId) {
		Note note = Note.find("noteRow.id = ? order by positionInRow desc",
				noteRowId).first();
		return note.positionInRow;
	}

	private static String createCSSNameStr(List<NoteRow> noterows, String ext) {
		String tmp = "";
		for (NoteRow noteRow : noterows) {
			tmp += "#sortable" + String.valueOf(noteRow.getId().intValue())
					+ ext + ",";
			
		}
		System.out.println("******test: " + tmp);
		
		
		return tmp.substring(0, tmp.length() - 1);
	}

}