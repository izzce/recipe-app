package org.izce.recipe.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Set;

import org.izce.recipe.model.Recipe;
import org.izce.recipe.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

public class IndexControllerTest {
	@Mock
	RecipeService recipeService;
	@Mock
	Model model;
	IndexController indexController;
	MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		indexController = new IndexController(recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
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
	
	@Test
	public void testMockMVC() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index2"));
	}
	
	@Test
	public void testMockMVC2() throws Exception {
		mockMvc.perform(get("/index")).andExpect(status().isOk()).andExpect(view().name("index2"));
	}

	@Test
	public void testMockMVC_HTTP404() throws Exception {
		mockMvc.perform(get("/index_404")).andExpect(status().isNotFound());
	}
}
