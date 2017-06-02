package com.janek.recipebook.models;

import java.util.List;

public class RecipeListResponse {
  private List<RecipeList> results;
  private String baseUri;
  private int offset;
  private int number;
  private int totalResults;
  private int processingTimeMs;
  private long expires;
  private boolean isStale;

  public RecipeListResponse(List<RecipeList> results, String baseUri, int offset, int number, int totalResults, int processingTimeMs, long expires, boolean isStale) {
    this.results = results;
    this.baseUri = baseUri;
    this.offset = offset;
    this.number = number;
    this.totalResults = totalResults;
    this.processingTimeMs = processingTimeMs;
    this.expires = expires;
    this.isStale = isStale;
  }

  public List<RecipeList> getResults() {
    return results;
  }

  public String getBaseUri() {
    return baseUri;
  }

  public int getOffset() {
    return offset;
  }

  public int getNumber() {
    return number;
  }

  public int getTotalResults() {
    return totalResults;
  }

  public int getProcessingTimeMs() {
    return processingTimeMs;
  }

  public long getExpires() {
    return expires;
  }

  public boolean isStale() {
    return isStale;
  }
}
