package com.janek.recipebook.ui;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.janek.recipebook.R;
import com.janek.recipebook.models.Instruction;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.RecipeListResponse;
import com.janek.recipebook.services.SpoonClient;
import com.janek.recipebook.services.SpoonService;
import com.squareup.picasso.Picasso;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private static final String BACK_STACK_ROOT_TAG = "root_fragment";
  private static final int MAX_WIDTH = 400;
  private static final int MAX_HEIGHT = 300;
  private ProgressDialog loading;

  private final CompositeDisposable disposable = new CompositeDisposable();

  @Bind(R.id.recipe_img_backdrop) ImageView recipeImgBackdrop;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.drawer_layout) DrawerLayout drawer;
  @Bind(R.id.nav_view) NavigationView navigationView;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.toolbarTitle) TextView toolbarTitle;
  @Bind(R.id.tabs) TabLayout tabs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    Typeface raleway = Typeface.createFromAsset(getAssets(), "fonts/raleway-regular.ttf");

    toolbarTitle.setTypeface(raleway);
    collapsingToolbar.setCollapsedTitleTypeface(raleway);
    collapsingToolbar.setExpandedTitleTypeface(raleway);
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
    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    disposable.clear();
  }


  public void runSearch(final String search) {
    loading.setMessage(String.format("Searching for %s recipes...", search));
    loading.show();
    SpoonClient spoonClient = SpoonService.createService(SpoonClient.class);
    disposable.add(spoonClient.searchRecipes(search).subscribeWith(new DisposableObserver<RecipeListResponse>() {
      @Override public void onNext(@NonNull RecipeListResponse recipeListResponse) {
        loadNavFragment(RecipeListFragment.newInstance(recipeListResponse, search));
        loading.dismiss();
      }
      @Override public void onError(@NonNull Throwable e) {e.printStackTrace();}
      @Override public void onComplete() {}
    }));
  }

  public void getRecipe(int id) {
    loading.setMessage("Getting recipe Details...");
    loading.show();
    SpoonClient spoonClient = SpoonService.createService(SpoonClient.class);
    disposable.add(Observable.combineLatest(spoonClient.getRecipe(id), spoonClient.getInstructions(id), new BiFunction<Recipe, List<Instruction>, Recipe>() {
      @Override public Recipe apply(@NonNull Recipe recipe, @NonNull List<Instruction> instructions) throws Exception {
        recipe.setFullInstructions(instructions);
        return recipe;
      }
    }).subscribeWith(new DisposableObserver<Recipe>() {
      @Override public void onNext(@NonNull Recipe recipe) {
        loadFragment(RecipeDetailFragment.newInstance(recipe));
        loading.dismiss();
      }
      @Override public void onError(@NonNull Throwable e) {e.printStackTrace();}
      @Override public void onComplete() {}
    }));
  }


  public void disableCollapse() {
    recipeImgBackdrop.setVisibility(View.GONE);
    toolbarTitle.setVisibility(View.VISIBLE);
    tabs.setVisibility(View.GONE);
    collapsingToolbar.setTitleEnabled(false);
  }

  public void enableCollapse() {
    recipeImgBackdrop.setVisibility(View.VISIBLE);
    toolbarTitle.setVisibility(View.GONE);
    tabs.setVisibility(View.VISIBLE);
    collapsingToolbar.setTitleEnabled(true);
  }


  public void setBackdropImg(String url) {
    Picasso.with(this).load(url).resize(MAX_WIDTH, MAX_HEIGHT).centerCrop().into(recipeImgBackdrop);
  }

  public void setRecipeTitle(String title) {
    collapsingToolbar.setTitle(title);
  }

  public void setToolbarTitle(String title) {
    setTitle("");
    toolbarTitle.setText(title);
  }

  public void setTabLayout(ViewPager viewPager) {
    tabs.setupWithViewPager(viewPager);
  }


  @Override
  public void onBackPressed() {
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
    final MenuItem menuItem = menu.findItem(R.id.action_search);

    SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        runSearch(query);
        MenuItemCompat.collapseActionView(menuItem);
        return false;
      }
      @Override public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
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
