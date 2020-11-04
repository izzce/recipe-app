package org.izce.recipe.service;

import org.izce.recipe.commands.NoteCommand;
import org.izce.recipe.model.Note;

public interface NoteService {
	Note findById(Long id);
	NoteCommand findNoteCommandById(Long id);
	NoteCommand saveNoteCommand(NoteCommand command);
	void delete(Long noteId);
}
