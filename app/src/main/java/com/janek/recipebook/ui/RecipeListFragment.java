package com.janek.recipebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janek.recipebook.R;
import com.janek.recipebook.adapters.RecipeListAdapter;
import com.janek.recipebook.models.RecipeListResponse;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment {
  @Bind(R.id.recipe_list) RecyclerView mRecipeList;

  private RecipeListResponse recipeResponse;

  public static RecipeListFragment newInstance(RecipeListResponse recipeResponse) {
    RecipeListFragment recipeListFragment = new RecipeListFragment();
    Bundle args = new Bundle();
    args.putParcelable("recipes", Parcels.wrap(recipeResponse));
    recipeListFragment.setArguments(args);
    return recipeListFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recipeResponse = Parcels.unwrap(getArguments().getParcelable("recipes"));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.recipe_list, container, false);
    ButterKnife.bind(this, view);
    Log.d("fragment", recipeResponse.toString());
    mRecipeList.setAdapter(new RecipeListAdapter(recipeResponse));
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mRecipeList.setLayoutManager(layoutManager);
    mRecipeList.setHasFixedSize(true);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getActivity().setTitle("Recipes");
  }
}
