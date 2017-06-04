package com.janek.recipebook.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.Ingredient;
import com.janek.recipebook.models.Recipe;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {
  private static final int MAX_WIDTH = 400;
  private static final int MAX_HEIGHT = 300;

  @Bind(R.id.recipe_detail_name) TextView titleTextView;
//  @Bind(R.id.recipe_detail_instructions) TextView instructionsTextView;
  @Bind(R.id.recipe_detail_cook_time) TextView cookTimeTextView;
  @Bind(R.id.recipe_detail_img) ImageView recipeImageView;
  @Bind(R.id.dairyFreeIcon) ImageView dairyFreeIcon;
  @Bind(R.id.glutenFreeIcon) ImageView glutenFreeIcon;
  @Bind(R.id.veganIcon) ImageView veganIcon;
  @Bind(R.id.vegetarianIcon) ImageView vegetarianIcon;
  @Bind(R.id.ingredient_list) ListView ingredientListView;


  private Recipe recipe;

  public static RecipeDetailFragment newInstance(Recipe recipe) {
    RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
    Bundle args = new Bundle();
    args.putParcelable("recipe", Parcels.wrap(recipe));
    recipeDetailFragment.setArguments(args);
    return recipeDetailFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recipe = Parcels.unwrap(getArguments().getParcelable("recipe"));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.recipe_detail, container, false);
    ButterKnife.bind(this, view);
    Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");

    ((MainActivity)getActivity()).setBackdropImg(recipe.getImage());
    Picasso.with(getContext()).load(recipe.getImage()).resize(MAX_WIDTH, MAX_HEIGHT).centerCrop().into(recipeImageView);
    titleTextView.setText(recipe.getTitle());
    cookTimeTextView.setText(String.format("Cook Time: %d minutes", recipe.getCookTime()));
//    instructionsTextView.setText(recipe.getInstructions());

    setVisibility(dairyFreeIcon, recipe.isDairyFree());
    setVisibility(glutenFreeIcon, recipe.isGlutenFree());
    setVisibility(veganIcon, recipe.isVegan());
    setVisibility(vegetarianIcon, recipe.isVegetarian());

    List<String> ingredients = new ArrayList<>();
    for (Ingredient ingredient : recipe.getIngredients()) {
      ingredients.add(ingredient.getOriginalString());
    }
    String[] ingredientsArray = ingredients.toArray(new String[ingredients.size()]);
    ArrayAdapter ingredientAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, ingredientsArray);
    ingredientListView.setAdapter(ingredientAdapter);
    titleTextView.setTypeface(raleway);
    return view;
  }

  public void setVisibility(ImageView icon, boolean visible) {
    if (visible) {
      icon.setVisibility(View.VISIBLE);
    } else {
      icon.setVisibility(View.INVISIBLE);
    }
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

}
