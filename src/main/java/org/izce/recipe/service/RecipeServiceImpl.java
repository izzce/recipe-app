package org.izce.recipe.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.izce.recipe.commands.CategoryCommand;
import org.izce.recipe.commands.RecipeCommand;
import org.izce.recipe.commands.UnitOfMeasureCommand;
import org.izce.recipe.converters.CategoryToCategoryCommand;
import org.izce.recipe.converters.RecipeCommandToRecipe;
import org.izce.recipe.converters.RecipeToRecipeCommand;
import org.izce.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.recipe.model.Category;
import org.izce.recipe.model.Ingredient;
import org.izce.recipe.model.Recipe;
import org.izce.recipe.repositories.CategoryRepository;
import org.izce.recipe.repositories.IngredientRepository;
import org.izce.recipe.repositories.NotesRepository;
import org.izce.recipe.repositories.RecipeRepository;
import org.izce.recipe.repositories.UnitOfMeasureRepository;
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
    private final CategoryToCategoryCommand cTocc;
    private final CategoryRepository categoryRepo;
    private final IngredientRepository ingredientRepo;
    private final UnitOfMeasureRepository uomRepo;
    private final NotesRepository notesRepo;
    private final UnitOfMeasureToUnitOfMeasureCommand uom2uomc;

	@Autowired
	public RecipeServiceImpl(
			RecipeRepository rr, 
			CategoryRepository cr, 
			IngredientRepository ir,
			UnitOfMeasureRepository uomr,
			NotesRepository nr,
			RecipeCommandToRecipe rc2r, 
			RecipeToRecipeCommand r2rc,
			UnitOfMeasureToUnitOfMeasureCommand uom2uomc, 
			CategoryToCategoryCommand cTocc) {
		
		log.debug("Initializing RecipeServiceImpl...");
		this.recipeRepository = rr;
		this.categoryRepo = cr;
		this.ingredientRepo = ir;
		this.uomRepo = uomr;
		this.notesRepo = nr;
		this.recipeCommandToRecipe = rc2r;
		this.recipeToRecipeCommand = r2rc;
		this.uom2uomc = uom2uomc;
		this.cTocc = cTocc;
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
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe recipe = recipeCommandToRecipe.convert(recipeCommand);
        if (recipe.getCategories().size() > 0) {
        	Set<Category> newCategories = new LinkedHashSet<>();
        	for (Category c : recipe.getCategories()) {
        		if (c.getId() == null) {
        			Optional<Category> savedCategory = categoryRepo.findByDescriptionIgnoreCase(c.getDescription());
        			if (savedCategory.isPresent()) {
        				newCategories.add(savedCategory.get());
        			} else {
        				// First save the new category and then add to set of categories (a fresh set)!
        				newCategories.add(categoryRepo.save(c));
        			}
        		} else {
        			newCategories.add(c);
        		}
        	}
        	
        	recipe.setCategories(newCategories);
        }
       
        if (recipe.getId() == null) {
        	recipe = recipeRepository.save(recipe);
        }
        
        if (recipe.getIngredients().size() > 0) {
        	Set<Ingredient> newIngredients = new LinkedHashSet<>();
        	for(Ingredient ingredient : recipe.getIngredients()) {
        		ingredient.setRecipe(recipe);
        		if (ingredient.getId() == null) {
        			newIngredients.add(ingredientRepo.save(ingredient));
        		}
        	}
        	recipe.setIngredients(newIngredients);
        }
        
        if (recipe.getNotes() != null && recipe.getNotes().getId() == null) {
        	recipe.getNotes().setRecipe(recipe);
        	recipe.setNotes(notesRepo.save(recipe.getNotes()));
        }
        
        recipe = recipeRepository.save(recipe);

        log.info("Saved Recipe - id: {}, name: {}", recipe.getId(), recipe.getDescription());
        return recipeToRecipeCommand.convert(recipe);
    }

	@Override
	public CategoryCommand findCategoryByDescription(String description) {
		var cOpt = categoryRepo.findByDescriptionIgnoreCase(description);
		if (cOpt.isPresent()) {
			return cTocc.convert(cOpt.get());
		} else {
			// First save the new category and then add to set of categories (a fresh set)!
			Category c = new Category();
			c.setDescription(description);
			
			Category savedCategory = categoryRepo.save(c);
			log.info("Saved Category - id: {}, name: {}", savedCategory.getId(), savedCategory.getDescription());
			return cTocc.convert(savedCategory);
		}
	}
	
	
	@Override
	public UnitOfMeasureCommand findUom(String uom) {
		var uomOptional = uomRepo.findByUomIgnoreCase(uom);
		if (uomOptional.isPresent()) {
			return uom2uomc.convert(uomOptional.get());
		} else {
			throw new NoSuchElementException("No such UnitOfMeasure defined: " + uom);
		}
	}

	@Override
	public UnitOfMeasureCommand findUom(Long uomId) {
		var uomOptional = uomRepo.findById(uomId);
		if (uomOptional.isPresent()) {
			return uom2uomc.convert(uomOptional.get());
		} else {
			throw new NoSuchElementException("No such UnitOfMeasure defined: " + uomId);
		}
	}
	
	@Override
	public List<UnitOfMeasureCommand> findAllUoms() {
		List<UnitOfMeasureCommand> uomcList = new ArrayList<>();
		for(var uomc : uomRepo.findAll()) {
			uomcList.add(uom2uomc.convert(uomc));
		}
		return uomcList;
	}

	@Transactional
	@Override
	public RecipeCommand findRecipeCommandById(Long id) {
		return recipeToRecipeCommand.convert(findById(id));
	}
	
}
