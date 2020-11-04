package org.izce.recipe.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DirectionCommand {
	private Long id;
	private String direction;
	private Long recipeId;
}