package com.janek.recipebook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.janek.recipebook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.emailEditText) EditText emailEditText;
    @BindView(R.id.passwordEditText) EditText passwordEditText;
    @BindView(R.id.signUpButton) Button loginButton;
    @BindView(R.id.createAccountButton) Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
}
