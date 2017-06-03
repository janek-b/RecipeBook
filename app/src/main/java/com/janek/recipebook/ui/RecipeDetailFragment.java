package com.janek.recipebook.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.adapters.RecipeDetailExpandAdapter;
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

//  @Bind(R.id.scrollView) ScrollView scrollView;
//  @Bind(R.id.frameLayoutWrapper) FrameLayout frameLayout;


//  @Bind(R.id.expand_list) ExpandableListView mExpandListView;
//  private RecipeDetailExpandAdapter mExpandListAdapter;

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

//    scrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());


//    String[] instructions = getResources().getStringArray(R.array.directions);
//    String[] ingredients = getResources().getStringArray(R.array.ingredients);

//    Bundle bundle = getArguments();
//    String title = bundle.getString("title");

//    String[] headers = new String[] {"Instructions", "Ingredients"};
//    HashMap<String, String[]> childData = new HashMap<String, String[]>();
//    childData.put(headers[0], instructions);
//    childData.put(headers[1], ingredients);
//
//    mExpandListAdapter = new RecipeDetailExpandAdapter(getActivity(), headers, childData);
//    mExpandListView.setAdapter(mExpandListAdapter);

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


//  private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {
//
//    private int imageViewHeight;
//
//    public ScrollPositionObserver() {
//      imageViewHeight = getResources().getDimensionPixelSize(R.dimen.recipe_image_height);
//    }
//
//    @Override
//    public void onScrollChanged() {
//      int scrollY = Math.min(Math.max(scrollView.getScrollY(), 0), imageViewHeight);
//
//      // changing position of ImageView
//      recipeImageView.setTranslationY(scrollY / 2);
//
//      // alpha you could set to ActionBar background
//      float alpha = scrollY / (float) imageViewHeight;
//      recipeImageView.setAlpha(1 - alpha);
//    }
//  }

}
