package org.izce.recipe.service;

import java.util.List;

import org.izce.recipe.commands.IngredientCommand;
import org.izce.recipe.commands.UnitOfMeasureCommand;
import org.izce.recipe.model.Ingredient;

public interface IngredientService {
	Ingredient findById(Long id);
	IngredientCommand findIngredientCommandById(Long id);
	IngredientCommand saveIngredientCommand(IngredientCommand command);
	UnitOfMeasureCommand findUom(String uom);
	UnitOfMeasureCommand findUom(Long uomId);
	List<UnitOfMeasureCommand> findAllUoms();
	void delete(Long ingredientId);
}
