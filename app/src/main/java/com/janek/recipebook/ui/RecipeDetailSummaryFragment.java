package com.janek.recipebook.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.janek.recipebook.Constants;
import com.janek.recipebook.R;
import com.janek.recipebook.models.Ingredient;
import com.janek.recipebook.models.Recipe;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class RecipeDetailSummaryFragment extends Fragment {
    @BindView(R.id.recipe_detail_cook_time) TextView cookTimeTextView;
    @BindView(R.id.recipe_detail_servings) TextView servingsTextView;
    @BindView(R.id.ingredients_label) TextView ingredientsLabel;
    @BindView(R.id.websiteTextView) TextView websiteTextView;
    @BindView(R.id.dairyFreeIcon) ImageView dairyFreeIcon;
    @BindView(R.id.glutenFreeIcon) ImageView glutenFreeIcon;
    @BindView(R.id.veganIcon) ImageView veganIcon;
    @BindView(R.id.vegetarianIcon) ImageView vegetarianIcon;
    @BindView(R.id.ingredient_list_layout) LinearLayout ingredientListLayout;
    @BindView(R.id.savedButton) ToggleButton savedButton;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private Unbinder unbinder;
    private Recipe recipe;
    private boolean userSaved;
    private DatabaseReference rootRef;

    public static RecipeDetailSummaryFragment newInstance(Recipe recipe, boolean userSaved) {
        RecipeDetailSummaryFragment recipeDetailSummaryFragment = new RecipeDetailSummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable("recipe", Parcels.wrap(recipe));
        args.putBoolean("userSaved", userSaved);
        recipeDetailSummaryFragment.setArguments(args);
        return recipeDetailSummaryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        recipe = Parcels.unwrap(args.getParcelable("recipe"));
        userSaved = args.getBoolean("userSaved");
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail_summary, container, false);
        unbinder = ButterKnife.bind(this, view);
        Typeface raleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raleway-regular.ttf");
        cookTimeTextView.setTypeface(raleway);
        servingsTextView.setTypeface(raleway);
        ingredientsLabel.setTypeface(raleway);
        websiteTextView.setTypeface(raleway);
        setVisibility(dairyFreeIcon, recipe.isDairyFree());
        setVisibility(glutenFreeIcon, recipe.isGlutenFree());
        setVisibility(veganIcon, recipe.isVegan());
        setVisibility(vegetarianIcon, recipe.isVegetarian());

        disposable.add(Observable.just(userSaved).subscribe(savedButton::setChecked));
        disposable.add(RxView.clicks(websiteTextView).subscribe(event -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getSourceUrl())))));
        disposable.add(RxCompoundButton.checkedChanges(savedButton).skipInitialValue().subscribe(this::saveRecipe));

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
        cookTimeTextView.setText(String.format("Cook Time: %d minutes", recipe.getCookTime()));
        servingsTextView.setText(String.format("Servings: %d", recipe.getServings()));
        for (Ingredient ingredient : recipe.getIngredients()) {
            TextView ingredientView = new TextView(getContext());
            ingredientView.setText(ingredient.getOriginalString());
            ingredientView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextIcons));
            ingredientView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            ingredientListLayout.addView(ingredientView);
        }
    }

    public void setVisibility(ImageView icon, boolean visible) {
        if (visible) {
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
    }

    public void saveRecipe(final boolean save) {
        final String recipeRef = String.format("%s/%d", Constants.FIREBASE_RECIPE_REF, recipe.getId());
        final String recipeSaveRef = String.format(Constants.FIREBASE_USER_RECIPES_REF, FirebaseAuth.getInstance().getCurrentUser().getUid(), recipe.getId());
        // Add listener to recipe node to see if it already exists before saving it.
        rootRef.child(recipeRef).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {

                Map updates = new HashMap();
                if (!dataSnapshot.exists()) updates.put(recipeRef, recipe);
                updates.put(recipeSaveRef, (save ? save : null));

                rootRef.updateChildren(updates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), save ? "Recipe Saved!" : "Recipe Removed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
