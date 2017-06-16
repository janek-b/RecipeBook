package com.janek.recipebook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.util.ItemTouchHelperAdapter;
import com.janek.recipebook.util.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by janek on 6/16/17.
 */

public class FirebaseRecipeListAdapter extends FirebaseIndexRecyclerAdapter<Recipe, FirebaseRecipeListViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference savedRecipeRef;
    private DatabaseReference recipeRef;
    private OnStartDragListener onStartDragListener;
    private Context context;
    private ChildEventListener childEventListener;
    private ArrayList<String> recipeOrderArray = new ArrayList<>();

    public FirebaseRecipeListAdapter(Class<Recipe> modelClass, int modelLayout, Class<FirebaseRecipeListViewHolder> viewHolderClass, Query savedRecipeRef, Query recipeRef, OnStartDragListener onStartDragListener, Context context) {
        super(modelClass, modelLayout, viewHolderClass, savedRecipeRef, recipeRef);
        this.savedRecipeRef = savedRecipeRef.getRef();
        this.recipeRef = recipeRef.getRef();
        this.onStartDragListener = onStartDragListener;
        this.context = context;

        childEventListener = savedRecipeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                recipeOrderArray.add(dataSnapshot.getKey());
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    protected void populateViewHolder(FirebaseRecipeListViewHolder viewHolder, Recipe model, int position) {
        viewHolder.bindRecipe(model);
        viewHolder.img.setOnTouchListener((View v, MotionEvent event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                onStartDragListener.onStartDrag(viewHolder);
            }
            return false;
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(recipeOrderArray, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        recipeOrderArray.remove(position);
        getRef(position).removeValue();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        savedRecipeRef.removeEventListener(childEventListener);
        setIndexInFirebase();
    }

    private void setIndexInFirebase() {
        LinkedHashMap<String, Integer> newRecipeOrder = new LinkedHashMap<>();
        for (String key : recipeOrderArray) {
            newRecipeOrder.put(key, recipeOrderArray.indexOf(key));
        }
        savedRecipeRef.setValue(newRecipeOrder);
    }
}
