package com.janek.recipebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.janek.recipebook.R;
import com.janek.recipebook.adapters.RecipeListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment {
  @Bind(R.id.recipe_list) ListView mRecipeList;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.recipe_list, container, false);
    ButterKnife.bind(this, view);

    final String[] recipeNames = getResources().getStringArray(R.array.recipe_name);
    final String[] recipeDesc = getResources().getStringArray(R.array.recipe_description);
    mRecipeList.setAdapter(new RecipeListAdapter(getActivity(), recipeNames, recipeDesc));

    mRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Fragment fragment = new RecipeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", recipeNames[position]);
        fragment.setArguments(bundle);
        ((MainActivity)getActivity()).loadFragment(fragment);
      }
    });
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getActivity().setTitle("Recipes");
  }
}
