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
	    new Board(MY_TEST_BOARD).save();
	    
	    // Retrieve the board 
	    Board board = Board.find("byTitle", MY_TEST_BOARD).first();
	    
	    // Test 
	    assertNotNull(board);
	    assertEquals(MY_TEST_BOARD, board.title);
	}
	
	
	@Test
	public void createAndRetrieveNoteRow() {
	    // Create a new board and save it
	    Board board = new Board(MY_TEST_BOARD).save();
	    
	    NoteRow test = new NoteRow(board, MY_TEST_NOTE_ROW, 1);
	      
	    NoteRow noteRow = NoteRow.findById(test.getEntityId());
	    
	    System.out.println("dsffdsdf444: " + noteRow);
	    
	    // Test 
	    assertNotNull(noteRow);
	    assertEquals(MY_TEST_NOTE_ROW, noteRow.title);
	}

}
