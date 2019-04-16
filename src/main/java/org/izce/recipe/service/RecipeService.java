package org.izce.recipe.service;

import org.izce.recipe.commands.RecipeCommand;
import org.izce.recipe.model.Recipe;

public interface RecipeService {
	Iterable<Recipe> getRecipes();
	Long getRecipesCount();
	Recipe findById(Long id);
	RecipeCommand saveRecipeCommand(RecipeCommand command);
}
