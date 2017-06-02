package com.janek.recipebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.janek.recipebook.R;
import com.janek.recipebook.adapters.RecipeListAdapter;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.RecipeList;
import com.janek.recipebook.models.RecipeListResponse;
import com.janek.recipebook.services.SpoonClient;
import com.janek.recipebook.services.SpoonService;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment {
  @Bind(R.id.recipe_list) ListView mRecipeList;

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
    mRecipeList.setAdapter(new RecipeListAdapter(getActivity(), recipeResponse));

    mRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        SpoonClient spoonClient = SpoonService.createService(SpoonClient.class);
        Call<Recipe> call = spoonClient.getRecipe(recipeResponse.getResults().get(position).getId());
        call.enqueue(new Callback<Recipe>() {
          @Override
          public void onResponse(Call<Recipe> call, Response<Recipe> response) {
            RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(response.body());
            ((MainActivity)getActivity()).loadFragment(recipeDetailFragment);
          }
          @Override public void onFailure(Call<Recipe> call, Throwable t) {t.printStackTrace();}
        });
//        Fragment fragment = new RecipeDetailFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("title", recipeNames[position]);
//        fragment.setArguments(bundle);
//        ((MainActivity)getActivity()).loadFragment(fragment);
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
