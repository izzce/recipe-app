package org.izce.recipe.bootstrap;

import org.izce.recipe.model.Category;
import org.izce.recipe.model.Difficulty;
import org.izce.recipe.model.Ingredient;
import org.izce.recipe.model.Notes;
import org.izce.recipe.model.Recipe;
import org.izce.recipe.model.UnitOfMeasure;
import org.izce.recipe.repositories.CategoryRepository;
import org.izce.recipe.repositories.IngredientRepository;
import org.izce.recipe.repositories.NotesRepository;
import org.izce.recipe.repositories.RecipeRepository;
import org.izce.recipe.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;
	private final UnitOfMeasureRepository uomRepository;
	private final NotesRepository notesRepository;
	private final CategoryRepository categoryRepository;

	@Autowired
	public DataLoader(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
			UnitOfMeasureRepository uomRepository, NotesRepository notesRepository,
			CategoryRepository categoryRepository) {
		log.debug("Initializing DataLoader...");
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
		this.uomRepository = uomRepository;
		this.notesRepository = notesRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// CATEGORIES
		Category mexican = categoryRepository.findByDescription("Mexican").get();

		// UNIT of MEASURES
		UnitOfMeasure piece = uomRepository.findByUom("Piece").get();
		UnitOfMeasure teaspoon = uomRepository.findByUom("Teaspoon").get();
		UnitOfMeasure tablespoon = uomRepository.findByUom("Tablespoon").get();
		UnitOfMeasure dash = uomRepository.findByUom("Dash").get();
		UnitOfMeasure clove = uomRepository.findByUom("Clove").get();
		UnitOfMeasure pound = uomRepository.findByUom("Pound").get();
		UnitOfMeasure cup = uomRepository.findByUom("Cup").get();
		UnitOfMeasure pint = uomRepository.findByUom("Pint").get();

		// RECIPE-1: Perfect Guacamole
		Recipe recipe1 = new Recipe();
		recipe1.setDescription("How to Make Perfect Guacamole");
		recipe1.setCookTime(0);
		recipe1.setDirections("1 Cut avocado, remove flesh.\n" + "2 Mash with a fork.\n"
				+ "3 Add salt, lime juice, and the rest.\n" + "4 Cover with plastic and chill to store.\n");
		recipe1.setPrepTime(10);
		recipe1.setSource("Simply Recipes");
		recipe1.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
		recipe1.getCategories().add(mexican);
		recipe1.setServings(3);
		recipe1.setDifficulty(Difficulty.MODERATE);
		// DB will auto-generate the ID.
		recipe1 = recipeRepository.save(recipe1);
				
		// RECIPE-1 NOTES
		Notes notes1 = new Notes();
		notes1.setRecipeNotes(
				"1. Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\r\n"
						+ "\r\n"
						+ "2. Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\r\n"
						+ "\r\n"
						+ "3. Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\r\n"
						+ "\r\n"
						+ "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\r\n"
						+ "\r\n"
						+ "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\r\n"
						+ "\r\n"
						+ "4. Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve."
						+ "\r\n"
						+ "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.");
		recipe1.setNotes(notes1);
		notes1 = notesRepository.save(notes1);
		recipe1 = recipeRepository.save(recipe1);

		// INGREDIENTS of RECIPE-1
		this.addIngredient(recipe1, new Ingredient("ripe avocado", 2.0f, piece));
		this.addIngredient(recipe1, new Ingredient("salt", 0.5f, teaspoon));
		this.addIngredient(recipe1, new Ingredient("fresh lime juice or lemon juice", 1.0f, tablespoon));
		this.addIngredient(recipe1, new Ingredient("minced red onion or thinly sliced green onion", 2.0f, tablespoon));
		this.addIngredient(recipe1, new Ingredient("serrano chiles, stems and seeds removed, minced", 1.5f, piece));
		this.addIngredient(recipe1,
				new Ingredient("cilantro (leaves and tender stems), finely chopped", 2.0f, tablespoon));
		this.addIngredient(recipe1, new Ingredient("freshly grated black pepper", 1.0f, dash));
		this.addIngredient(recipe1, new Ingredient("ripe tomato, seeds and pulp removed, chopped", 0.5f, piece));
		
		log.debug("Added Recipe1: {}", "Perfect Guacamole!");
		
		
		// RECIPE-2: Spicy Grilled Chicken Tacos
		Recipe recipe2 = new Recipe();
		recipe2.setDescription("Spicy Grilled Chicken Tacos");
		recipe2.setCookTime(15);
		recipe2.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n"
				+ "2 Make the marinade and coat the chicken.\n" + "3 Grill the chicken.\n" + "4 Warm the tortillas.\n"
				+ "5 Assemble the tacos.");
		recipe2.setPrepTime(20);
		recipe2.setSource("Simply Recipes");
		recipe2.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
		recipe2.getCategories().add(mexican);
		recipe2.setServings(5);
		recipe2.setDifficulty(Difficulty.HARD);
		// DB will auto-generate the ID.
		recipe2 = recipeRepository.save(recipe2);
		
		// RECIPE-2 NOTES
		Notes notes2 = new Notes();
		notes2.setRecipeNotes("1 Prepare a gas or charcoal grill for medium-high, direct heat.\r\n" + "\r\n"
				+ "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. "
				+ "Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\r\n"
				+ "\r\n" + "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\r\n"
				+ "\r\n"
				+ "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. "
				+ "Transfer to a plate and rest for 5 minutes.\r\n" + "\r\n"
				+ "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat."
				+ " As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\r\n"
				+ "\r\n" + "Wrap warmed tortillas in a tea towel to keep them warm until serving.\r\n" + "\r\n"
				+ "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. "
				+ "Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");
		recipe2.setNotes(notes2);
		notes2 = notesRepository.save(notes2);
		recipe2 = recipeRepository.save(recipe2);

		// INGREDIENTS of RECIPE-2
		this.addIngredient(recipe2, new Ingredient("ancho chili powder", 2.0f, tablespoon));
		this.addIngredient(recipe2, new Ingredient("dried oregano", 1.0f, teaspoon));
		this.addIngredient(recipe2, new Ingredient("dried cumin", 1.0f, teaspoon));
		this.addIngredient(recipe2, new Ingredient("sugar", 1.0f, teaspoon));
		this.addIngredient(recipe2, new Ingredient("salt", 0.5f, teaspoon));
		this.addIngredient(recipe2, new Ingredient("garlic, finely chopped", 1.0f, clove));
		this.addIngredient(recipe2, new Ingredient("finely grated orange zest", 1.0f, teaspoon));
		this.addIngredient(recipe2, new Ingredient("fresh-squeezed orange juice", 3.0f, teaspoon));
		this.addIngredient(recipe2, new Ingredient("olive oil", 2.0f, teaspoon));
		this.addIngredient(recipe2, new Ingredient("skinless, boneless chicken thighs", 1.25f, pound));
		this.addIngredient(recipe2, new Ingredient("small corn tortillas", 8.0f, piece));
		this.addIngredient(recipe2, new Ingredient("packed baby arugula", 3.0f, cup));
		this.addIngredient(recipe2, new Ingredient("medium ripe avocados, sliced", 3.0f, piece));
		this.addIngredient(recipe2, new Ingredient("radishes, thinly sliced", 4.0f, piece));
		this.addIngredient(recipe2, new Ingredient("cherry tomatoes, halved", 0.5f, piece));
		this.addIngredient(recipe2, new Ingredient("red onion thinly sliced", 0.25f, piece));
		this.addIngredient(recipe2, new Ingredient("roughly chopped cilantaro", 1.0f, piece));
		this.addIngredient(recipe2, new Ingredient("sour cream thinned with 1/4 cup milk", 0.5f, cup));
		this.addIngredient(recipe2, new Ingredient("lime, cut into wedges", 1.0f, piece));
		log.debug("Added Recipe2: {}", "Spicy Grilled Chicken Tacos!");
	}

	private void addIngredient(Recipe recipe, Ingredient ingredient) {
		ingredient = ingredientRepository.save(ingredient);
		recipe.addIngredient(ingredient);
	}

}
