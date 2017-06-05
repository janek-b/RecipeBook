package com.janek.recipebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.janek.recipebook.R;
import com.janek.recipebook.adapters.RestaurantDetailPagerAdapter;
import com.janek.recipebook.models.Recipe;
import org.parceler.Parcels;


import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {
  @Bind(R.id.recipe_detail_viewPager) ViewPager viewPager;

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
    Log.d("test", recipe.getFullInstructions().toString());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    ButterKnife.bind(this, view);
    RestaurantDetailPagerAdapter adapter = new RestaurantDetailPagerAdapter(getActivity().getSupportFragmentManager());
    adapter.addFragment(RecipeDetailSummaryFragment.newInstance(recipe), "Summary");
    adapter.addFragment(RecipeDetailSummaryFragment.newInstance(recipe), "Instructions");

    viewPager.setAdapter(adapter);
    ((MainActivity)getActivity()).setTabLayout(viewPager);
    return view;
  }


  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ((MainActivity)getActivity()).setRecipeTitle(recipe.getTitle());
    ((MainActivity)getActivity()).setBackdropImg(recipe.getImage());
  }

}
