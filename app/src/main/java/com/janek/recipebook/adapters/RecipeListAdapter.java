package com.janek.recipebook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.janek.recipebook.R;


public class RecipeListAdapter extends BaseAdapter {
  private Context mContext;
  private String[] mRecipeTitles;
  private String[] mRecipeDescriptions;

  public RecipeListAdapter(Context context, String[] recipeTitles, String[] recipeDescriptions) {
    this.mContext = context;
    this.mRecipeTitles = recipeTitles;
    this.mRecipeDescriptions = recipeDescriptions;
  }

  @Override
  public int getCount() {
    return mRecipeTitles.length;
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
    title.setText(mRecipeTitles[position]);
    desc.setText(mRecipeDescriptions[position]);
    return convertView;
  }
}
