package org.izce.recipe.service;

import java.util.List;

import org.izce.recipe.commands.CategoryCommand;
import org.izce.recipe.commands.RecipeCommand;
import org.izce.recipe.commands.UnitOfMeasureCommand;
import org.izce.recipe.model.Recipe;

public interface RecipeService {
	Iterable<Recipe> getRecipes();
	Long getRecipesCount();
	Recipe findById(Long id);
	RecipeCommand findRecipeCommandById(Long id);
	RecipeCommand saveRecipeCommand(RecipeCommand command);
	CategoryCommand findCategoryByDescription(String description);
	UnitOfMeasureCommand findUom(String uom);
	UnitOfMeasureCommand findUom(Long uomId);
	List<UnitOfMeasureCommand> findAllUoms();
}
