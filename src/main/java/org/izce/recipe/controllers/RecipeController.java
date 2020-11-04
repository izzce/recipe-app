package org.izce.recipe.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.izce.recipe.commands.RecipeCommand;
import org.izce.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({ "recipe", "uomList" })
public class RecipeController {
	private final RecipeService recipeService;

	@Autowired
	public RecipeController(RecipeService recipeService) {
		log.debug("Initializing RecipeController ...");
		this.recipeService = recipeService;
	}

	@GetMapping("/recipe/{id}/show")
	public String showRecipe(@PathVariable String id, Model model) {
		log.debug("recipe/show page is requested!");
		model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
		return "recipe/show";
	}

	@GetMapping("/recipe/new")
	public String createRecipe(final Model model) {
		log.debug("recipe/new is requested!");
		model.addAttribute("recipe", new RecipeCommand());
		model.addAttribute("uomList", recipeService.findAllUoms());

		return "recipe/form";
	}

	@GetMapping("/recipe/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model) {
		log.debug("recipe/{}/update page is requested!", id);
		model.addAttribute("recipe", recipeService.findRecipeCommandById(Long.valueOf(id)));
		model.addAttribute("uomList", recipeService.findAllUoms());

		return "recipe/form";
	}

	@PostMapping("/recipe")
	public String saveOrUpdateRecipe(@Validated @ModelAttribute("recipe") RecipeCommand recipe,
			BindingResult bindingResult, Model model, SessionStatus status, 
			HttpServletRequest req, HttpSession session) {

		if (bindingResult.hasErrors()) {
			for (var error : bindingResult.getAllErrors()) {
				log.warn(error.toString());
			}
			// the model attr 'recipe' will still be avail to view for rendering!
			return "recipe/form";
		}
		
		//printRequestMap(req, session, model);

		RecipeCommand savedRecipe = recipeService.saveRecipeCommand(recipe);

		if (recipe.getId() == null) {
			// 1. createRecipe(...)
			// 2. recipe/form --> for main recipe details.
			// 3. saveOrUpdateRecipe(...)
			// 4. recipe/form --> for categories, directions, ingredients & notes.

			// A new recipe was just saved. Return to the form to update
			// for categories, directions, ingredients & notes details.
			model.addAttribute("recipe", savedRecipe);
			return "recipe/form";
		} else {
			// This is to remove 'recipe' from session.
			status.setComplete();
			return "redirect:/recipe/" + savedRecipe.getId() + "/show";
		}
	}
	
	/*
	 * private void printRequestMap(HttpServletRequest req, HttpSession session,
	 * Model model) { final StringBuilder sb = new StringBuilder();
	 * req.getParameterMap() .forEach((k, v) ->
	 * sb.append("   ").append(k).append(": ").append(Arrays.toString(v)).append('\n
	 * ')); log.info("Request Parameters:\n {}", sb);
	 * 
	 * final StringBuilder sb2 = new StringBuilder(); for (var attrNameItr =
	 * req.getAttributeNames().asIterator(); attrNameItr.hasNext();) { var name =
	 * attrNameItr.next();
	 * sb2.append("   ").append(name).append(": ").append(req.getAttribute(name)).
	 * append('\n'); } log.info("Request Attributes:\n {}", sb2);
	 * 
	 * System.out.println("*** Session data ***"); Enumeration<String> e =
	 * session.getAttributeNames(); while (e.hasMoreElements()) { String s =
	 * e.nextElement(); System.out.println(s); System.out.println("**" +
	 * session.getAttribute(s)); }
	 * 
	 * System.out.println("--- Model data ---"); var modelMap = model.asMap(); for
	 * (Object modelKey : modelMap.keySet()) { Object modelValue =
	 * modelMap.get(modelKey); System.out.println(modelKey + " -- " + modelValue); }
	 * }
	 */

}

