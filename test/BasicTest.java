import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

	private static final String MY_TEST_NOTE_ROW = "MyTestNoteRow";
	private static final String MY_TEST_BOARD = "MyTestBoard";

	@Test
	public void createAndRetrieveBoard() {
	    // Create a new board and save it
	    //new Board(MY_TEST_BOARD).save();
	    
	    // Retrieve the board 
	    Board board = Board.find("byTitle", MY_TEST_BOARD).first();
	    
	    // Test 
	    assertNotNull(board);
	    assertEquals(MY_TEST_BOARD, board.title);
	}
	
	
	@Test
	public void createAndRetrieveNoteRow() {
	    // Create a new board and save it
	    //Board board = new Board(MY_TEST_BOARD).save();
	    
	    //board.addNoteRow(MY_TEST_NOTE_ROW, 1);
	    
	    NoteRow noteRow = NoteRow.find("byTitle", MY_TEST_NOTE_ROW).first();
	    
	    // Test 
	    assertNotNull(noteRow);
	    assertEquals(MY_TEST_NOTE_ROW, noteRow.title);
	}
	
	
	@Test
	public void createAndRetrieveNote() {
	    // Create a new board and save it
	    //Board board = new Board(MY_TEST_BOARD).save();
	    
	    //board.addNoteRow(MY_TEST_NOTE_ROW, 1);
	    
	    NoteRow noteRow = NoteRow.find("byTitle", MY_TEST_NOTE_ROW).first();
	    
	    noteRow.addNote("Min först lapp", "hej hej här kommer lite text bla bla bla", 1);
	    noteRow.addNote("Min andra lapp", "hej hej här kommer lite mer text bla bla bla", 2);
	   
	    // Retrieve all comments
        List<Note> notes = Note.find("byNoteRow", noteRow).fetch();

        // Tests
        assertEquals(2, notes.size());

        Note firstNote = notes.get(0);
        assertNotNull(firstNote);
        assertEquals("Min först lapp", firstNote.title);
        assertEquals("hej hej här kommer lite text bla bla bla", firstNote.text);
        
        Note secondNote = notes.get(1);
        assertNotNull(secondNote);
        assertEquals("Min andra lapp", secondNote.title);
        assertEquals("hej hej här kommer lite mer text bla bla bla", secondNote.text);
	}
}