package org.izce.recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.izce.recipe.commands.NoteCommand;
import org.izce.recipe.converters.NoteCommandToNote;
import org.izce.recipe.converters.NoteToNoteCommand;
import org.izce.recipe.model.Note;
import org.izce.recipe.repositories.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class NoteServiceImplTest {
	NoteService noteService;
	Note note1;
	@Mock NoteRepository noteRepository; 
	NoteCommandToNote nc2n;
	NoteToNoteCommand n2nc;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		nc2n = new NoteCommandToNote();
		n2nc = new NoteToNoteCommand();
		noteService = new NoteServiceImpl(noteRepository, nc2n, n2nc);
		note1 = new Note("note1");
	}

	@Test
	void testFindById() {
		when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note1));
		assertEquals(note1.getNote(), noteService.findById(1L).getNote());
		
		verify(noteRepository, times(1)).findById(anyLong());
	}

	@Test
	void testFindNoteCommandById() {
		when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note1));
		assertEquals(n2nc.convert(note1).getNote(), noteService.findNoteCommandById(anyLong()).getNote());
		
		verify(noteRepository, times(1)).findById(anyLong());
	}

	@Test
	void testSaveNoteCommand() {
		NoteCommand nc = n2nc.convert(note1);
		when(noteRepository.save(any(Note.class))).thenReturn(note1);
		NoteCommand returnedNc = noteService.saveNoteCommand(nc);
		
		assertEquals(nc.getNote(), returnedNc.getNote());
		verify(noteRepository, times(1)).save(any(Note.class));
	}

	@Test
	void testDelete() {
		noteService.delete(1L);
		verify(noteRepository, times(1)).deleteById(1L);
	}

}
