package org.izce.recipe.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.izce.recipe.model.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) 
@DataJpaTest
public class UnitOfMeasureRepositoryIT {
	
	@Autowired
	UnitOfMeasureRepository repository;
	
	@Before
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
