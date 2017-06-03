package com.janek.recipebook.services;

import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.RecipeListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface SpoonClient {
  @GET("/recipes/search?instructionsRequired=true")
  Call<RecipeListResponse> searchRecipes(@Query("query") String query);

  @GET("/recipes/{recipeId}/information?includeNutrition=false")
  Call<Recipe> getRecipe(@Path("recipeId") int recipeId);

  //TODO add api call for analyzed instructions

  //TODO add random joke api call
}
