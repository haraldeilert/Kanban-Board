package controllers;

import java.util.List;

import models.JsonNote;
import models.Note;
import models.NoteRow;
import play.modules.pusher.Pusher;
import play.mvc.Controller;

public class Application extends Controller {
	private static final String KANBANEVENT = "kanbanevent";
	private static final String KANBANCHANNEL = "kanbanchannel";
	private static Pusher pusher = new Pusher();
	
	public static void index() {
		List<NoteRow> noterows = NoteRow.find("order by position asc").fetch();
		String cssStr = createCSSNameStr(noterows, "");
		String cssStr2 = createCSSNameStr(noterows, " li");
		render(noterows, cssStr, cssStr2);
	}

	public static void addNewNote(Long id, String title, String text,
			String identify) {
		NoteRow noteRow = NoteRow.findById(id);
		int pos = (findLastPos(id) + 1);

		JsonNote jsonNote = noteRow.addNote(title, text, pos);
		try {
			// TODO: Create some Object here instead
			pusher.trigger(KANBANCHANNEL, KANBANEVENT, "add;" + identify + ";" 
					+ id.toString() + ";" 
					+ title + ";" 
					+ jsonNote.id + ";"
					+ jsonNote.positionInRow);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderJSON(jsonNote);
	}

	public static void deleteNote(Long noteId, String identify) {

		try {
			Note note = Note.findById(noteId);
			note.delete();
			pusher.trigger(KANBANCHANNEL, KANBANEVENT, "delete;" + identify + ";"
					+ noteId.toString());
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void editNote(Long noteId, String newTitle, String identify) {
		JsonNote jsonNote = null;
		if (newTitle != null && !"".equals(newTitle)) {
			try {
				jsonNote = Note.editNote(noteId, newTitle);
				
				pusher.trigger(KANBANCHANNEL, KANBANEVENT,"update;" + identify + ";"
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

		NoteRow noteRowTo = NoteRow.findById((long) toList);

		Note movedNote = Note.findById((long) noteId);
		movedNote.setNoteRow(noteRowTo);
		movedNote.setPositionInRow(stopUiIndex);
		movedNote.save();

		// Rearrange positions in the From List
		NoteRow noteRowFrom = NoteRow.findById((long) fromList);
		List<Note> notes = noteRowFrom.notes;
		for (Note note : notes) {
			if (note.positionInRow > startUiIndex) {
				note.positionInRow = note.positionInRow - 1;
				note.save();
			}
		}

		// Rearrange positions in the From List
		List<Note> notesTo = noteRowTo.notes;
		for (Note note : notesTo) {
			if (note.getId() != (long) noteId
					&& note.positionInRow >= stopUiIndex) {
				note.positionInRow = note.positionInRow + 1;
				note.save();
			}
		}

		pusher.trigger(KANBANCHANNEL, KANBANEVENT, "moved;" + identify + ";"
				+ movedNote.id.toString() + ";" + movedNote.title + ";"
				+ toList + ";" + fromList + ";" + stopUiIndex);
	}

	private static int findLastPos(Long noteRowId) {
		Note note = Note.find("noteRow.id = ? order by positionInRow desc",
				noteRowId).first();

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
		return tmp.substring(0, tmp.length() - 1);
	}
}