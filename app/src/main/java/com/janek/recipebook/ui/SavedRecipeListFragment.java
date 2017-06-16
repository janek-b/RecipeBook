package com.janek.recipebook.ui;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.janek.recipebook.Constants;
import com.janek.recipebook.R;
import com.janek.recipebook.adapters.FirebaseRecipeListAdapter;
import com.janek.recipebook.adapters.FirebaseRecipeListViewHolder;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.util.OnStartDragListener;
import com.janek.recipebook.util.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SavedRecipeListFragment extends Fragment implements OnStartDragListener{
    private Unbinder unbinder;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private Query savedRecipeRef;
    private DatabaseReference recipeRef;
    private FirebaseRecipeListAdapter firebaseRecipeListAdapter;
    private ItemTouchHelper itemTouchHelper;

    @BindView(R.id.recipe_list) RecyclerView savedRecipeRecyclerView;


    public SavedRecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        String userRecipes = String.format(Constants.FIREBASE_USER_RECIPES_LIST_REF, mAuth.getCurrentUser().getUid());
        savedRecipeRef = rootRef.child(userRecipes).orderByValue();
        recipeRef = rootRef.child(Constants.FIREBASE_RECIPE_REF);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseRecipeListAdapter = new FirebaseRecipeListAdapter(Recipe.class, R.layout.recipe_list_item, FirebaseRecipeListViewHolder.class, savedRecipeRef, recipeRef, this, getActivity());

        savedRecipeRecyclerView.setAdapter(firebaseRecipeListAdapter);

        RecyclerView.LayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
        }
        savedRecipeRecyclerView.setLayoutManager(layoutManager);
        savedRecipeRecyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(firebaseRecipeListAdapter, getActivity());
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(savedRecipeRecyclerView);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        firebaseRecipeListAdapter.cleanup();
        unbinder.unbind();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

}
