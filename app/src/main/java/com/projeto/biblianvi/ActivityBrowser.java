package com.projeto.biblianvi;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.projeto.biblianvi.biblianvi.R;

public class ActivityBrowser extends Activity {

    private WebView myWebView;
    private String url;
    private AdView mAdView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_browser);

        intent = getIntent();
        myWebView = findViewById(R.id.webViewBrowser);
        url = intent.getStringExtra("url");

        mAdView = findViewById(R.id.adViewBrowser);
        final AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdView.loadAd(adRequest);
            }
        }, 500);
        Log.e("url", url);
        webView();

    }

    public void onBackPressed() {

        if (intent.getStringExtra("url").equals(myWebView.getUrl()))
            super.onBackPressed();
        else
            myWebView.loadUrl(intent.getStringExtra("url"));
    }

    private void webView() {

        myWebView.getSettings().setJavaScriptEnabled(true);

        WebSettings settings = myWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        myWebView.setWebViewClient(new CustomWebViewClient());
        myWebView.loadUrl(url);

    }

    class CustomWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlC) {
            view.loadUrl(urlC);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            String summary = "<html><body>Request error: <b>" + Integer.toString(error.getErrorCode()) + "</b>" +
                    "<b>" + error.toString() + "</b></body></html>";
            myWebView.loadData(summary, "text/html", null);


        }

    }


}
