package com.zybooks.beginners_kitchen;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FetchRecipe {
    public interface OnRecipeDataReceivedListener{
        void onRecipeSubjectReceived(List<Recipe> recipes);
        void onRecipeDataReceived(Recipe recipe);
        void onErrorResponse(VolleyError error);
    }

    private final String TAG = "FetchRecipe";
    private final String BASE_URL = "http://24.31.16.221:5000";
    private RequestQueue queue;

    // Constructor~
    public FetchRecipe(Context context){
        queue = Volley.newRequestQueue(context);
    }

    // This section is to get the names of recipes to fill in the home screen widgets
    public void fetchTheRecipeSubject(final OnRecipeDataReceivedListener listener){
        // Need to access the url containing the list of recipes
        String url = Uri.parse(BASE_URL).buildUpon().appendQueryParameter("type", "recipes")
                .build().toString();
        // Request all the Recipes
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Call to get the list of recipes from the JSON
                        List<Recipe> recipes = parseJsonRecipes(response);
                        listener.onRecipeSubjectReceived(recipes);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error);
                    }
                });
        queue.add(request);
    }

    // Communicates with the JSON data to get the list of recipes...
    private List<Recipe> parseJsonRecipes(JSONObject json){
        List<Recipe> recipes = new ArrayList<>();
        try{
            JSONArray recipeArray = json.getJSONArray("recipes");
            for(int i = 0; i < recipeArray.length(); i++){
                // Retrieve one of the JSON objects~
                JSONObject recipeObj = recipeArray.getJSONObject(i);
                // Now gonna translate that recipe into a Recipe obj
                Recipe recipe = new Recipe();
                recipe.setName(recipeObj.getString("name"));
                // add the recipeWidget information to the list
                recipes.add(recipe);
            }
        }
        catch (Exception e){
            Log.e(TAG, "One or more fields not found in the JSON data");
        }
        // returns the list of RecipeWidgets
        return recipes;
    }



    // Now for getting the full recipe information~
    // First need to communicate where to retrieve that info from and then call to get the JSON data
    public void fetchTheRecipeInfo(final Recipe data,
                                   final OnRecipeDataReceivedListener listener){
        // Need to set the Url
        String url = Uri.parse(BASE_URL).buildUpon().appendQueryParameter("type","recipe")
                .appendQueryParameter("recipes", data.getName()).build().toString();
        // Now to get the JSON Object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Recipe recipe = parseJsonRecipeInfo(response);
                listener.onRecipeDataReceived(recipe);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse(error);
            }
        });
        queue.add(jsonObjectRequest);
    }

    // Communicates with the JSON data to get the recipe data~
    private Recipe parseJsonRecipeInfo(JSONObject json){
        // create a recipe object~
        Recipe recipe = new Recipe();
        try{
            JSONObject recipeObj = json.getJSONObject("recipe");
            // Now gonna fill out the recipe info~
            recipe.setName(recipeObj.getString("name"));
            recipe.setCookingLevel(recipeObj.getString("difficulty"));
            recipe.setCookTime(recipeObj.getString("time"));
            recipe.setUserRating(recipeObj.getLong("rating"));
            /*
            // Need to figure out how to make this into a list
            // Possibly turn it into a string first, then have some helper function
            // to reformat into List of Strings
            recipe.setIngredients();
            recipe.setInstructions();
            recipe.setTags();
            */

        }
        catch (Exception e){
            Log.e(TAG, "One or more fields not found in the JSON data");
        }
        // return the recipe
        return recipe;
    }


}
