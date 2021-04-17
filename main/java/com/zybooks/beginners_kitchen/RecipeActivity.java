package com.zybooks.beginners_kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class RecipeActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_ID = "com.zybooks.beginners_kitchen.recipe_id";
    private RecipeDatabase recipeDb;
    private long recipeId;
    private Recipe recipe;

    private ImageView recipeImage;
    private TextView recipeName;
    private TextView difficultyLevel;
    private TextView timeRecipe;
    private TextView ingredientsList;
    private TextView instructionsList;
    private TextView tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();

        recipeDb = RecipeDatabase.getInstance();
        recipe = recipeDb.getRecipe(recipeId);

        recipeName = (TextView) findViewById(R.id.recipe_title_name);
        difficultyLevel = (TextView) findViewById(R.id.recipe_difficulty_level);
        timeRecipe = (TextView) findViewById(R.id.recipe_time);
        ingredientsList = (TextView) findViewById(R.id.ingredients_list);
        instructionsList = (TextView) findViewById(R.id.instructions_list);
        tagList = (TextView) findViewById(R.id.tag_list);

        updateRecipe(recipeId);
    }

    // Helper function that updates all the information in the view
    private void updateRecipe(long num){
        updateRecipeTitle();
        updateLevel();
        updateTime();
        // NEED TO UPDATE THE IMAGE
        updateIngredients();
        updateInstructions();
        updateTags();
    }

    // Helper functions to help update the recipe information
    // Updates the Recipe Name on the View
    private void updateRecipeTitle(){
        recipeName = (TextView) findViewById(R.id.recipe_title_name);
        recipeName.setText(recipe.getName());
    }

    private void updateImage(){
        recipeImage = (ImageView) findViewById(R.id.recipe_img_view);
        // WORK ON
    }

    // Updates the level difficulty  on the View

    private void updateLevel(){
        difficultyLevel = (TextView) findViewById(R.id.recipe_difficulty_level);
        difficultyLevel.setText(recipe.getCookingLevel());
    }

    // Updates the time on the View
    private void updateTime(){
        timeRecipe = (TextView) findViewById(R.id.recipe_time);
        timeRecipe.setText(recipe.getCookTime());
    }

    // Updates the list of ingredients on the View
    private void updateIngredients(){
        String str = getIngredientsListString();
        ingredientsList = (TextView) findViewById(R.id.ingredients_list);
        ingredientsList.setText(str);
    }

    // Updates the list of instructions on the View
    private void updateInstructions(){
        String str = getInstructionsListString();
        instructionsList = (TextView) findViewById(R.id.instructions_list);
        instructionsList.setText(str);
    }

    // Updates the list of tags on the View
    private void updateTags(){
        String str = getTagsListString();
        tagList = (TextView) findViewById(R.id.tag_list);
        tagList.setText(str);
    }

    // Helper functions to properly for the lists into strings
    // Format the list of ingredients into bullets points
    private String getIngredientsListString() {
        String str = "";
        List<String> temp = recipe.getIngredients();
        for (int i = 0; i < temp.size(); i ++){
            // adds the bullet point and a space between...
            str += "\u2022";
            str += " ";
            // adds the data onto the string...
            str += temp.get(i).toString();
            str += "\n";
        }
        // returns the properly formatted string for it to be printed
        return str;
    }

    private String getInstructionsListString() {
        String str = "";
        List<String> temp = recipe.getInstructions();
        // Now to format the string in numerical order
        for(int i = 0; i < temp.size(); i++){
            // prints the instruction number
            str += String.valueOf(i+1);
            str +=  ".) ";
            // adds the data onto the string
            str += temp.get(i).toString();
            str += "\n";
        }
        // returns the properly formatted string for it to be printed
        return str;
    }

    private String getTagsListString() {
        String str = "";
        List<String> temp = recipe.getTags();
        // prints the tags out one after another...
        for(int i = 0; i < temp.size(); i ++){
            str += temp.get(i).toString();
            // adds a comma if the tag list continues...
            if(i != temp.size()-1){
                str += ", ";
            }
        }
        // returns the properly formatted string for it to be printed
        return str;
    }
}