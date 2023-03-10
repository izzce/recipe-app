package org.izce.recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.izce.recipe.commands.DirectionCommand;
import org.izce.recipe.converters.DirectionCommandToDirection;
import org.izce.recipe.converters.DirectionToDirectionCommand;
import org.izce.recipe.model.Direction;
import org.izce.recipe.repositories.DirectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DirectionServiceImplTest {
	DirectionService directionService;
	Direction direction;
	@Mock DirectionRepository directionRepository; 
	DirectionCommandToDirection dc2d;
	DirectionToDirectionCommand d2dc;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		dc2d = new DirectionCommandToDirection();
		d2dc = new DirectionToDirectionCommand();
		directionService = new DirectionServiceImpl(directionRepository, dc2d, d2dc);
		direction = new Direction("direction1");
	}

	@Test
	void testFindById() {
		when(directionRepository.findById(anyLong())).thenReturn(Optional.of(direction));
		assertEquals(direction.getDirection(), directionService.findById(1L).getDirection());
		
		verify(directionRepository, times(1)).findById(anyLong());
	}

	@Test
	void testFindDirectionCommandById() {
		when(directionRepository.findById(anyLong())).thenReturn(Optional.of(direction));
		assertEquals(d2dc.convert(direction).getDirection(), directionService.findDirectionCommandById(anyLong()).getDirection());
		
		verify(directionRepository, times(1)).findById(anyLong());
	}

	@Test
	void testSaveDirectionCommand() {
		DirectionCommand nc = d2dc.convert(direction);
		when(directionRepository.save(any(Direction.class))).thenReturn(direction);
		DirectionCommand returnedNc = directionService.saveDirectionCommand(nc);
		
		assertEquals(nc.getDirection(), returnedNc.getDirection());
		verify(directionRepository, times(1)).save(any(Direction.class));
	}

	@Test
	void testDelete() {
		directionService.delete(1L);
		verify(directionRepository, times(1)).deleteById(1L);
	}

}
