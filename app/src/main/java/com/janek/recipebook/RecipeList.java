package com.janek.recipebook;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeList extends Fragment {
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
        String recipe = ((TextView)view.findViewById(R.id.recipe_title)).getText().toString();
        Toast.makeText(getActivity(), recipe, Toast.LENGTH_LONG).show();

        Fragment fragment = new RecipeDetail();
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
