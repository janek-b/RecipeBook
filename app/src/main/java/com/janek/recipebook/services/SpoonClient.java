package com.janek.recipebook.services;

import com.janek.recipebook.models.RecipeList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface SpoonClient {
  @GET("/recipes/search")
  Call<ResponseBody> searchRecipes(@Query("instructionsRequired") boolean instructions, @Query("query") String query);
}
