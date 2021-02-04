package org.izce.recipe.service;

import static org.junit.Assert.assertEquals;

import org.izce.recipe.commands.RecipeCommand;
import org.izce.recipe.converters.RecipeCommandToRecipe;
import org.izce.recipe.converters.RecipeToRecipeCommand;
import org.izce.recipe.model.Recipe;
import org.izce.recipe.repositories.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Transactional
    @Test
    public void testSaveOfDescription() throws Exception {
        //given
        for (Recipe recipe : recipeRepository.findAll()) {
	        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
	
	        //when
	        recipeCommand.setDescription(NEW_DESCRIPTION);
	        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
	
	        //then
	        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
	        assertEquals(recipe.getId(), savedRecipeCommand.getId());
	        assertEquals(recipe.getCategories().size(), savedRecipeCommand.getCategories().size());
	        assertEquals(recipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
        }
    }
}