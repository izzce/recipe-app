package org.izce.recipe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.izce.recipe.commands.IngredientCommand;
import org.izce.recipe.commands.UnitOfMeasureCommand;
import org.izce.recipe.converters.IngredientCommandToIngredient;
import org.izce.recipe.converters.IngredientToIngredientCommand;
import org.izce.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.recipe.model.Ingredient;
import org.izce.recipe.repositories.IngredientRepository;
import org.izce.recipe.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepo;
    private final UnitOfMeasureRepository uomRepo;
    private final IngredientCommandToIngredient ingc2ing;
    private final IngredientToIngredientCommand ing2ingc;
    private final UnitOfMeasureToUnitOfMeasureCommand uom2uomc;

	@Autowired
	public IngredientServiceImpl(
			IngredientRepository ir,
			IngredientCommandToIngredient ingc2ing,
			IngredientToIngredientCommand ing2ingc,
			UnitOfMeasureRepository uomr,
			UnitOfMeasureToUnitOfMeasureCommand uom2uomc) {
		
		log.debug("Initializing IngredientServiceImpl...");
		this.ingredientRepo = ir;
		this.ingc2ing = ingc2ing;
		this.ing2ingc = ing2ingc;
		this.uomRepo = uomr;
		this.uom2uomc = uom2uomc;
	}
	
	@Override
	public Ingredient findById(Long id) {
		Optional<Ingredient> ingredientOptional = ingredientRepo.findById(id);
		return ingredientOptional.orElseThrow(() -> new RuntimeException("Ingredient not found: " + id));
	}

	@Override
	public IngredientCommand findIngredientCommandById(Long id) {
		return ing2ingc.convert(findById(id));
	}
	
	@Override
	@Transactional
	public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
		Ingredient ingredient = ingc2ing.convert(ingredientCommand);
		ingredient = ingredientRepo.save(ingredient);
		
		log.info("Saved Ingredient: {}", ingredient);
		
		return ing2ingc.convert(ingredient);
	}
	
	@Override
	@Transactional
	public void delete(Long ingredientId) {
		ingredientRepo.deleteById(ingredientId);
		 
		log.info("Deleted Ingredient: {}", ingredientId);		 
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
	
}

