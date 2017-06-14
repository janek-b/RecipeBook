package com.janek.recipebook.ui;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.janek.recipebook.Constants;
import com.janek.recipebook.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


public class HomeFragment extends Fragment {
    @BindView(R.id.home_title) TextView mTitle;
    @BindView(R.id.search_field) EditText mSearchField;
    @BindView(R.id.search_button) Button mSearchButton;
    @BindView(R.id.diet_selector) Spinner mDietSelector;
    private Unbinder unbinder;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String[] diets;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private String uid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");
        mTitle.setTypeface(raleway);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mSharedPreferences.edit();
        diets = getResources().getStringArray(R.array.diets);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        disposable.clear();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter dietAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.diets, android.R.layout.simple_spinner_item);
        mDietSelector.setAdapter(dietAdapter);
        mDietSelector.setSelection(Arrays.asList(diets).indexOf(mSharedPreferences.getString(uid, "any")));

        disposable.add(RxAdapterView.itemSelections(mDietSelector).skipInitialValue().subscribe(i -> mEditor.putString(uid, diets[i]).apply()));

//        disposable.add(RxAdapterView.itemSelections(mDietSelector).skipInitialValue().subscribe(new Consumer<Integer>() {
//            @Override public void accept(@NonNull Integer i) throws Exception {
//                mEditor.putString(uid, diets[i]).apply();
//            }
//        }));

        ((MainActivity)getActivity()).setToolbarTitle("Recipe Book");

        disposable.add(RxView.clicks(mSearchButton).subscribe(event -> {
            String searchInput = mSearchField.getText().toString();
            if (searchInput.equals("")) {
                Toast.makeText(getActivity(), "No Search Input Provided", Toast.LENGTH_LONG).show();
            } else {
                mSearchField.setText("");
                ((MainActivity)getActivity()).runSearch(searchInput);
            }
        }));

//        mSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String searchInput = mSearchField.getText().toString();
//                if (searchInput.equals("")) {
//                    Toast.makeText(getActivity(), "No Search Input Provided", Toast.LENGTH_LONG).show();
//                } else {
//                    mSearchField.setText("");
//                    ((MainActivity)getActivity()).runSearch(searchInput);
//                }
//            }
//        });
    }
}
