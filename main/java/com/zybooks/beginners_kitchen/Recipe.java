package com.zybooks.beginners_kitchen;

import android.util.Log;

import java.util.List;

public class Recipe {
    private String name;
    private String cookingLevel;
    private String cookTime;
    private long userRating;
    private List<String> ingredients;
    private List<String> instructions;
    private List<String> tags;
    private long id;

    // default Constructor
    public Recipe(){
        name = "";
        cookingLevel = "";
        cookTime = "0";
        userRating = 0;
        ingredients = null;
        instructions = null;
        tags = null;

    }

    public Recipe(String str){
        name = str;
    }

    // constructor
    public Recipe(String nameStr, String levelStr, String timeNum, Integer ratingNum,
                  List<String> ingredientsList, List<String> instructionsList,
                  List<String> tagList){
        name = nameStr;
        cookingLevel = levelStr;
        cookTime = timeNum;
        userRating = ratingNum;
        ingredients = ingredientsList;
        instructions = instructionsList;
        tags = tagList;
    }

    // Setter Functions
    public void setName(String name) {
        this.name = name;
    }

    public void setCookingLevel(String cookingLevel) {
        this.cookingLevel = cookingLevel;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public void setUserRating(long userRating) {
        this.userRating = userRating;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setId(long num){
        id = num;
    }

    // Getter Functions


    public String getName() {
        return name;
    }

    public String getCookingLevel() {
        return cookingLevel;
    }

    public String getCookTime() {
        return cookTime;
    }

    public long getUserRating() {
        return userRating;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public List<String> getTags() {
        return tags;
    }

    public long getId(){
        return id;
    }
}
