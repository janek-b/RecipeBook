package com.janek.recipebook.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.janek.recipebook.Constants;
import com.janek.recipebook.R;
import com.janek.recipebook.models.Instruction;
import com.janek.recipebook.models.Recipe;
import com.janek.recipebook.models.RecipeListResponse;
import com.janek.recipebook.models.RecipeSearchSuggestion;
import com.janek.recipebook.services.SpoonClient;
import com.janek.recipebook.services.SpoonService;
import com.squareup.picasso.Picasso;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.parceler.Parcels;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;
    private ProgressDialog loading;
    private SharedPreferences mSharedPreferences;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @BindView(R.id.recipe_img_backdrop) ImageView recipeImgBackdrop;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.tabs) TabLayout tabs;

    private View navDrawerHeader;
    private TextView navDrawerTitle;
    private TextView userProfileName;
    private ImageView userProfileImg;

    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private FragmentManager fragmentManager;
    private SpoonClient spoonClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface raleway = Typeface.createFromAsset(getAssets(), "fonts/raleway-regular.ttf");
        // Bind Views
        ButterKnife.bind(this);
        navDrawerHeader = navigationView.getHeaderView(0);
        navDrawerTitle = (TextView) navDrawerHeader.findViewById(R.id.nav_drawer_title);
        userProfileName = (TextView) navDrawerHeader.findViewById(R.id.user_profile_name);
        userProfileImg = (ImageView) navDrawerHeader.findViewById(R.id.user_profile_img);
        // Set Typeface
        toolbarTitle.setTypeface(raleway);
        collapsingToolbar.setCollapsedTitleTypeface(raleway);
        collapsingToolbar.setExpandedTitleTypeface(raleway);
        navDrawerTitle.setTypeface(raleway);
        // Initialize Progress Dialog
        loading = new ProgressDialog(MainActivity.this);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        fragmentManager = getSupportFragmentManager();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        spoonClient = SpoonService.createService(SpoonClient.class);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (fragmentManager.findFragmentById(R.id.content_frame) instanceof  RecipeDetailFragment) {
            enableCollapse();
        } else {
            disableCollapse();
        }

        // Listen for fragment changes and update selected nav item
        fragmentManager.addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment instanceof HomeFragment) {
                disableCollapse();
                navigationView.setCheckedItem(R.id.nav_home);
            } else if (fragment instanceof AboutFragment) {
                disableCollapse();
                navigationView.setCheckedItem(R.id.nav_about);
            } else if (fragment instanceof SavedRecipeListFragment) {
                disableCollapse();
                navigationView.setCheckedItem(R.id.nav_saved_recipes);
            } else if (fragment instanceof RecipeDetailFragment) {
                enableCollapse();
            } else {
                disableCollapse();
            }
        });

        mAuthListener = firebaseAuth -> {
            currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                // update nav header
                userProfileName.setText(currentUser.getDisplayName());
                Picasso.with(MainActivity.this).load(currentUser.getPhotoUrl()).resize(100, 100).centerCrop().into(userProfileImg);
                if (fragmentManager.findFragmentById(R.id.content_frame) == null) {
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
                }
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void runSearch(final String search) {
        String diet = mSharedPreferences.getString(currentUser.getUid(), "any");
        loading.setMessage(String.format("Searching for %s recipes...", search));
        loading.show();
        Observable<RecipeListResponse> searchObservable;
        if (diet.equals("any")) searchObservable = spoonClient.searchRecipes(search);
        else searchObservable = spoonClient.searchDietRecipes(search, diet);

        disposable.add(searchObservable.subscribeWith(new DisposableObserver<RecipeListResponse>() {
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
        disposable.add(Observable.zip(spoonClient.getRecipe(id), spoonClient.getInstructions(id), (Recipe recipe, List<Instruction> instructions) -> {
            recipe.setFullInstructions(instructions);
            return recipe;
        }).subscribeWith(new DisposableObserver<Recipe>() {
            @Override public void onNext(@NonNull final Recipe recipe) {
                rootRef.child(String.format(Constants.FIREBASE_USER_RECIPES_REF, currentUser.getUid(), recipe.getId()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                                loadFragment(RecipeDetailFragment.newInstance(recipe, dataSnapshot.exists()));
                                loading.dismiss();
                            }
                            @Override public void onCancelled(DatabaseError databaseError) {}
                        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        Observable<SearchViewQueryTextEvent> searchObs = RxSearchView.queryTextChangeEvents(searchView).skipInitialValue().share();

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ArrayAdapter<String> suggestionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(suggestionAdapter);
        searchAutoComplete.setDropDownBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimaryLight));

        disposable.add(RxAutoCompleteTextView.itemClickEvents(searchAutoComplete)
                .subscribe(event -> searchView.setQuery(suggestionAdapter.getItem(event.position()), true)));

        disposable.add(searchObs.debounce(400, TimeUnit.MILLISECONDS)
                .map(searchViewQueryTextEvent -> searchViewQueryTextEvent.queryText().toString())
                .subscribeOn(AndroidSchedulers.mainThread())
                .switchMap(spoonClient::getSearchSuggestion)
                .flatMap(suggestions -> Observable.fromIterable(suggestions)
                        .map(RecipeSearchSuggestion::getTitle)
                        .toList()
                        .toObservable()
                ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchInput -> {
                    suggestionAdapter.clear();
                    suggestionAdapter.addAll(searchInput);
                    suggestionAdapter.getFilter().filter(searchAutoComplete.getText(), searchAutoComplete);
                }));

        disposable.add(searchObs.subscribe(searchViewQueryTextEvent -> {
                    if (searchViewQueryTextEvent.isSubmitted()) {
                        runSearch(searchViewQueryTextEvent.queryText().toString());
                        MenuItemCompat.collapseActionView(menuItem);
                    }
                }));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void loadNavFragment(Fragment fragment) {
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, fragment)
                .addToBackStack(BACK_STACK_ROOT_TAG).commit();
    }

    public void loadFragment(Fragment fragment) {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                loadNavFragment(new HomeFragment());
                break;
            case R.id.nav_saved_recipes:
                loadFragment(new SavedRecipeListFragment());
                break;
            case R.id.nav_about:
                loadNavFragment(new AboutFragment());
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
