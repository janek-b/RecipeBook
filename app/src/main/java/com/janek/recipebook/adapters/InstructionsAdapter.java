package com.janek.recipebook.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.Instruction;
import com.janek.recipebook.models.Item;
import com.janek.recipebook.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstructionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<Object> steps;

  public InstructionsAdapter(List<Object> instruction) {
    steps = instruction;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case 1: return new InstructionTitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_header, parent, false));
      default: return new InstructionsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_step, parent, false));
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case 0:
        InstructionsViewHolder stepViewHolder = (InstructionsViewHolder) holder;
        stepViewHolder.bindInstructions((Step)steps.get(position));
        break;
      case 1:
        InstructionTitleViewHolder titleViewHolder = (InstructionTitleViewHolder) holder;
        titleViewHolder.bindTitle((String)steps.get(position));
        break;
    }
  }

  @Override
  public int getItemViewType(int position) {
    return steps.get(position) instanceof String ? 1 : 0;
  }

  @Override
  public int getItemCount() {
    return steps.size();
  }

  public class InstructionsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.step_number) TextView stepNumberTextView;
    @BindView(R.id.step_desc) TextView stepDescTextView;
    @BindView(R.id.ingredients_instructions_label) TextView ingredientLabel;
    @BindView(R.id.step_layout) LinearLayout stepLayout;

    private Context mContext;

    public InstructionsViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mContext = itemView.getContext();
    }

    public void bindInstructions(Step step) {
      stepNumberTextView.setText(String.format("%d", step.getNumber()));
      stepDescTextView.setText(step.getStep());

      if (step.getIngredients().size() > 0) ingredientLabel.setVisibility(View.VISIBLE);
      else ingredientLabel.setVisibility(View.GONE);

      for (Item ingredient : step.getIngredients()) {
        TextView ingredientView = new TextView(mContext);
        ingredientView.setText(ingredient.getName());
        ingredientView.setTextColor(ContextCompat.getColor(mContext, R.color.colorTextIcons));
        stepLayout.addView(ingredientView);
      }
    }
  }

  public class InstructionTitleViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.instruction_title) TextView instructionTitleTextView;

    private Context mContext;

    public InstructionTitleViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mContext = itemView.getContext();
    }

    public void bindTitle(String title) {
      instructionTitleTextView.setText(title);
    }
  }
}
