package com.zybooks.beginners_kitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnRecipeSelectedListener,
        SearchDialogFragment.OnSearchSelectedListener {

    private static final String RECIPE_KEY = "recipeId";
    private int recipeId;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        // Sets the main fragment for the apps startup
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.search_recipes:
                DialogFragment newSearchFragment = new SearchDialogFragment();
                newSearchFragment.show(getSupportFragmentManager(), "search");
                return true;

            case R.id.add_recipe:
                Intent intent = new Intent(MainActivity.this, RecipeAddActivity.class);
                startActivity(intent);
                return true;

            case R.id.settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;

                    switch (item.getItemId()){
                        case R.id.home_button:
                            fragment = new HomeFragment();
                            break;
                        case R.id.favorite_button:
                            fragment = new FavoritesFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment).commit();
                    return true;
                }
            };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save state when something is selected
        if (recipeId != -1) {
            savedInstanceState.putInt(RECIPE_KEY, recipeId);
        }
    }

    @Override
    public void onRecipeSelectedListener(long numId) {
        Log.i("MainActivity", "RECIPE SELECTED");
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        startActivity(intent);
    }
}