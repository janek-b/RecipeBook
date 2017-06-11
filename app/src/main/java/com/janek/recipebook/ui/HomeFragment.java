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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.janek.recipebook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment {
    @BindView(R.id.home_title) TextView mTitle;
    @BindView(R.id.search_field) EditText mSearchField;
    @BindView(R.id.search_button) Button mSearchButton;
    @BindView(R.id.diet_selector) Spinner mDietSelector;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");
        mTitle.setTypeface(raleway);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setToolbarTitle("Recipe Book");
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchInput = mSearchField.getText().toString();
                if (searchInput.equals("")) {
                    Toast.makeText(getActivity(), "No Search Input Provided", Toast.LENGTH_LONG).show();
                } else {
                    mSearchField.setText("");
                    ((MainActivity)getActivity()).runSearch(searchInput);
                }
            }
        });
    }
}
