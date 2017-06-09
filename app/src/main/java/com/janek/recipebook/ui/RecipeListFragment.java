package com.janek.recipebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janek.recipebook.R;
import com.janek.recipebook.adapters.RecipeListAdapter;
import com.janek.recipebook.models.RecipeListResponse;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeListFragment extends Fragment {
    @BindView(R.id.recipe_list) RecyclerView mRecipeList;
    private Unbinder unbinder;
    private String searchInput;
    private RecipeListResponse recipeResponse;

    public static RecipeListFragment newInstance(RecipeListResponse recipeResponse, String searchInput) {
        RecipeListFragment recipeListFragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putParcelable("recipes", Parcels.wrap(recipeResponse));
        args.putString("searchInput", searchInput);
        recipeListFragment.setArguments(args);
        return recipeListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeResponse = Parcels.unwrap(getArguments().getParcelable("recipes"));
        searchInput = getArguments().getString("searchInput");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mRecipeList.setAdapter(new RecipeListAdapter(recipeResponse));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecipeList.setLayoutManager(layoutManager);
        mRecipeList.setHasFixedSize(true);
        mRecipeList.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setToolbarTitle(String.format("Results for %s", searchInput));
    }

}
