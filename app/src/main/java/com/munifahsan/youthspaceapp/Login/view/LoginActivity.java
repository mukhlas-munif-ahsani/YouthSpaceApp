package com.munifahsan.youthspaceapp.Login.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.munifahsan.youthspaceapp.Login.pres.LoginPres;
import com.munifahsan.youthspaceapp.Login.pres.LoginPresInt;
import com.munifahsan.youthspaceapp.MainActivity;
import com.munifahsan.youthspaceapp.R;
import com.munifahsan.youthspaceapp.Register.view.RegisterActivity;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginViewInt {

    private static final int RC_SIGN_IN = 120;

    @BindView(R.id.progressBar_login)
    ProgressBar mProgress;
    @BindView(R.id.editTextEmail)
    TextInputEditText mEmail;
    @BindView(R.id.editTextPass)
    TextInputEditText mPass;
    @BindView(R.id.textInputEmail)
    TextInputLayout mEmailLayout;
    @BindView(R.id.textInputPass)
    TextInputLayout mPassLayout;
    @BindView(R.id.login_button)
    LoginButton loginButton;

    private LoginPresInt mLoginPresInt;

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mLoginPresInt = new LoginPres(this);
        mLoginPresInt.onCreate();

         /*
        Hide app toolbar
         */
        //getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLoginPresInt.doFacebookSignIn(loginResult.getAccessToken());
//                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                showMessage("Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                showMessage(error.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresInt.onDestroy();
    }

    /*Unused*/
    private void handleFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    showMessage("facebook signin SUCCESS");
                    navigateToMain();
                } else {
                    showMessage(task.getException().toString());
                }
            }
        });
    }

    @OnClick(R.id.textView_belum_memiliki_akun)
    public void buttonToRegisterOnClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_login)
    public void doEmailLogin(){
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if (mLoginPresInt.isValidForm(email, pass)){
            mLoginPresInt.doEmailLogin(email, pass);
        }
    }

    @OnClick(R.id.button_google)
    public void doGoogleSignIn(){
        showProgress();
        setInputEnabled(false);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.button_facebook)
    public void doFacebookSignIn(){
        showProgress();
        setInputEnabled(false);

        loginButton.performClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //String ex = task.getException().toString();
            if (task.isSuccessful()){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("googleSignUp", "firebaseAuthWithGoogle:" + account.getId());
                    mLoginPresInt.doGoogleSignIn(account.getIdToken());
                    //firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("googleSignUp", "Google sign in failed", e);
                    // ...
                }
            } else {
                Log.w("googleSignUp", "ex");
            }
        }
    }

    @Override
    public void showProgress(){
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress(){
        mProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setEmailLayoutError(String msg){
        mEmailLayout.setError(msg);
    }

    @Override
    public void setPassLayoutError(String msg){
        mPassLayout.setError(msg);
    }

    @Override
    public void setInputEnabled(Boolean enabled){
        mEmail.setEnabled(enabled);
        mPass.setEnabled(enabled);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}