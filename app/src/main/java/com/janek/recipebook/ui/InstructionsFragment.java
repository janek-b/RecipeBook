package com.janek.recipebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janek.recipebook.R;
import com.janek.recipebook.adapters.InstructionsAdapter;
import com.janek.recipebook.models.Instruction;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.Step;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InstructionsFragment extends Fragment {
  @BindView(R.id.instructionsList) RecyclerView instructionsListView;
  private Unbinder unbinder;
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
    unbinder = ButterKnife.bind(this, view);
    instructionsListView.setAdapter(new InstructionsAdapter(flattenInstructions(recipe)));
    instructionsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
    instructionsListView.setHasFixedSize(true);
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
  }

  private List<Object> flattenInstructions(Recipe recipe) {
    List<Object> flat = new ArrayList<>();
    for (Instruction instruction : recipe.getFullInstructions()) {
      flat.add(instruction.getName().equals("") ? recipe.getTitle() : instruction.getName());
      for (Step step : instruction.getSteps()) {
        flat.add(step);
      }
    }
    return flat;
  }
}
