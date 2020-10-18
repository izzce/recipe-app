package org.izce.recipe;

import org.izce.recipe.formatters.CategoryFormatter;
import org.izce.recipe.formatters.IngredientFormatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RecipeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeAppApplication.class, args);
	}

	
    @Configuration
    static class RecipeAppConfig implements WebMvcConfigurer {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addFormatter(new CategoryFormatter());
            registry.addFormatter(new IngredientFormatter());
        }
    }
}
