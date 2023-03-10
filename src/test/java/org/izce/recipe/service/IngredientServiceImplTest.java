package org.izce.recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.izce.recipe.commands.IngredientCommand;
import org.izce.recipe.commands.UnitOfMeasureCommand;
import org.izce.recipe.converters.IngredientCommandToIngredient;
import org.izce.recipe.converters.IngredientToIngredientCommand;
import org.izce.recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import org.izce.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.recipe.model.Ingredient;
import org.izce.recipe.model.UnitOfMeasure;
import org.izce.recipe.repositories.IngredientRepository;
import org.izce.recipe.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class IngredientServiceImplTest {
	IngredientService ingredientService;
	Ingredient ingredient;
	UnitOfMeasure uom;
	@Mock IngredientRepository ingRepo; 
	@Mock UnitOfMeasureRepository uomRepo; 
	UnitOfMeasureCommandToUnitOfMeasure uomc2uom;
	UnitOfMeasureToUnitOfMeasureCommand uom2uomc;
	IngredientCommandToIngredient ic2i;
	IngredientToIngredientCommand i2ic;
	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		uomc2uom = new UnitOfMeasureCommandToUnitOfMeasure();
		uom2uomc = new UnitOfMeasureToUnitOfMeasureCommand();
		ic2i = new IngredientCommandToIngredient(uomc2uom);
		i2ic = new IngredientToIngredientCommand(uom2uomc);
		ingredientService = new IngredientServiceImpl(ingRepo, ic2i, i2ic, uomRepo, 
				new UnitOfMeasureToUnitOfMeasureCommand());
		uom = new UnitOfMeasure();
		uom.setUom("spoon");
		ingredient = new Ingredient("sugar", 1, uom);
	}

	@Test
	void testFindById() {
		when(ingRepo.findById(anyLong())).thenReturn(Optional.of(ingredient));
		assertEquals(ingredient.getDescription(), ingredientService.findById(1L).getDescription());
		
		verify(ingRepo, times(1)).findById(anyLong());
	}

	@Test
	void testFindIngredientCommandById() {
		when(ingRepo.findById(anyLong())).thenReturn(Optional.of(ingredient));
		assertEquals(i2ic.convert(ingredient).getDescription(), 
				ingredientService.findIngredientCommandById(anyLong()).getDescription());
		
		verify(ingRepo, times(1)).findById(anyLong());
	}

	@Test
	void testSaveIngredientCommand() {
		IngredientCommand ic = i2ic.convert(ingredient);
		when(ingRepo.save(any(Ingredient.class))).thenReturn(ingredient);
		IngredientCommand returnedIc = ingredientService.saveIngredientCommand(ic);
		
		assertEquals(ic.getDescription(), returnedIc.getDescription());
		verify(ingRepo, times(1)).save(any(Ingredient.class));
	}

	@Test
	void testDelete() {
		ingredientService.delete(1L);
		verify(ingRepo, times(1)).deleteById(1L);
	}

	@Test
	void testFindUomString() {
		when(uomRepo.findByUomIgnoreCase(anyString())).thenReturn(Optional.of(uom));
		assertEquals(uom.getUom(), ingredientService.findUom(anyString()).getUom());
		
		verify(uomRepo, times(1)).findByUomIgnoreCase(anyString());
	}

	@Test
	void testFindUomLong() {
		when(uomRepo.findById(anyLong())).thenReturn(Optional.of(uom));
		assertEquals(uom.getUom(), ingredientService.findUom(anyLong()).getUom());
		
		verify(uomRepo, times(1)).findById(anyLong());
	}

	@Test
	void testFindAllUoms() {
		when(uomRepo.findAll()).thenReturn(List.of(uom));
		List<UnitOfMeasureCommand> uomList = ingredientService.findAllUoms();
		
		assertEquals(List.of(uom), List.of(uomc2uom.convert(uomList.get(0))));
		
		verify(uomRepo, times(1)).findAll();
	}

}
