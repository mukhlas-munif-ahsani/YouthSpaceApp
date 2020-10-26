package com.munifahsan.youthspaceapp.Register.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.munifahsan.youthspaceapp.MainActivity;
import com.munifahsan.youthspaceapp.R;
import com.munifahsan.youthspaceapp.Register.pres.RegisterPres;
import com.munifahsan.youthspaceapp.Register.pres.RegisterPresInt;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterViewInt{

    private static final int RC_SIGN_IN = 120;

    @BindView(R.id.progressBar_register)
    ProgressBar mProgress;
    @BindView(R.id.editText_email)
    TextInputEditText mEmail;
    @BindView(R.id.editText_pass)
    TextInputEditText mPass;
    @BindView(R.id.editTextConfirm_pass)
    TextInputEditText mConfirmPass;
    @BindView(R.id.button_facebook)
    MaterialButton mButtonFacebook;
    @BindView(R.id.login_button)
    LoginButton loginButton;

    private RegisterPresInt mRegisterPresInt;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private GoogleSignInClient googleSignInClient;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        mRegisterPresInt = new RegisterPres(this);
        mRegisterPresInt.onCreate();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        /*Google*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        /*Facebook*/
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mRegisterPresInt.doFacebookSignIn(loginResult.getAccessToken());
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

    /*Unused*/
    private void handleFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //showMessage("facebook signin SUCCESS");
                    navigateToMain();
                } else {
                    showMessage(task.getException().toString());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mRegisterPresInt.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.button_daftar)
    public void doEmailRegister() {
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();
        String confirmPass = mConfirmPass.getText().toString();

        if (mRegisterPresInt.isValidForm(email, pass, confirmPass)){
            mRegisterPresInt.doEmailRegister(email, pass, confirmPass);
        }

    }

    @OnClick({R.id.button_google})
    public void doGoogleRegister(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    //* unused*//
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("googleSignUp", "signInWithCredential:success");
                            navigateToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("googleSignUp", "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }

    @OnClick(R.id.button_facebook)
    public void doFacebookRegister(){
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
                    showProgress();
                    mRegisterPresInt.doGoogleSignIn(account.getIdToken());
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

    @OnClick(R.id.textIView_sudah_memiliki_akun)
    public void sudahPunyaAkunOnClick(){
        finish();
    }

    @Override
    public void setEmailLayoutError(String msg){
        mEmail.setError(msg);
    }

    @Override
    public void setPassLayoutError(String msg){
        mPass.setError(msg);
    }

    @Override
    public void setConfirmPassLayoutError(String msg){
        mConfirmPass.setError(msg);
    }

    @Override
    public void setInputEnabled(Boolean enabeled){
        mEmail.setEnabled(enabeled);
        mPass.setEnabled(enabeled);
        mConfirmPass.setEnabled(enabeled);
    }

    @Override
    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMain(){
//        showMessage(mCurrentUser.getDisplayName());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showProgress(){
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress(){
        mProgress.setVisibility(View.INVISIBLE);
    }
}