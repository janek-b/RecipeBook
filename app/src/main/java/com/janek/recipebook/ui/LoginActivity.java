package com.janek.recipebook.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginButton.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        createAuthProgressDialog();

        mAuthListener = (firebaseAuth) -> {
            if (firebaseAuth.getCurrentUser() != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Observable<CharSequence> emailObservable = RxTextView.textChanges(emailEditText).skipInitialValue();
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(passwordEditText).skipInitialValue();

        disposable.add(Observable.combineLatest(emailObservable, passwordObservable, (CharSequence emailInput, CharSequence passwordInput) -> {
            boolean validEmail = (emailInput != null && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches());
            if (!validEmail) emailEditText.setError("Please enter a valid email address");

            boolean validPassword = passwordInput.length() > 6;
            if (!validPassword) passwordEditText.setError("Please enter a password containing at least 6 characters");

            return validEmail && validPassword;
        }).subscribe(valid -> loginButton.setEnabled(valid)));

        disposable.add(RxView.clicks(loginButton).subscribe(event -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            mAuthProgressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                mAuthProgressDialog.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }));

        disposable.add(RxView.clicks(createAccountButton).subscribe(event -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setMessage("Logging in...");
        mAuthProgressDialog.setCancelable(false);
    }
}
