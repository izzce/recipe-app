package org.izce.recipe.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.izce.recipe.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
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
		String viewName = indexController.getIndexPage(model);
		
		assertEquals("index2", viewName);
		
		verify(model, times(1)).addAttribute(ArgumentMatchers.any(), ArgumentMatchers.any());
	}

}
