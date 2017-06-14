package com.janek.recipebook.services;

import com.janek.recipebook.models.Instruction;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.RecipeListResponse;
import com.janek.recipebook.models.RecipeSearchSuggestion;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface SpoonClient {
    @GET("/recipes/search?instructionsRequired=true")
    Observable<RecipeListResponse> searchRecipes(@Query("query") String query);

    @GET("/recipes/search?instructionsRequired=true")
    Observable<RecipeListResponse> searchDietRecipes(@Query("query") String query, @Query("diet") String diet);

    @GET("/recipes/{recipeId}/information?includeNutrition=false")
    Observable<Recipe> getRecipe(@Path("recipeId") int recipeId);

    @GET("/recipes/{recipeId}/analyzedInstructions?stepBreakdown=true")
    Observable<List<Instruction>> getInstructions(@Path("recipeId") int recipeId);

    @GET("/recipes/autocomplete?number=10")
    Observable<List<RecipeSearchSuggestion>> getSearchSuggestion(@Query("query") String query);

    //TODO add random joke api call
}
