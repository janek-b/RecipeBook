package com.janek.recipebook.services;

import com.janek.recipebook.models.Instruction;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.RecipeListResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface SpoonClient {
  @GET("/recipes/search?instructionsRequired=true")
  Observable<RecipeListResponse> searchRecipes(@Query("query") String query);

  @GET("/recipes/{recipeId}/information?includeNutrition=false")
  Observable<Recipe> getRecipe(@Path("recipeId") int recipeId);

  @GET("/recipes/{recipeId}/analyzedInstructions?stepBreakdown=true")
  Observable<List<Instruction>> getInstructions(@Path("recipeId") int recipeId);

  //TODO add random joke api call
}
