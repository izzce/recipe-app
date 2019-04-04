package org.izce.recipe.service;

import java.util.Optional;

import org.izce.recipe.model.Recipe;
import org.izce.recipe.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
	private final RecipeRepository recipeRepository;

	@Autowired
	public RecipeServiceImpl(RecipeRepository recipeRepository) {
		log.debug("Initializing RecipeServiceImpl...");
		this.recipeRepository = recipeRepository;
	}

	@Override
	public Iterable<Recipe> getRecipes() {
		return recipeRepository.findAll();
	}

	@Override
	public Long getRecipesCount() {
		return recipeRepository.count();
	}

	@Override
	public Recipe findById(Long id) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(id);
		
		return recipeOptional.orElseThrow(() -> new RuntimeException("Recipe not found: " + id));
	}
}
