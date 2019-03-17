package org.izce.recipe.service;

import org.izce.recipe.model.Recipe;

public interface RecipeService {
	Iterable<Recipe> getRecipeList();
}
