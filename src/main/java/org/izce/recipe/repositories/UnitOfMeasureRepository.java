package org.izce.recipe.repositories;

import java.util.Optional;

import org.izce.recipe.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {
	Optional<UnitOfMeasure> findByUom(String uom);
	Optional<UnitOfMeasure> findByUomIgnoreCase(String uom);
}
