package com.janek.recipebook.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.janek.recipebook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.emailEditText) EditText emailEditText;
    @BindView(R.id.passwordEditText) EditText passwordEditText;
    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.createAccountButton) Button createAccountButton;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private FirebaseAuth mAuth;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginButton.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        createAuthProgressDialog();

        Observable<CharSequence> emailObservable = RxTextView.textChanges(emailEditText).skipInitialValue();
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(passwordEditText).skipInitialValue();

        disposable.add(Observable.combineLatest(emailObservable, passwordObservable, new BiFunction<CharSequence, CharSequence, Boolean>() {
            @Override public Boolean apply(@NonNull CharSequence emailInput, @NonNull CharSequence passwordInput) throws Exception {

                boolean validEmail = (emailInput != null && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches());
                if (!validEmail) emailEditText.setError("Please enter a valid email address");

                boolean validPassword = passwordInput.length() > 6;
                if (!validPassword) passwordEditText.setError("Please enter a password containing at least 6 characters");

                return validEmail && validPassword;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override public void accept(@NonNull Boolean valid) throws Exception {
                loginButton.setEnabled(valid);
            }
        }));

        disposable.add(RxView.clicks(loginButton).subscribe(new Consumer<Object>() {
            @Override public void accept(@NonNull Object o) throws Exception {
                // login and move to main activity
            }
        }));

        disposable.add(RxView.clicks(createAccountButton).subscribe(new Consumer<Object>() {
            @Override public void accept(@NonNull Object o) throws Exception {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    public void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setMessage("Logging in...");
        mAuthProgressDialog.setCancelable(false);
    }
}
