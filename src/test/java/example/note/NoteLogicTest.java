package example.note;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteLogicTest {

    private NoteLogic noteLogic;

    @Before
    public void setUp() {
        noteLogic = new NoteLogic();
    }

    /**
     * При команде добавления должна добавляться заметка
     */
    @Test
    public void whenAddCommand_thenShouldAddNote() {
        String response = noteLogic.handleMessage("/add Task");
        assertEquals("Note added!", response);
        assertEquals("Your notes:\n"
                + "1) Task", noteLogic.handleMessage("/notes"));
    }

    /**
     * При команде редактирования заметка должна измениться
     */
    @Test
    public void whenEditCommand_thenShouldEditNote() {
        noteLogic.handleMessage("/add Task");
        assertEquals("Note edited!", noteLogic.handleMessage("/edit 1 New task"));
        assertEquals("Your notes:\n"
                + "1) New task", noteLogic.handleMessage("/notes"));
    }

    /**
     * При команде удаления заметка должна удалиться
     */
    @Test
    public void givenDeleteCommand_thenShouldDeleteNote() {
        noteLogic.handleMessage("/add Task");
        noteLogic.handleMessage("/add Another task");
        assertEquals("Note deleted!", noteLogic.handleMessage("/del 1"));
        assertEquals("Your notes:\n"
                + "1) Another task", noteLogic.handleMessage("/notes"));
    }
}