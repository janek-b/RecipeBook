package com.janek.recipebook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.Instruction;
import com.janek.recipebook.models.Step;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.InstructionsViewHolder> {
  private List<Step> steps;

  public InstructionsAdapter(Instruction instruction) {
    steps = instruction.getSteps();
  }

  @Override
  public InstructionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_step, parent, false);
    InstructionsViewHolder viewHolder = new InstructionsViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(InstructionsViewHolder holder, int position) {
    holder.bindInstructions(steps.get(position));
  }

  @Override
  public int getItemCount() {
    return steps.size();
  }

  public class InstructionsViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.step_number) TextView stepNumberTextView;
    @Bind(R.id.step_desc) TextView stepDescTextView;

    private Context mContext;

    public InstructionsViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mContext = itemView.getContext();
    }

    public void bindInstructions(Step step) {
      stepNumberTextView.setText(String.format("%d", step.getNumber()));
      stepDescTextView.setText(step.getStep());
    }
  }
}
