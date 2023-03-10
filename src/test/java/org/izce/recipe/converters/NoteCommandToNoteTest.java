package org.izce.recipe.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.izce.recipe.commands.NoteCommand;
import org.izce.recipe.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoteCommandToNoteTest {

    public static final Long ID_VALUE = 1L;
    public static final String RECIPE_NOTE = "Notes";
    NoteCommandToNote converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new NoteCommandToNote();

    }

    @Test
    public void testNullParameter() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new NoteCommand()));
    }

    @Test
    public void convert() throws Exception {
        //given
        NoteCommand notesCommand = new NoteCommand();
        notesCommand.setId(ID_VALUE);
        notesCommand.setNote(RECIPE_NOTE);

        //when
        Note note = converter.convert(notesCommand);

        //then
        assertNotNull(note);
        assertEquals(ID_VALUE, note.getId());
        assertEquals(RECIPE_NOTE, note.getNote());
    }

}