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
		List<NoteRow> noterows = NoteRow.find("order by position asc").fetch();
		String cssStr = createCSSNameStr(noterows, "");
		String cssStr2 = createCSSNameStr(noterows, " li");
		render(noterows, cssStr, cssStr2);
	}

	public static void addNewNote(Long id, String title, String text) {
		NoteRow noteRow = NoteRow.findById(id);
		JsonNote jsonNote = noteRow.addNote(title, text, (findLastPos(id) + 1));
		StatefulModel.instance.event.publish(id.toString() + ";" + title);
		renderJSON(jsonNote);
	}

	public static void updateNotePosition(int noteId, int startUiIndex,
			int stopUiIndex, int fromList, int toList) {
		
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
			if (note.getId() != (long) noteId && note.positionInRow >= stopUiIndex) {
				note.positionInRow = note.positionInRow + 1;
				note.save();
			}
		}
	}

	private static int findLastPos(Long noteRowId) {
		Note note = Note.find("noteRow.id = ? order by positionInRow desc",
				noteRowId).first();
		if(note == null || note.positionInRow == 0)
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
		return tmp.substring(0, tmp.length() - 1);
	}
	
	public static class WebSocket extends WebSocketController {
		      public static void listen() {
		         while(inbound.isOpen()) {
		            String event = await(StatefulModel.instance.event.nextEvent());
		            outbound.send(event);
		         }
		      }
		   }
}