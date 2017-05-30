package com.janek.recipebook.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.janek.recipebook.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {
  @Bind(R.id.home_title) TextView mTitle;
  @Bind(R.id.search_field) EditText mSearchField;
  @Bind(R.id.search_button) Button mSearchButton;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home, container, false);
    ButterKnife.bind(this, view);
    Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");
    mTitle.setTypeface(raleway);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getActivity().setTitle("Recipe Book");
    mSearchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String searchInput = mSearchField.getText().toString();
        if (searchInput.equals("")) {
          Toast.makeText(getActivity(), "No Search Input Provided", Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(getActivity(), String.format("You Searched for %s \n Search is current not available", searchInput), Toast.LENGTH_LONG).show();
        }
      }
    });
  }
}
