package org.izce.recipe.commands;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.izce.recipe.model.Difficulty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RecipeCommand {
    private Long id;
    
    @NotEmpty
    @Size(min=3, max=100)
    private String description;
    @Range(min=1, max=200)
    private Integer prepTime;
    @Range(min=0, max=200)
    private Integer cookTime;
    @Range(min=1, max=10)
    private Integer servings;
    private String source;
    private String url;
    private String imageUrl;
    private Byte[] image;
    //@NotNull
    private Difficulty difficulty;
    //@NotEmpty
    private Set<DirectionCommand> directions = new LinkedHashSet<>();
    //@NotEmpty
    private Set<IngredientCommand> ingredients = new LinkedHashSet<>();
    //@NotEmpty
    private Set<CategoryCommand> categories = new LinkedHashSet<>();
    
    private Set<NoteCommand> notes = new LinkedHashSet<>();
    
    public RecipeCommand(Long id) {
    	this.id = id;
    }
}
