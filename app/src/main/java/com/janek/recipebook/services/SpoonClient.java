package com.janek.recipebook.services;

import com.janek.recipebook.models.RecipeListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface SpoonClient {
  @GET("/recipes/search")
  Call<RecipeListResponse> searchRecipes(@Query("instructionsRequired") boolean instructions, @Query("query") String query);
}
