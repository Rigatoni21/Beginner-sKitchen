package com.zybooks.beginners_kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecipeAddActivity extends AppCompatActivity {
    private final String TAG = "RecipeActivity";
    private Recipe recipe;
    private List<String> listOfIngredients, listOfInstructions, listOfTags;
    private EditText etName, etIngredient, etInstruction, etTag;
    private ImageView img;
    private Button pictureBtn, uploadBtn;
    public final static int PHOTO_CODE = 100;
    private Uri imageUri;
    private final int TAKE_PHOTO = 1;
    private File photoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);
        recipe = new Recipe();
        listOfIngredients = new ArrayList<String>();
        listOfInstructions = new ArrayList<String>();
        listOfTags = new ArrayList<String>();

        img = (ImageView) findViewById(R.id.photo);
        uploadBtn = (Button) findViewById(R.id.upload_pic_btn);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotos();
            }
        });

        pictureBtn = (Button) findViewById(R.id.take_pic_btn);
        if(ContextCompat.checkSelfPermission(RecipeAddActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RecipeAddActivity.this, new String[]{
                    Manifest.permission.CAMERA}, PHOTO_CODE);
        }
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        // Now to set the info for the spinners~
        // First the difficulty spinner
        Spinner difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        // Set the spinner values and the layout~
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        // Need to specify the dropdown layout~
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set the adapter to the spinner...
        difficultySpinner.setAdapter(difficultyAdapter);
        // Create a listener for whatever the user decides for the spinner
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = (String)parent.getItemAtPosition(position);
                recipe.setCookingLevel(str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recipe.setCookingLevel("");
            }
        });

        // Now the Time Spinner~
        Spinner timeSpinner = (Spinner) findViewById(R.id.time_spinner);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.by_five, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = (String)parent.getItemAtPosition(position);
                recipe.setCookTime(str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recipe.setCookingLevel("");
            }
        });

    }

    // PHOTO UPLOAD SECTION~

    // This is for when the user wants to take a photo~
    // will need access to the camera in order to take the photo
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                    .format(new Date());
            String imageFileName = "photo_" + timeStamp + ".jpg";
            File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            photoFile = new File(file, imageFileName);

        }
        // Want to double check file was successfully created
        if(photoFile != null){
            startActivityForResult(intent, TAKE_PHOTO);
        }
    }

    // This is for when the user wants to upload a photo from camera roll~
    // Will need access to the camera roll in order to retrieve the photo
    private void openPhotos(){
        Intent photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(photo, PHOTO_CODE);
    }

    // Helps to actually display the photo taken
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);
        Log.i(TAG, "ONACTIVITYRESULT OPENED!!!");
        // This makes it so the picture taken by the camera gets put up...
        if(reqCode == TAKE_PHOTO){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 800, 1200, false));
        }
        // This makes it so the picture taken from "photo app" gets put up...
        if(resCode == RESULT_OK && reqCode == PHOTO_CODE){
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }

    // CLICK FUNCTION SECTION
    // Takes the ingredient that the user added, adds it to the ingredient list,
    // then displays the ingredient so user can keep track
    public void addIngredientClick(View view){
        // Gets the String that the user entered
        etIngredient = findViewById(R.id.ingredient_entered);
        String str = etIngredient.getText().toString();
        // Adds the new input onto the string...
        listOfIngredients.add(str);

        // Takes the string and lists it under the input request to allow the user easy access to
        // see what they previously entered
        TextView textView = (TextView) findViewById(R.id.list_ingredients);
        String temp = ListIngredients2String(listOfIngredients);
        textView.setText(temp);
        // Now clears the edit text so that the user can then be free to put in their next
        // ingredient
        etIngredient.getText().clear();

    }

    // Takes the instruction that the user added, adds it to the instruction list,
    // then displays the instruction in numerical order so user can keep track
    public void addInstructionClick(View view){
        // Gets the String that the user entered
        etInstruction = findViewById(R.id.instruction_entered);
        String str = etInstruction.getText().toString();
        listOfInstructions.add(str);
        // Takes the string and lists it under the input request to allow the user easy access to
        // see what they previously entered. Makes sure to show it in numerical order to keep it
        // organized
        String temp = ListInstructions2String(listOfInstructions);
        TextView textView = (TextView) findViewById(R.id.list_instructions);
        textView.setText(temp);

        // Now clears the edit text so that the user can then be free to put in their next
        // instruction
        etInstruction.getText().clear();
    }

    // Takes the tag that the user added, adds it to the tag list,
    // then displays the tag so user can keep track
    public void addTagClick(View view){
        // Gets the String that the user entered
        etTag = findViewById(R.id.tag_entered);
        String str = etTag.getText().toString();
        listOfTags.add(str);
        // Takes the string and lists it under the input request to allow the user easy access to
        // see what they previously input
        String temp = ListTags2String(listOfTags);
        TextView textView = (TextView) findViewById(R.id.list_tag);
        textView.setText(temp);

        // Now clears the edit text so that the user can then be free to put in their next
        // ingredient
        etTag.getText().clear();
    }

    // The click listener for when the user is done with the recipe
    // Will retrieve the recipe name, difficulty level, and cook time data to then add the
    // information
    // Will also assume that the user is finished with their ingredients, instructions, and tags,
    // and will now take the lists that were keeping track of that into the recipe object
    public void addRecipeClick(View view){
        etName = (EditText) findViewById(R.id.et_recipe_request);
        String str = etName.getText().toString();
        recipe.setName(str);

        // Checks that all the fields were filled out first...
        // Don't need to check difficulty level or time since they have default values...
        // Don't need to check tags since that's more to encourage user to fill out for fellow
        // users to get more information about the dish
        // Just need to check the recipe name, image, list of ingredients, and list of instructions
        if (wasRecipeNameFilled() && wasPictureChosen() && wasListsFilledOut()){
            // If everything was filled out, then the data can be added to the data base
            Toast.makeText(RecipeAddActivity.this,
                    "Thank your for your contributions to Beginner's Kitchen!",
                    Toast.LENGTH_LONG).show();
        }
    }

    // Helper functions to track that no data was left empty
    // Returns false if something was empty, but if everything was filled out, it returns true
    // First checks the recipe name was filled out...
    private boolean wasRecipeNameFilled(){
        // First retrieves the information since there was no button or spinner that logged the
        // info when it was entered
        // Checks to see if user left the field empty...
        if(recipe.getName() == null || recipe.getName() == " " || recipe.getName() == ""){
            // Sends a toast message to inform them what they need to fill out
            Toast.makeText(RecipeAddActivity.this, "Please fill out the Recipe Name",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        // if not empty, user must have input something so returns true
        return true;
    }
    // Checks that an image was input...
    private boolean wasPictureChosen(){
        return true;
    }
    // This checks that Ingredients and Instructions were filled out
    private boolean wasListsFilledOut(){
        // Just makes sure they have something in them~
        if(listOfIngredients.size() == 0 || listOfInstructions.size() == 0){
            // sends a toast message informing the user what to fill out
            if(listOfIngredients.size() == 0){
                Toast.makeText(RecipeAddActivity.this,
                        "Please add ingredients to the recipe", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(RecipeAddActivity.this,
                        "Please add instructions to the recipe", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        // if not empty, must have something inside them so returns true
        return true;
    }



    // Helper functions to help with the format that the input data will be displayed to make it
    // easy for the user to keep track
    // This function is to print out the ingredients that the user input...
    private String ListIngredients2String(List<String> list){
        String str = "";
        for(int i = 0; i < list.size(); i++){
            str += "\u2022";
            str += " ";
            str += list.get(i).toString();
            str += "\n";
        }
        return str;
    }

    // This function is to print out the instructions that the user input
    private String ListInstructions2String(List<String> list){
        String str = "";
        for(int i = 0; i < list.size(); i++){
            str += String.valueOf(i+1);
            str +=  ".) ";
            str += list.get(i).toString();
            str += "\n";
        }
        return str;
    }

    // This function is to print out the tags that the user input
    private String ListTags2String(List<String> list){
        String str = "";
        for(int i = 0; i < list.size(); i ++){
            str += list.get(i).toString();
            if(i != list.size()-1){
                str += ", ";
            }
        }
        return str;
    }


}