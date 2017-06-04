package com.janek.recipebook.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.Ingredient;
import com.janek.recipebook.models.Recipe;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeDetailSummaryFragment extends Fragment implements View.OnClickListener {
  @Bind(R.id.recipe_detail_cook_time) TextView cookTimeTextView;
  @Bind(R.id.recipe_detail_servings) TextView servingsTextView;
  @Bind(R.id.ingredients_label) TextView ingredientsLabel;
  @Bind(R.id.websiteTextView) TextView websiteTextView;
  @Bind(R.id.dairyFreeIcon) ImageView dairyFreeIcon;
  @Bind(R.id.glutenFreeIcon) ImageView glutenFreeIcon;
  @Bind(R.id.veganIcon) ImageView veganIcon;
  @Bind(R.id.vegetarianIcon) ImageView vegetarianIcon;
  @Bind(R.id.ingredient_list_layout) LinearLayout ingredientListLayout;

  private Recipe recipe;

  public static RecipeDetailSummaryFragment newInstance(Recipe recipe) {
    RecipeDetailSummaryFragment recipeDetailSummaryFragment = new RecipeDetailSummaryFragment();
    Bundle args = new Bundle();
    args.putParcelable("recipe", Parcels.wrap(recipe));
    recipeDetailSummaryFragment.setArguments(args);
    return recipeDetailSummaryFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recipe = Parcels.unwrap(getArguments().getParcelable("recipe"));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recipe_detail_summary, container, false);
    ButterKnife.bind(this, view);
    Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");
    cookTimeTextView.setTypeface(raleway);
    servingsTextView.setTypeface(raleway);
    ingredientsLabel.setTypeface(raleway);
    websiteTextView.setTypeface(raleway);
    setVisibility(dairyFreeIcon, recipe.isDairyFree());
    setVisibility(glutenFreeIcon, recipe.isGlutenFree());
    setVisibility(veganIcon, recipe.isVegan());
    setVisibility(vegetarianIcon, recipe.isVegetarian());

    websiteTextView.setOnClickListener(this);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    cookTimeTextView.setText(String.format("Cook Time: %d minutes", recipe.getCookTime()));
    servingsTextView.setText(String.format("Servings: %d", recipe.getServings()));
    for (Ingredient ingredient : recipe.getIngredients()) {
      TextView ingredientView = new TextView(getContext());
      ingredientView.setText(ingredient.getOriginalString());
      ingredientView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextIcons));
      ingredientView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
      ingredientListLayout.addView(ingredientView);
    }
  }

  public void setVisibility(ImageView icon, boolean visible) {
    if (visible) {
      icon.setVisibility(View.VISIBLE);
    } else {
      icon.setVisibility(View.GONE);
    }
  }

  @Override
  public void onClick(View v) {
    if (v == websiteTextView) {
      Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getSourceUrl()));
      startActivity(webIntent);
    }
  }
}
