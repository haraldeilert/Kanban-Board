package controllers;

import java.util.List;

import models.Board;
import models.JsonNote;
import models.Note;
import models.NoteRow;
import play.mvc.Controller;

public class Application extends Controller {

	public static void index() {
		
		//TODO: Fix bootstrap with siena!!
		List<NoteRow> noterows = NoteRow.all().order("position").fetch();
		
		if(noterows == null || noterows.isEmpty()) {
			populate();
			noterows = NoteRow.all().order("position").fetch();
		}
		
		System.out.println("****test: " + noterows);
		String cssStr = createCSSNameStr(noterows, "");
		String cssStr2 = createCSSNameStr(noterows, " li");
		render(noterows, cssStr, cssStr2);
	}

	private static void populate() {
		Board board = new Board("test");
		board.insert();
		
		NoteRow noteRow = new NoteRow(board, "ToDo", 0);
		noteRow.insert();
		
		NoteRow noteRow1 = new NoteRow(board, "Doing", 1);
		noteRow1.insert();
		
		NoteRow noteRow2 = new NoteRow(board, "Test", 2);
		noteRow2.insert();
		
		NoteRow noteRow3 = new NoteRow(board, "Done", 3);
		noteRow3.insert();
		
		Note note = new Note(noteRow, "todo note", "dsf", 0);
		note.insert();
		Note note2 = new Note(noteRow1, "doing note", "dsf", 0);
		note2.insert();
		Note note3 = new Note(noteRow2, "test note", "dsf", 0);
		note3.insert();
		Note note4 = new Note(noteRow3, "done note", "dsf", 0);
		note4.insert();
	}

	public static void addNewNote(Long id, String title, String text) {
		NoteRow noteRow = NoteRow.all().filter("id", id).get();
		int pos = (findLastPos(noteRow) + 1);

		JsonNote jsonNote = noteRow.addNote(title, text, pos);
		
		renderJSON(jsonNote);
	}

	public static void deleteNote(Long noteId) {

		try {
			Note note = Note.all().filter("id", noteId).get();
			note.delete();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void editNote(Long noteId, String newTitle) {
		JsonNote jsonNote = null;
		System.out.println("*******newTitle: " + newTitle);
		if (newTitle != null && !"".equals(newTitle)) {
			try {
				jsonNote = Note.editNote(noteId, newTitle);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			renderJSON(jsonNote);
		}
	}

	public static void updateNotePosition(int noteId, int startUiIndex,
			int stopUiIndex, int fromList, int toList) {

		System.out.println("****startUiIndex: " + startUiIndex);
		System.out.println("****stopUiIndex: " + stopUiIndex);
		
		
		NoteRow noteRowTo = NoteRow.all().filter("id", (long) toList).get();

		Note movedNote = Note.all().filter("id", (long) noteId).get();
		movedNote.setNoteRow(noteRowTo);
		movedNote.setPositionInRow(stopUiIndex);
		movedNote.save();

		// Rearrange positions in the From List
		NoteRow noteRowFrom = NoteRow.all().filter("id", (long) fromList).get();
		
		List<Note> notes = Note.all().filter("noteRow", noteRowFrom).fetch();
		
		for (Note note : notes) {
			if (note.positionInRow > startUiIndex) {
				note.positionInRow = note.positionInRow - 1;
				note.save();
			}
		}

		// Rearrange positions in the From List
		List<Note> notesTo = Note.all().filter("noteRow", noteRowTo).fetch();
		
		for (Note note : notesTo) {
			if (note.getId() != (long) noteId
					&& note.positionInRow >= stopUiIndex) {
				note.positionInRow = note.positionInRow + 1;
				note.save();
			}
		}	
	}

	private static int findLastPos(NoteRow noteRow) {
		Note note = Note.all().filter("noteRow", noteRow).order("-positionInRow").get();
		
		System.out.println("******note: " + note.getPositionInRow());
		
		if (note == null || note.positionInRow == -1)
			return -1;
		else
			return note.positionInRow;
	}

	private static String createCSSNameStr(List<NoteRow> noterows, String ext) {
		String tmp = "";
		for (NoteRow noteRow : noterows) {
			tmp += "#sortable" + java.lang.String.valueOf(noteRow.getId().intValue())
					+ ext + ",";

		}
		if(!"".equals(tmp))
			return tmp.substring(0, tmp.length() - 1);
		else
			return tmp;
	}
}