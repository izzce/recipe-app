package org.izce.recipe.repositories;

import org.izce.recipe.model.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Long> {

}
