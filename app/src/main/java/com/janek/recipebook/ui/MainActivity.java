package com.janek.recipebook.ui;

import android.os.Bundle;
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

import com.janek.recipebook.Constants;
import com.janek.recipebook.R;
import com.janek.recipebook.models.RecipeList;
import com.janek.recipebook.services.SpoonClient;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private static final String BACK_STACK_ROOT_TAG = "root_fragment";

  private Retrofit retrofit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    // Initialize with HomeFragment fragment
    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();

    // Listen for fragment changes and update selected nav item
    getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
      @Override
      public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        int id;
        if (fragment instanceof HomeFragment) {
          id = R.id.nav_home;
        } else if (fragment instanceof AboutFragment) {
          id = R.id.nav_about;
        } else if (fragment instanceof RecipeListFragment) {
          id = R.id.nav_recipe_list;
        } else {
          return;
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(id);
      }
    });

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.addInterceptor(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
            .header("X-Mashape-Key", Constants.SPOON_KEY)
            .header("Accept", "application/json")
            .method(original.method(), original.body())
            .build();

        return chain.proceed(request);
      }
    });
    OkHttpClient client = httpClient.build();

    retrofit = new Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client).build();
  }

  public void runSearch(String search) {
    SpoonClient client = retrofit.create(SpoonClient.class);
    Call<ResponseBody> call = client.searchRecipes(true, search);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        try {
          Log.d("test", response.body().string());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        t.printStackTrace();
      }
    });
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
      case R.id.nav_recipe_list:
        loadNavFragment(new RecipeListFragment());
        break;
      default:
        loadNavFragment(new HomeFragment());
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

}
