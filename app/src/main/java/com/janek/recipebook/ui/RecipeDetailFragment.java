package com.janek.recipebook.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.adapters.RecipeDetailExpandAdapter;
import com.janek.recipebook.models.Recipe;

import org.parceler.Parcels;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {
  @Bind(R.id.recipe_detail_name) TextView mTitle;
  @Bind(R.id.recipe_detail_instructions) TextView mInstructions;
  @Bind(R.id.expand_list) ExpandableListView mExpandListView;
  private RecipeDetailExpandAdapter mExpandListAdapter;

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

    mTitle.setText(recipe.getTitle());
    mInstructions.setText(recipe.getInstructions());
    mTitle.setTypeface(raleway);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
