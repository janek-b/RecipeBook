package com.janek.recipebook.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.RecipeList;
import com.janek.recipebook.models.RecipeListResponse;
import com.janek.recipebook.services.SpoonClient;
import com.janek.recipebook.services.SpoonService;
import com.janek.recipebook.ui.MainActivity;
import com.janek.recipebook.ui.RecipeDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
  private Context mContext;
  private List<RecipeList> mRecipes;
  private String mImageBaseUrl;

  public RecipeListAdapter(Context context, RecipeListResponse recipeResponse) {
    this.mContext = context;
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
  public void onBindViewHolder(RecipeListViewHolder holder, int position) {
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
      title.setText(recipeList.getTitle());
      desc.setText(String.format("Cook Time: %d minutes", recipeList.getCookTime()));
      Picasso.with(mContext).load(String.format("%s%s", mImageBaseUrl, recipeList.getImage()))
          .resize(MAX_WIDTH, MAX_HEIGHT).centerCrop().into(img);
    }
    @Override
    public void onClick(View v) {
      final ProgressDialog loading = new ProgressDialog(mContext);
      loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      loading.setMessage("Getting recipe Details...");
      loading.setIndeterminate(true);
      loading.show();

      RecipeList recipe = mRecipes.get(getLayoutPosition());

      SpoonClient spoonClient = SpoonService.createService(SpoonClient.class);
      Call<Recipe> call = spoonClient.getRecipe(recipe.getId());
      call.enqueue(new Callback<Recipe>() {
        @Override
        public void onResponse(Call<Recipe> call, Response<Recipe> response) {
          RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(response.body());

          ((MainActivity)mContext).loadFragment(recipeDetailFragment);
          loading.dismiss();
        }
        @Override public void onFailure(Call<Recipe> call, Throwable t) {t.printStackTrace();}
      });
    }
  }
}
