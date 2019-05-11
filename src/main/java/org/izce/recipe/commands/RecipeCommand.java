package org.izce.recipe.commands;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.izce.recipe.model.Difficulty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
    @NotEmpty
    private String source;
    private String url;
    private String imageUrl;
    @NotEmpty
    private List<String> directions = new ArrayList<>();
    @NotEmpty
    private List<IngredientCommand> ingredients = new ArrayList<>();
    @NotNull
    private Difficulty difficulty;
    private NotesCommand notes = new NotesCommand();
    @NotEmpty
    private List<CategoryCommand> categories = new ArrayList<>();

}
