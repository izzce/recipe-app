package org.izce.recipe.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
public class Recipe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	private Integer prepTime;
	private Integer cookTime;
	private Integer servings;
	private String source;
	private String url;
	private String directions;
	@Enumerated(value = EnumType.STRING)
	private Difficulty difficulty;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
	@EqualsAndHashCode.Exclude 
	private Set<Ingredient> ingredients = new HashSet<Ingredient>();
	@Lob
	private Byte[] image;
	private String imageUrl;

	@OneToOne(cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude 
	private Notes notes;

	@ManyToMany
	@JoinTable(name = "recipe_category", 
			joinColumns = @JoinColumn(name = "recipe_id"), 
			inverseJoinColumns = @JoinColumn(name = "category_id"))
	@EqualsAndHashCode.Exclude 
	private Set<Category> categories = new HashSet<Category>();

	public void setNotes(Notes notes) {
		if (notes != null) {
			this.notes = notes;
			notes.setRecipe(this);
		}
	}
	
	public void addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}
	
	public String[] getDirectionsList() {
		String[] directionsArray = directions.split("\\d\\.");
		return directionsArray;
	}

}
