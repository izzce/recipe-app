package org.izce.recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.izce.recipe.converters.CategoryToCategoryCommand;
import org.izce.recipe.converters.RecipeCommandToRecipe;
import org.izce.recipe.converters.RecipeToRecipeCommand;
import org.izce.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.recipe.model.Recipe;
import org.izce.recipe.repositories.CategoryRepository;
import org.izce.recipe.repositories.DirectionRepository;
import org.izce.recipe.repositories.IngredientRepository;
import org.izce.recipe.repositories.NoteRepository;
import org.izce.recipe.repositories.RecipeRepository;
import org.izce.recipe.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RecipeServiceImplTest {
	RecipeServiceImpl service;
	@Mock RecipeRepository repository;
	@Mock CategoryRepository catRepo;
	@Mock IngredientRepository ingredientRepo;
	@Mock UnitOfMeasureRepository uomRepo;
	@Mock NoteRepository noteRepo;
	@Mock DirectionRepository directionRepo;
    @Mock RecipeToRecipeCommand recipeToRecipeCommand;
    @Mock RecipeCommandToRecipe recipeCommandToRecipe;
    @Mock UnitOfMeasureToUnitOfMeasureCommand uom2uomc;
    @Mock CategoryToCategoryCommand cTocc;
    
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		service = new RecipeServiceImpl(repository, catRepo, ingredientRepo, uomRepo, noteRepo, directionRepo, recipeCommandToRecipe, recipeToRecipeCommand, uom2uomc, cTocc);
	}

	@Test
	public void testGetRecipes() {
		Recipe recipe = new Recipe();
		HashSet<Recipe> recipeData = new HashSet<Recipe>();
		recipeData.add(recipe);
		when(repository.findAll()).thenReturn(recipeData);
		
		int recipeCount = 0;
		for (Recipe r : service.getRecipes()) {
			r.getId();
			recipeCount++;
		}
		
		assertEquals(recipeCount, 1);
		
		verify(repository, times(1)).findAll();
	}

}
