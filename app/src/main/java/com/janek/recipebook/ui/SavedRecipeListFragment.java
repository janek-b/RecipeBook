package com.janek.recipebook.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.janek.recipebook.Constants;
import com.janek.recipebook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SavedRecipeListFragment extends Fragment {
    private Unbinder unbinder;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    @BindView(R.id.savedRecipeRecyclerView) RecyclerView savedRecipeRecyclerView;


    public SavedRecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        String userRecipes = String.format(Constants.FIREBASE_USER_RECIPES_LIST_REF, mAuth.getCurrentUser().getUid());
        DatabaseReference savedRecipeRef = rootRef.child(userRecipes);
        DatabaseReference recipeRef = rootRef.child(Constants.FIREBASE_RECIPE_REF);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
