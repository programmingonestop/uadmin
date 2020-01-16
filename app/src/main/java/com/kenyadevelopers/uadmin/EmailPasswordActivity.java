package com.kenyadevelopers.uadmin;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends AppCompatActivity
{
    private static final String TAG="UserLoginManager";
    private FirebaseAuth mAuth;
    private AppCompatButton btnLogin;
    private EditText emailEd;
    private EditText passwordEd;
    private String email;
    private String password;
    private String btnLoginState="Login";
    private TextView linkSignUp;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    public void createAccount(String email,String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void signIn(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser currentUser)
    {
    }
    public void userInfor()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        mAuth = FirebaseAuth.getInstance();
        findViews();
        attachListenersToViews();
    }
    private void findViews()
    {
        btnLogin=(AppCompatButton)findViewById(R.id.btn_login);

        emailEd=(EditText)findViewById(R.id.input_email);
        passwordEd=(EditText)findViewById(R.id.input_password);
        linkSignUp = (TextView) findViewById(R.id.link_signup);
    }
    private void attachListenersToViews()
    {
        if (btnLogin !=null)
        {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(btnLoginState.equals("Login"))
                    {
                        signIn(email,password);
                    }
                    else
                    {
                        createAccount(email,password);
                    }
                }
            });
        }
        if(passwordEd!=null)
        {
            passwordEd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    password=s.toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        if(emailEd!=null)
        {
            emailEd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    email=s.toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        if(linkSignUp!=null)
        {
            linkSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btnLoginState.equals("Login"))
                    {
                        btnLogin.setText("CREATE ACCOUNT");
                        btnLoginState="CreateAccount";
                        linkSignUp.setText("Have an Account?Login");
                    }
                    else
                    {
                        btnLogin.setText("LOGIN");
                        btnLoginState="Login";
                        linkSignUp.setText("No account yet? Create one");
                    }
                }
            });
        }

    }

}
