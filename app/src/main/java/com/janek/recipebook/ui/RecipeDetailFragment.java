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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment {
    @BindView(R.id.recipe_detail_viewPager) ViewPager viewPager;
    private Unbinder unbinder;
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
        unbinder = ButterKnife.bind(this, view);
        RestaurantDetailPagerAdapter adapter = new RestaurantDetailPagerAdapter(getChildFragmentManager());
        adapter.addFragment(RecipeDetailSummaryFragment.newInstance(recipe), "Summary");
        adapter.addFragment(InstructionsFragment.newInstance(recipe), "Instructions");
        viewPager.setAdapter(adapter);
        ((MainActivity)getActivity()).setTabLayout(viewPager);
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
        ((MainActivity)getActivity()).setRecipeTitle(recipe.getTitle());
        ((MainActivity)getActivity()).setBackdropImg(recipe.getImage());
    }

}
