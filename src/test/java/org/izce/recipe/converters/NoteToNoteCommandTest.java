package org.izce.recipe.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.izce.recipe.commands.NoteCommand;
import org.izce.recipe.model.Note;
import org.junit.Before;
import org.junit.Test;

public class NoteToNoteCommandTest {

	public static final Long ID_VALUE = 1L;
	public static final String RECIPE_NOTES = "Notes";
	NoteToNoteCommand converter;

	@Before
	public void setUp() throws Exception {
		converter = new NoteToNoteCommand();
	}

	@Test
	public void convert() throws Exception {
		// given
		Note notes = new Note();
		notes.setId(ID_VALUE);
		notes.setNote(RECIPE_NOTES);

		// when
		NoteCommand notesCommand = converter.convert(notes);

		// then
		assertEquals(ID_VALUE, notesCommand.getId());
		assertEquals(RECIPE_NOTES, notesCommand.getNote());
	}

	@Test
	public void testNull() throws Exception {
		assertNull(converter.convert(null));
	}

	@Test
	public void testEmptyObject() throws Exception {
		assertNotNull(converter.convert(new Note()));
	}
}
