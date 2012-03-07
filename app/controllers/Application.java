package controllers;

import java.util.List;

import models.JsonNote;
import models.Note;
import models.NoteRow;
import models.StatefulModel;
import play.mvc.Controller;
import play.mvc.WebSocketController;

public class Application extends Controller {

	public static void index() {
		List<NoteRow> noterows = NoteRow.all().fetch();//TODO:Order by postion!!!!
		String cssStr = createCSSNameStr(noterows, "");
		String cssStr2 = createCSSNameStr(noterows, " li");
		render(noterows, cssStr, cssStr2);
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
		List<Note> notes = noteRowFrom.notes.asList();
		for (Note note : notes) {
			if (note.positionInRow > startUiIndex) {
				note.positionInRow = note.positionInRow - 1;
				note.save();
			}
		}

		// Rearrange positions in the From List
		List<Note> notesTo = noteRowTo.notes.asList();
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