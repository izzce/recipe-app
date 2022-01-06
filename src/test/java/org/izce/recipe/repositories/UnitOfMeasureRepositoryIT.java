package org.izce.recipe.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.izce.recipe.model.UnitOfMeasure;
import org.izce.recipe.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) 
@DataJpaTest
public class UnitOfMeasureRepositoryIT {
	
	@Autowired
	UnitOfMeasureRepository repository;
	
	@Autowired
	StorageService storageService;
	
	@BeforeEach
	public void setUp() throws Exception {
	}

	@Test
	public void testFindByUom() {
		Optional<UnitOfMeasure> uom = repository.findByUom("Teaspoon");
		
		assertEquals("Teaspoon", uom.get().getUom());
	}
	
	@Test
	public void testFindByUomCup() {
		Optional<UnitOfMeasure> uom = repository.findByUom("Cup");
		
		assertEquals("Cup", uom.get().getUom());
	}

}
