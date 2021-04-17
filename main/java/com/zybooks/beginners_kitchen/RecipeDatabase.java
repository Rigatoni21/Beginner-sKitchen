package com.zybooks.beginners_kitchen;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class RecipeDatabase {

    // database~
    private static RecipeDatabase rDatabase;
    // List of recipes
    private List<Recipe> recipes;

    public enum RecipeSortOrder{
        ALPHABETIC, UPDATE_DESC, UPDATE_ASC
    };

    public static RecipeDatabase getInstance(){
        if(rDatabase == null){
            rDatabase = new RecipeDatabase();
        }
        return rDatabase;
    }

    // Constructor~
    private RecipeDatabase(){
        recipes = new ArrayList<>();

        Recipe recipe = new Recipe("Pancakes");
        recipe.setId(1);
        recipes.add(recipe);

        recipe = new Recipe("Chicken Ramen");
        recipe.setId(2);
        recipes.add(recipe);

        recipe = new Recipe("Cookies");
        recipe.setId(3);
        recipes.add(recipe);

        recipe = new Recipe("Crepes");
        recipe.setId(4);
        recipes.add(recipe);

        recipe = new Recipe("Waffles");
        recipe.setId(5);
        recipes.add(recipe);

        recipe = new Recipe("Eggs");
        recipe.setId(6);
        recipes.add(recipe);

        recipe = new Recipe("Sausage");
        recipe.setId(7);
        recipes.add(recipe);

    }

    public Recipe getRecipe(long idNum){
        for(Recipe recipe: recipes){
            if(recipe.getId() == idNum){
                return recipe;
            }
        }
        return null;
    }

    public List<Recipe> getRecipes(RecipeSortOrder order){
        return recipes;
    }


}
