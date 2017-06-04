package com.janek.recipebook.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.janek.recipebook.R;
import com.janek.recipebook.models.Recipe;
import org.parceler.Parcels;


import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {
  @Bind(R.id.recipe_detail_cook_time) TextView cookTimeTextView;
  @Bind(R.id.dairyFreeIcon) ImageView dairyFreeIcon;
  @Bind(R.id.glutenFreeIcon) ImageView glutenFreeIcon;
  @Bind(R.id.veganIcon) ImageView veganIcon;
  @Bind(R.id.vegetarianIcon) ImageView vegetarianIcon;

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
    View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    ButterKnife.bind(this, view);
    Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");
    cookTimeTextView.setTypeface(raleway);
    setVisibility(dairyFreeIcon, recipe.isDairyFree());
    setVisibility(glutenFreeIcon, recipe.isGlutenFree());
    setVisibility(veganIcon, recipe.isVegan());
    setVisibility(vegetarianIcon, recipe.isVegetarian());
    return view;
  }


  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getActivity().setTitle(recipe.getTitle());
    ((MainActivity)getActivity()).setBackdropImg(recipe.getImage());
    cookTimeTextView.setText(String.format("Cook Time: %d minutes", recipe.getCookTime()));
  }

  public void setVisibility(ImageView icon, boolean visible) {
    if (visible) {
      icon.setVisibility(View.VISIBLE);
    } else {
      icon.setVisibility(View.INVISIBLE);
    }
  }

}
