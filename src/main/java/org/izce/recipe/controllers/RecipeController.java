package org.izce.recipe.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.izce.recipe.commands.CategoryCommand;
import org.izce.recipe.commands.IngredientCommand;
import org.izce.recipe.commands.RecipeCommand;
import org.izce.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("recipe")
public class RecipeController {
	private final RecipeService recipeService;

	@Autowired
	public RecipeController(RecipeService recipeService) {
		log.debug("RecipeController IndexController...");
		this.recipeService = recipeService;
	}

	@RequestMapping("/recipe/{id}/show")
	public String showRecipe(@PathVariable String id, Model model) {
		log.debug("recipe/show page is requested!");
		model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
		return "recipe/show";
	}

    @RequestMapping("/recipe/new")
    public String createRecipe(final Model model){
    	RecipeCommand rc = new RecipeCommand();
    	rc.getCategories().add(recipeService.findCategoryByDescription("Turkish"));
    	rc.getDirections().add("Slice");
    	IngredientCommand ic = new IngredientCommand();
    	ic.setAmount(new BigDecimal(1));
    	ic.setUom(recipeService.findUom("clove"));
    	ic.setDescription("garlic");
    	rc.getIngredients().add(ic);
    	model.addAttribute("recipe", rc);
    	model.addAttribute("uomList", recipeService.findAllUoms());

        return "recipe/form";
    }
    
	@RequestMapping("/recipe/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model) {
		log.debug("recipe/{}/update page is requested!", id);
		model.addAttribute("recipe", recipeService.findRecipeCommandById(Long.valueOf(id)));
		return "recipe/form";
	}

    @PostMapping("/recipe")
    public String saveOrUpdateRecipe(@Validated @ModelAttribute("recipe") RecipeCommand recipe, 
    		BindingResult bindingResult, 
    		Model model, 
    		SessionStatus status) {
    	
    	if (bindingResult.hasErrors()) {
    		for (var error: bindingResult.getAllErrors()) {
    			log.warn(error.toString());
    		}
    		// the model attr 'recipe' will still be avail to view for rendering!
    		return "recipe/form";
    	}
    	
        RecipeCommand savedRecipe = recipeService.saveRecipeCommand(recipe);
        // This is to remove 'recipe' from session. 
        status.setComplete();

        return "redirect:/recipe/" + savedRecipe.getId() + "/show";
    }
    

    private void printRequestMap(HttpServletRequest req) {
    	var reqParamMap = req.getParameterMap();
    	StringBuilder sb = new StringBuilder();
    	reqParamMap.forEach((k,v) -> sb.append(k).append(": ").append(Arrays.toString(v)).append(' '));
    	log.info("params: {}", sb);
    }
    
    @PostMapping(value="/recipe/category/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody 
    public Map<String, String> manageCategory(
    		@ModelAttribute("recipe") RecipeCommand recipe, 
    		@PathVariable String action, 
    		HttpServletRequest req, 
    		HttpServletResponse res) throws Exception {
    	
    	var reqParamMap = req.getParameterMap();
    	printRequestMap(req);
    	
    	Map<String, String> map = new HashMap<>();
    	map.put("type", "category");
    	
    	if ("add".equalsIgnoreCase(action)) {
    		String element = reqParamMap.get("element")[0];
			if (recipe.getCategories().stream().anyMatch(e -> e.getDescription().equalsIgnoreCase(element))) {
				map.put("status", "PRESENT");
			} else {
				CategoryCommand cc = recipeService.findCategoryByDescription(element);
				recipe.getCategories().add(cc);
				map.putAll(cc.toMap());
				map.put("index", Integer.toString(recipe.getCategories().size() - 1));
				map.put("status", "OK");
			}
		} else if ("remove".equalsIgnoreCase(action)) {
			String index = reqParamMap.get("index")[0];
			int categoryIndex = Integer.valueOf(index);
			((ArrayList<CategoryCommand>) recipe.getCategories()).remove(categoryIndex);
			map.put("status", "OK");
		} else {
			map.put("status", "ERROR");
	    	map.put("message", "Unsupported action: " + action);
		}

    	return map;
	}

    
    @PostMapping(value="/recipe/direction/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody 
    public Map<String, String> manageDirection(
    		@ModelAttribute("recipe") RecipeCommand recipe, 
    		@PathVariable String action, 
    		HttpServletRequest req, 
    		HttpServletResponse res) throws Exception {
    	
    	var reqParamMap = req.getParameterMap();
    	printRequestMap(req);
    	
    	Map<String, String> map = new HashMap<>();
    	map.put("type", "direction");
    	
    	if ("add".equalsIgnoreCase(action)) {
    		String element = reqParamMap.get("element")[0];
			if (recipe.getDirections().stream().anyMatch(e -> e.equalsIgnoreCase(element))) {
				map.put("status", "PRESENT");
			} else {
				recipe.getDirections().add(element);
				map.put("description", element);
				map.put("index", Integer.toString(recipe.getDirections().size() - 1));
				map.put("status", "OK");
			}
		} else if ("remove".equalsIgnoreCase(action)) {
			String index = reqParamMap.get("index")[0];
			int directionIndex = Integer.valueOf(index);
			((ArrayList<String>) recipe.getDirections()).remove(directionIndex);
			map.put("status", "OK");
		} else {
			map.put("status", "ERROR");
			map.put("message", "Unsupported action: " + action);
		}

		return map;
	}
    
    
    @PostMapping(value="/recipe/ingredient/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody 
    public Map<String, String> manageIngredient(
    		@ModelAttribute("recipe") RecipeCommand recipe, 
    		@PathVariable String action, 
    		HttpServletRequest req, 
    		HttpServletResponse res) throws Exception {
    	
    	var reqParamMap = req.getParameterMap();
    	printRequestMap(req);
    	
    	Map<String, String> map = new HashMap<>();
    	map.put("type", "ingredient");

    	if ("add".equalsIgnoreCase(action)) {
    		String element = reqParamMap.get("element")[0];
    		String amount = reqParamMap.get("element2")[0];
        	String uomId = reqParamMap.get("element3")[0];
        	
			if (recipe.getIngredients().stream().anyMatch(e -> e.getDescription().equalsIgnoreCase(element))) {
				map.put("status", "PRESENT");
			} else {
				IngredientCommand ic = new IngredientCommand();
		    	ic.setAmount(new BigDecimal(amount));
		    	ic.setUom(recipeService.findUom(Long.valueOf(uomId)));
		    	ic.setDescription(element);
				recipe.getIngredients().add(ic);
				
				map.put("description", ic.toString());
				map.put("index", Integer.toString(recipe.getIngredients().size() - 1));
				map.put("status", "OK");
			}
		} else if ("remove".equalsIgnoreCase(action)) {
			String index = reqParamMap.get("index")[0];
			int ingredientIndex = Integer.valueOf(index);
			((ArrayList<IngredientCommand>) recipe.getIngredients()).remove(ingredientIndex);
			map.put("status", "OK");
		} else {
			map.put("status", "ERROR");
			map.put("message", "Unsupported action: " + action);
		}

		return map;
	}
    
}

