package com.janek.recipebook;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeDetail extends Fragment {
  @Bind(R.id.recipe_detail_name) TextView mTitle;
  @Bind(R.id.recipe_detail_desc) TextView mDesc;

  public RecipeDetail() {

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.recipe_detail, container, false);
    ButterKnife.bind(this, view);
    Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");

    Bundle bundle = getArguments();
    String title = bundle.getString("title");

    mTitle.setText(title);
    mTitle.setTypeface(raleway);
    mDesc.setTypeface(raleway);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
