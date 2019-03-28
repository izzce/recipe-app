package org.izce.recipe.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.izce.recipe.model.Recipe;
import org.izce.recipe.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RecipeServiceImplTest {
	RecipeServiceImpl service;
	@Mock
	RecipeRepository repository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		service = new RecipeServiceImpl(repository);
	}

	@Test
	public void testGetRecipes() {
		Recipe recipe = new Recipe();
		HashSet<Recipe> recipeData = new HashSet<Recipe>();
		recipeData.add(recipe);
		when(repository.findAll()).thenReturn(recipeData);
		
		Iterable<Recipe> recipes = service.getRecipes();
		int recipeCount = 0;
		for(Recipe r : recipes) {
			recipeCount++;
		}
		
		assertEquals(recipeCount, 1);
		
		verify(repository, times(1)).findAll();
	}

}
