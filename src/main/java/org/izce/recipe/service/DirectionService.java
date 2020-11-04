package org.izce.recipe.service;

import org.izce.recipe.commands.DirectionCommand;
import org.izce.recipe.model.Direction;

public interface DirectionService {
	Direction findById(Long id);
	DirectionCommand findDirectionCommandById(Long id);
	DirectionCommand saveDirectionCommand(DirectionCommand command);
	void delete(Long directionId);
}
