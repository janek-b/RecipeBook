package com.janek.recipebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janek.recipebook.R;
import com.janek.recipebook.adapters.InstructionsAdapter;
import com.janek.recipebook.models.Recipe;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstructionsFragment extends Fragment {
  @Bind(R.id.instructionsList) RecyclerView instructionsListView;
  private Recipe recipe;


  public static InstructionsFragment newInstance(Recipe recipe) {
    InstructionsFragment instructionsFragment = new InstructionsFragment();
    Bundle args = new Bundle();
    args.putParcelable("recipe", Parcels.wrap(recipe));
    instructionsFragment.setArguments(args);
    return instructionsFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recipe = Parcels.unwrap(getArguments().getParcelable("recipe"));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_instructions, container, false);
    ButterKnife.bind(this, view);
    instructionsListView.setAdapter(new InstructionsAdapter(recipe.getFullInstructions().get(0)));
    instructionsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
    instructionsListView.setHasFixedSize(true);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
