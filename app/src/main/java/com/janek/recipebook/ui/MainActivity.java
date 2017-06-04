package com.janek.recipebook.ui;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
//import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.RecipeListResponse;
import com.janek.recipebook.services.SpoonClient;
import com.janek.recipebook.services.SpoonService;
import com.squareup.picasso.Picasso;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private static final String BACK_STACK_ROOT_TAG = "root_fragment";
  private static final int MAX_WIDTH = 400;
  private static final int MAX_HEIGHT = 300;
  private ProgressDialog loading;

  @Bind(R.id.recipe_img_backdrop) ImageView recipeImgBackdrop;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.drawer_layout) DrawerLayout drawer;
  @Bind(R.id.nav_view) NavigationView navigationView;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    Typeface raleway = Typeface.createFromAsset(getAssets(), "fonts/raleway-regular.ttf");

    collapsingToolbar.setCollapsedTitleTypeface(raleway);
    collapsingToolbar.setExpandedTitleTypeface(raleway);
//    collapsingToolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);
    loading = new ProgressDialog(MainActivity.this);
    loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    loading.setIndeterminate(true);

    setSupportActionBar(toolbar);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();
    navigationView.setNavigationItemSelectedListener(this);
    disableCollapse();

    // Listen for fragment changes and update selected nav item
    getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
      @Override
      public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof HomeFragment) {
          disableCollapse();
          navigationView.setCheckedItem(R.id.nav_home);
        } else if (fragment instanceof AboutFragment) {
          disableCollapse();
          navigationView.setCheckedItem(R.id.nav_about);
        } else if (fragment instanceof RecipeDetailFragment) {
          enableCollapse();
        } else {
          disableCollapse();
        }
      }
    });
    // Initialize with HomeFragment fragment
    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
  }


  public void runSearch(String search) {
    loading.setMessage(String.format("Searching for %s recipes...", search));
    loading.show();

    SpoonClient spoonClient = SpoonService.createService(SpoonClient.class);
    Call<RecipeListResponse> call = spoonClient.searchRecipes(search);
    call.enqueue(new Callback<RecipeListResponse>() {
      @Override public void onResponse(Call<RecipeListResponse> call, retrofit2.Response<RecipeListResponse> response) {
        loadNavFragment(RecipeListFragment.newInstance(response.body()));
        loading.dismiss();
      }
      @Override public void onFailure(Call<RecipeListResponse> call, Throwable t) {
        t.printStackTrace();
        //TODO show error message
      }
    });
  }

  public void getRecipe(int id) {
    loading.setMessage("Getting recipe Details...");
    loading.show();

    SpoonClient spoonClient = SpoonService.createService(SpoonClient.class);
    Call<Recipe> call = spoonClient.getRecipe(id);
    call.enqueue(new Callback<Recipe>() {
      @Override
      public void onResponse(Call<Recipe> call, retrofit2.Response<Recipe> response) {
        loadFragment(RecipeDetailFragment.newInstance(response.body()));
        loading.dismiss();
      }
      @Override public void onFailure(Call<Recipe> call, Throwable t) {t.printStackTrace();}
    });
  }


  public void disableCollapse() {
    recipeImgBackdrop.setVisibility(View.GONE);
//    recipeTitleContainer.setVisibility(View.GONE);
    collapsingToolbar.setTitleEnabled(false);
  }

  public void enableCollapse() {
    recipeImgBackdrop.setVisibility(View.VISIBLE);
//    recipeTitleContainer.setVisibility(View.VISIBLE);
    collapsingToolbar.setTitleEnabled(true);
  }


  public void setBackdropImg(String url) {
    Picasso.with(this).load(url).resize(MAX_WIDTH, MAX_HEIGHT).centerCrop().into(recipeImgBackdrop);
  }

  public void setRecipeTitle(String title) {
    collapsingToolbar.setTitle(title);
  }


  @Override
  public void onBackPressed() {
//    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

// Tool bar right menu items handled here. Add search bar here to make it available on all pages.
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }



  public void loadNavFragment(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .replace(R.id.content_frame, fragment)
        .addToBackStack(BACK_STACK_ROOT_TAG).commit();
  }

  public void loadFragment(Fragment fragment) {
    FragmentManager fragmentManger = getSupportFragmentManager();
    fragmentManger.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.nav_home:
        loadNavFragment(new HomeFragment());
        break;
      case R.id.nav_about:
        loadNavFragment(new AboutFragment());
        break;
      default:
        loadNavFragment(new HomeFragment());
    }
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

}
