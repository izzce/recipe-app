package org.izce.recipe.service;

import java.util.Optional;

import org.izce.recipe.commands.RecipeCommand;
import org.izce.recipe.converters.RecipeCommandToRecipe;
import org.izce.recipe.converters.RecipeToRecipeCommand;
import org.izce.recipe.model.Recipe;
import org.izce.recipe.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
	private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

	@Autowired
	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe rc2r, RecipeToRecipeCommand r2rc) {
		log.debug("Initializing RecipeServiceImpl...");
		this.recipeRepository = recipeRepository;
		this.recipeCommandToRecipe = rc2r;
		this.recipeToRecipeCommand = r2rc;
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
	
    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

         Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }
	
}
