package com.janek.recipebook.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.janek.recipebook.Constants;
import com.janek.recipebook.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;
import io.reactivex.observers.DisposableObserver;

public class CreateAccountActivity extends AppCompatActivity {
    @BindView(R.id.nameEditText) EditText nameEditText;
    @BindView(R.id.emailEditText) EditText emailEditText;
    @BindView(R.id.passwordEditText) EditText passwordEditText;
    @BindView(R.id.confirmEditText) EditText confirmEditText;
    @BindView(R.id.signUpButton) Button signUpButton;
    @BindView(R.id.loginButton) Button loginButton;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private FirebaseAuth mAuth;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
        signUpButton.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();

        createAuthProgressDialog();

        Observable<CharSequence> nameObservable = RxTextView.textChanges(nameEditText).skipInitialValue();
        Observable<CharSequence> emailObservable = RxTextView.textChanges(emailEditText).skipInitialValue();
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(passwordEditText).skipInitialValue();
        Observable<CharSequence> confirmObservable = RxTextView.textChanges(confirmEditText).skipInitialValue();

        disposable.add(Observable.combineLatest(nameObservable, emailObservable, passwordObservable, confirmObservable, new Function4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence nameInput, @NonNull CharSequence emailInput, @NonNull CharSequence passwordInput, @NonNull CharSequence confirmInput) throws Exception {
                boolean goodName = isValidName(nameInput.toString().trim());
                boolean goodEmail = isValidEmail(emailInput.toString().trim());
                boolean goodPassword = isValidPassword(passwordInput.toString().trim(), confirmInput.toString().trim());
                return goodName && goodEmail && goodPassword;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override public void accept(@NonNull Boolean valid) throws Exception {
                signUpButton.setEnabled(valid);
            }
        }));


        disposable.add(RxView.clicks(signUpButton).subscribe(new Consumer<Object>() {
            @Override public void accept(@NonNull Object o) throws Exception {
                createNewUser();
            }
        }));

        disposable.add(RxView.clicks(loginButton).subscribe(new Consumer<Object>() {
            @Override public void accept(@NonNull Object o) throws Exception {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
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
        mAuthProgressDialog.setMessage("Creating new account...");
        mAuthProgressDialog.setCancelable(false);
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) emailEditText.setError("Please enter a valid email address");
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        boolean isGoodName = !TextUtils.isEmpty(name);
        if (!isGoodName) nameEditText.setError("Please enter your name");
        return isGoodName;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            passwordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            passwordEditText.setError("Passwords do not match");
            return false;
        } else passwordEditText.setError(null);
        return true;
    }

    private void createNewUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        mAuthProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@android.support.annotation.NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    updateUserProfile(task.getResult().getUser());
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserProfile(final FirebaseUser user) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameEditText.getText().toString().trim())
                .setPhotoUri(Uri.parse(Constants.DEFAULT_IMG_URL))
                .build();

        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mAuthProgressDialog.dismiss();
                    Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}
