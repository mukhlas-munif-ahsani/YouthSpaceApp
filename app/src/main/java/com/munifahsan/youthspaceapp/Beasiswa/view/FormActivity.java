package com.munifahsan.youthspaceapp.Beasiswa.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.munifahsan.youthspaceapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormActivity extends AppCompatActivity {

    @BindView(R.id.webView_form)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String formUrl = intent.getStringExtra("FORM_LINK");

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(intent.getStringExtra("FORM_LINK"));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.floatingActionButton_back_form)
    public void onBackButtonClick(){
        finish();
    }
}