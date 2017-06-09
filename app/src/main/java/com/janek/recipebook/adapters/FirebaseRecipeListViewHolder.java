package com.janek.recipebook.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.ui.MainActivity;
import com.janek.recipebook.ui.RecipeDetailFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FirebaseRecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;
    @BindView(R.id.recipe_title) TextView title;
    @BindView(R.id.recipe_desc) TextView desc;
    @BindView(R.id.recipe_img) ImageView img;

    View mView;
    Context mContext;
    Recipe mRecipe;

    public FirebaseRecipeListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindRecipe(Recipe recipe) {
        mRecipe = recipe;
        Typeface raleway = Typeface.createFromAsset(mContext.getAssets(), "fonts/raleway-regular.ttf");
        title.setTypeface(raleway);
        desc.setTypeface(raleway);
        title.setText(recipe.getTitle());
        desc.setText(String.format("Cook Time: %d minutes", recipe.getCookTime()));
        Picasso.with(mContext).load(recipe.getImage()).resize(MAX_WIDTH, MAX_HEIGHT).centerCrop().into(img);
    }

    @Override
    public void onClick(View v) {
        ((MainActivity)mContext).loadFragment(RecipeDetailFragment.newInstance(mRecipe, true));
    }
}
