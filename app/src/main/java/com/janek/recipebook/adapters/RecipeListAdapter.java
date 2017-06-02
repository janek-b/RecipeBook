package com.janek.recipebook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.RecipeList;
import com.janek.recipebook.models.RecipeListResponse;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipeListAdapter extends BaseAdapter {
  private static final int MAX_WIDTH = 400;
  private static final int MAX_HEIGHT = 300;

  private Context mContext;
  private List<RecipeList> mRecipes;
  private String mImageBaseUrl;

  public RecipeListAdapter(Context context, RecipeListResponse recipeListResponse) {
    this.mContext = context;
    this.mRecipes = recipeListResponse.getResults();
    this.mImageBaseUrl = recipeListResponse.getBaseUri();
  }

  @Override
  public int getCount() {
    return mRecipes.size();
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.recipe_list_item, null);
    }
    TextView title = (TextView) convertView.findViewById(R.id.recipe_title);
    TextView desc = (TextView) convertView.findViewById(R.id.recipe_desc);
    ImageView img = (ImageView) convertView.findViewById(R.id.recipe_img);
    title.setText(mRecipes.get(position).getTitle());
    desc.setText(String.format("%d", mRecipes.get(position).getReadyInMinutes()));
    Picasso.with(convertView.getContext()).load(String.format("%s%s", mImageBaseUrl, mRecipes.get(position).getImage()))
        .resize(MAX_WIDTH, MAX_HEIGHT).centerCrop().into(img);
    return convertView;
  }
}
