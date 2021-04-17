package com.zybooks.beginners_kitchen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class HomeFragment extends Fragment {
    private final String TAG = "HomeActivity";
    private Menu mMenu;

    private RecipeDatabase recipeDatabase;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    public interface OnRecipeSelectedListener{
        void onRecipeSelectedListener(long numId);
    }
    private OnRecipeSelectedListener recipeListener;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_home, container, false);
        recipeDatabase = RecipeDatabase.getInstance();

        // 2 grid layout columns
        recyclerView = myView.findViewById(R.id.recipeRecycleViewer);
        RecyclerView.LayoutManager gridLayout =
                new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayout);

        // show the available Recipes
        adapter = new RecipeAdapter(loadRecipes());
        recyclerView.setAdapter(adapter);

        return myView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }


    private List<Recipe> loadRecipes(){
        return recipeDatabase.getRecipes(RecipeDatabase.RecipeSortOrder.UPDATE_DESC);
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Recipe lRecipe;
        private TextView text;

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            text = itemView.findViewById(R.id.recipeTextView);
        }

        public void bind(Recipe rec, int num){
            lRecipe = rec;
            text.setText(rec.getName());
        }

        @Override
        public void onClick(View v) {
            recipeListener.onRecipeSelectedListener(lRecipe.getId());
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder>{
        private List<Recipe> recipeList;

        public RecipeAdapter(List<Recipe> recipes){
            recipeList = recipes;
        }

        @NonNull
        @Override
        public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            RecipeHolder holder = new RecipeHolder(layoutInflater, parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
            holder.bind(recipeList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return recipeList.size();
        }
    }

    // Now for the attach and detach functions~
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof OnRecipeSelectedListener){
            recipeListener = (OnRecipeSelectedListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLocationSelectedListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        recipeListener = null;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            String str = (String) view.getTag();
            int num = Integer.parseInt(str);
            // call back to mainActivity
            recipeListener.onRecipeSelectedListener(num);
        }
    };

}