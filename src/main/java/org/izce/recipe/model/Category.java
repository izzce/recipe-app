package org.izce.recipe.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	
	@ToString.Exclude 
	@ManyToMany(mappedBy="categories")
	@EqualsAndHashCode.Exclude 
	private Set<Recipe> recipes = new LinkedHashSet<Recipe>();

}
