package controllers;

import java.util.List;

import models.Board;
import models.JsonNote;
import models.Note;
import models.NoteRow;
import models.StatefulModel;
import play.mvc.Controller;
import play.mvc.WebSocketController;

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
		
		NoteRow noteRow1 = new NoteRow(board, "Doing", 0);
		noteRow1.insert();
		
		NoteRow noteRow2 = new NoteRow(board, "Done", 0);
		noteRow2.insert();
		
		Note note = new Note(noteRow, "todo note", "dsf", 0);
		note.insert();
		Note note2 = new Note(noteRow1, "doing note", "dsf", 0);
		note2.insert();
		Note note3 = new Note(noteRow2, "done note", "dsf", 0);
		note3.insert();
	}

	public static void addNewNote(Long id, String title, String text,
			String identify) {
		NoteRow noteRow = NoteRow.all().filter("id", id).get();
		int pos = (findLastPos(id) + 1);

		JsonNote jsonNote = noteRow.addNote(title, text, pos);
		try {
			// TODO: Create some object here instead
			StatefulModel.instance.event.publish("add;" + identify + ";"
					+ id.toString() + ";" + title + ";" + jsonNote.id + ";"
					+ jsonNote.positionInRow);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderJSON(jsonNote);
	}

	public static void deleteNote(Long noteId, String identify) {

		try {
			Note note = Note.all().filter("id", noteId).get();
			note.delete();

			StatefulModel.instance.event.publish("delete;" + identify + ";"
					+ noteId.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void editNote(Long noteId, String newTitle, String identify) {
		JsonNote jsonNote = null;
		System.out.println("*******newTitle: " + newTitle);
		if (newTitle != null && !"".equals(newTitle)) {
			try {
				jsonNote = Note.editNote(noteId, newTitle);
				StatefulModel.instance.event.publish("update;" + identify + ";"
						+ noteId + ";" + newTitle);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			renderJSON(jsonNote);
		}
	}

	public static void updateNotePosition(int noteId, int startUiIndex,
			int stopUiIndex, int fromList, int toList, String identify) {

		NoteRow noteRowTo = NoteRow.all().filter("id", (long) toList).get();

		Note movedNote = Note.all().filter("id", (long) noteId).get();
		movedNote.setNoteRow(noteRowTo);
		movedNote.setPositionInRow(stopUiIndex);
		movedNote.save();

		// Rearrange positions in the From List
		NoteRow noteRowFrom = NoteRow.all().filter("id", (long) fromList).get();
		
		List<Note> notes = Note.all().filter("notes", noteRowFrom).fetch();
		
		for (Note note : notes) {
			if (note.positionInRow > startUiIndex) {
				note.positionInRow = note.positionInRow - 1;
				note.save();
			}
		}

		// Rearrange positions in the From List
		List<Note> notesTo = Note.all().filter("notes", noteRowTo).fetch();
		
		for (Note note : notesTo) {
			if (note.getId() != (long) noteId
					&& note.positionInRow >= stopUiIndex) {
				note.positionInRow = note.positionInRow + 1;
				note.save();
			}
		}

		StatefulModel.instance.event.publish("moved;" + identify + ";"
				+ movedNote.id.toString() + ";" + movedNote.title + ";"
				+ toList + ";" + fromList + ";" + stopUiIndex);
	}

	private static int findLastPos(Long noteRowId) {
		Note note = Note.all().filter("noteRow.id", noteRowId).order("positionInRow-").get();

		if (note == null || note.positionInRow == -1)
			return -1;
		else
			return note.positionInRow;
	}

	private static String createCSSNameStr(List<NoteRow> noterows, String ext) {
		String tmp = "";
		for (NoteRow noteRow : noterows) {
			tmp += "#sortable" + String.valueOf(noteRow.getId().intValue())
					+ ext + ",";

		}
		if(!"".equals(tmp))
			return tmp.substring(0, tmp.length() - 1);
		else
			return tmp;
	}

	public static class WebSocket extends WebSocketController {
		public static void listen() {
			while (inbound.isOpen()) {
				try {
					String event = await(StatefulModel.instance.event
							.nextEvent());
					outbound.send(event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}