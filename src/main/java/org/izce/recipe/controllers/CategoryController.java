package org.izce.recipe.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.izce.recipe.commands.CategoryCommand;
import org.izce.recipe.commands.RecipeCommand;
import org.izce.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({ "recipe" })
public class CategoryController {
	private final RecipeService recipeService;

	@Autowired
	public CategoryController(RecipeService recipeService) {
		log.debug("CategoryController ...");
		this.recipeService = recipeService;
	}
	
	@PostMapping(value = "/recipe/{recipeId}/category/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> addCategory(
			@PathVariable String recipeId, 
			@RequestBody CategoryCommand category,
			@ModelAttribute("recipe") RecipeCommand recipe,
			Model model,
			HttpServletResponse res) throws Exception {

		Map<String, String> map = new HashMap<>();
		map.put("type", "category");
	
		if (recipe.getCategories().stream().anyMatch(e -> e.getDescription().equalsIgnoreCase(category.getDescription()))) {
			map.put("status", "PRESENT");
		} else {
			CategoryCommand cc = recipeService.findCategoryByDescription(category.getDescription());
			recipe.getCategories().add(cc);
			map.put("id", cc.getId().toString());
			map.put("description", cc.getDescription());

			map.put("status", "OK");
		}

		return map;
	}

	@PostMapping(value = "/recipe/{recipeId}/category/{categoryId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> deleteCategory(
			@ModelAttribute("recipe") RecipeCommand recipe,
			@PathVariable String recipeId, 
			@PathVariable String categoryId, 
			HttpServletResponse resp) throws Exception {

		Map<String, String> map = new HashMap<>();
		map.put("type", "category");
		map.put("id", categoryId);
		
		Long categoryIdLong = Long.valueOf(categoryId);
		boolean elementRemoved = recipe.getCategories().removeIf(e -> e.getId() == categoryIdLong);
		if (elementRemoved) {
			map.put("status", "OK");
		} else {			
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		return map;
	}

}

