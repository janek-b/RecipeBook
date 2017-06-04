package com.janek.recipebook.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.janek.recipebook.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutFragment extends Fragment {
  @Bind(R.id.about_title) TextView mTitle;
  @Bind(R.id.about_link) TextView mLink;
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_about, container, false);
    ButterKnife.bind(this, view);
    Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");
    mTitle.setTypeface(raleway);
    mLink.setMovementMethod(LinkMovementMethod.getInstance());
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ((MainActivity)getActivity()).setToolbarTitle("About");
  }
}
