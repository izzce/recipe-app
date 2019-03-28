package org.izce.recipe.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.izce.recipe.model.Recipe;
import org.izce.recipe.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class IndexControllerTest {
	@Mock
	RecipeService recipeService;
	@Mock
	Model model;
	IndexController indexController;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		indexController = new IndexController(recipeService);
	}

	@Test
	public void testGetIndexPage() {
		
		Set<Recipe> recipes = new HashSet<Recipe>();
		Recipe recipe1 = new Recipe();
		recipe1.setId(1L);
		recipes.add(recipe1);
		Recipe recipe2 = new Recipe();
		recipe2.setId(2L);
		recipes.add(recipe2);
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Iterable<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
		
		when(recipeService.getRecipes()).thenReturn(recipes);
		
		String viewName = indexController.getIndexPage(model);
		assertEquals("index2", viewName);
		
		verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
		
		Iterable<Recipe> recipesFromController = argumentCaptor.getValue();
		int recipeCount = 0;
		for(Recipe r : recipesFromController) {
			recipeCount++;
		}
		
		assertEquals(2, recipeCount);
		
	}

}
