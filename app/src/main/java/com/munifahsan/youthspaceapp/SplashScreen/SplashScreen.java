package com.munifahsan.youthspaceapp.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.munifahsan.youthspaceapp.Login.view.LoginActivity;
import com.munifahsan.youthspaceapp.MainActivity;
import com.munifahsan.youthspaceapp.R;

public class SplashScreen extends AppCompatActivity {

    FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        /*
        Change status bar color
         */
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        /*
        Hide app toolbar
         */
        //getActionBar().hide();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        Filter whether the user is logged in or not
         */
        Handler handler = new Handler();
        if (mCurrentUser != null) {
//            showMessage("MAIN ACTIVITY");

            /*
            Set delay 2 sec
             */
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToMain();
                }
            }, 2000);

        } else {
//            showMessage("LOGIN ACTIVITY");

            /*
            Set delay 2 sec
             */
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToLogin();
                }
            }, 2000);
        }

    }

    private void navigateToMain() {
        /*
        Navigate to Main activity
         */
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        /*
        Navigate to Main Login Activity
         */
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}