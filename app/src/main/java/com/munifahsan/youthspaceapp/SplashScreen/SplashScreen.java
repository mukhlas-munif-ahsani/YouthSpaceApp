package com.munifahsan.youthspaceapp.SplashScreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;
    Handler handler = new Handler();


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

        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        Filter whether the user is logged in or not
         */
        if (isConnected){
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
                }, 3000);

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
        } else {
            showDialogInternetIssue();
        }
    }

    public void showDialogInternetIssue() {
        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title dialog
        alertDialogBuilder.setTitle("Mohon Periksa Koneksi Internet anda !!");

        // set pesan dari dialog
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Coba Lagi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan menutup activity ini

                        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                check();
                            }
                        }, 2000);
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

    }

    public void check(){
        if (isConnected){
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
                }, 3000);

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
        } else {
            showDialogInternetIssue();
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