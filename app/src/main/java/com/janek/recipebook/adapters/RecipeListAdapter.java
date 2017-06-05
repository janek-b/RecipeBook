package com.janek.recipebook.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.RecipeList;
import com.janek.recipebook.models.RecipeListResponse;
import com.janek.recipebook.ui.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
  private List<RecipeList> mRecipes;
  private String mImageBaseUrl;

  public RecipeListAdapter(RecipeListResponse recipeResponse) {
    this.mRecipes = recipeResponse.getResults();
    this.mImageBaseUrl = recipeResponse.getBaseUri();
  }

  @Override
  public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
    RecipeListViewHolder viewHolder = new RecipeListViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecipeListViewHolder holder, final int position) {
    holder.bindRecipeList(mRecipes.get(position));
  }

  @Override
  public int getItemCount() {
    return mRecipes.size();
  }

  public class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;
    @Bind(R.id.recipe_title) TextView title;
    @Bind(R.id.recipe_desc) TextView desc;
    @Bind(R.id.recipe_img) ImageView img;

    private Context mContext;

    public RecipeListViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mContext = itemView.getContext();
      itemView.setOnClickListener(this);
    }

    public void bindRecipeList(RecipeList recipeList) {
      Typeface raleway = Typeface.createFromAsset(mContext.getAssets(), "fonts/raleway-regular.ttf");
      title.setTypeface(raleway);
      desc.setTypeface(raleway);
      title.setText(recipeList.getTitle());
      desc.setText(String.format("Cook Time: %d minutes", recipeList.getCookTime()));
      Picasso.with(mContext).load(String.format("%s%s", mImageBaseUrl, recipeList.getImage()))
          .resize(MAX_WIDTH, MAX_HEIGHT).centerCrop().into(img);
    }

    @Override
    public void onClick(View v) {
      RecipeList recipe = mRecipes.get(getLayoutPosition());
      ((MainActivity)mContext).getFullRecipe(recipe.getId());
//      ((MainActivity)mContext).getRecipe(recipe.getId());
    }
  }
}
