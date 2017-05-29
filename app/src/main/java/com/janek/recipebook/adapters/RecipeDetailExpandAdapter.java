package com.janek.recipebook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.janek.recipebook.R;

import java.util.HashMap;

public class RecipeDetailExpandAdapter extends BaseExpandableListAdapter {
  private Context mContext;
  private String[] mHeaders;
  private HashMap<String, String[]> mChildData;

  public RecipeDetailExpandAdapter(Context context, String[] headers, HashMap<String, String[]> childData) {
    this.mContext = context;
    this.mHeaders = headers;
    this.mChildData = childData;
  }

  @Override
  public int getGroupCount() {
    return this.mHeaders.length;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return this.mChildData.get(this.mHeaders[groupPosition]).length;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return this.mChildData.get(this.mHeaders[groupPosition]);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return this.mChildData.get(this.mHeaders[groupPosition])[childPosition];
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.recipe_detail_group, null);
    }
    TextView groupHeader = (TextView) convertView.findViewById(R.id.list_header);
    groupHeader.setText(this.mHeaders[groupPosition]);
    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    String itemText = (String) getChild(groupPosition, childPosition);
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.recipe_detail_item, null);
    }
    TextView listItem = (TextView) convertView.findViewById(R.id.list_item);
    listItem.setText(itemText);
    return convertView;
  }

}
