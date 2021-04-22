package com.projeto.biblianvi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.projeto.biblianvi.biblianvi.R;

import java.util.Locale;

public class Sermoes extends Activity {


    static String SITE;
    PopupWindow pwindo;
    View layoutPop;
    private WebView myWebView;
    private String siteNome = "biblianvi";
    private String googleDriveHost = "drive.google.com";
    private Context esteContext;
    private WebView mWebviewPop;
    private FrameLayout mContainer;
    private TextView textViewDeveloperM;
    private String language;
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_mensagem);

        language = Locale.getDefault().getLanguage();


        // Makes Progress bar Visible
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        mContainer = findViewById(R.id.frameLayoutWeb);
        myWebView = findViewById(R.id.webViewBrowser);
        textViewDeveloperM = findViewById(R.id.textViewDeveloper);

        textViewDeveloperM.setText(MainActivity.VERSIONAPP);

        esteContext = this.getApplicationContext();


        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        /*
        if (language.equals("es")) {
            SITE = "https://asambleas.net/category/sermones/";
        } else if (language.equals("pt")) {
            SITE = "http://marcasdoevangelho.com.br/sermons/";
        } else {
            SITE = "https://www.biblestudytools.com/sermons/";

        }
         */
        SITE = "https://www.sermonaudio.com/sermonslanguage.asp";


        myWebView.setBackgroundColor(Color.TRANSPARENT);
        myWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setWebChromeClient(new CustomChromeClient());
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(SITE);
        //myWebView.loadData(SITE, "text/html; charset=UTF-8", null);


        WebSettings settings = myWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);

    }

    protected void onResume() {
        super.onResume();

        //requestNewInterstitial();
        propaganda();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_devocional:
                Intent in = icon_new Intent(getApplicationContext(),NetworkActivityDevocional.class);
                startActivity(in);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
            */
        return super.onOptionsItemSelected(item);

    }

    public void onBackPressed() {

        if (myWebView.getUrl().equals(SITE)) {
            myWebView.pauseTimers();
            super.onBackPressed();
            return;

        } else {
            myWebView.loadUrl(SITE);
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void propaganda() {


        LinearLayout myLayoutBase = findViewById(R.id.linearMobDevo);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLayoutBase.getLayoutParams();

        AdView mAdView = findViewById(R.id.adViewBrowser);

        if (isNetworkAvailable() && (mAdView != null)) {

            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            mAdView.setAdListener(new AdListener() {

                public void onAdFailedToLoad(int errorCode) {

                    switch (errorCode) {

                        case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                            Log.e("ADMOB ERRO:", "ERROR_CODE_INTERNAL_ERROR");
                            break;
                        case AdRequest.ERROR_CODE_INVALID_REQUEST:
                            Log.e("ADMOB ERRO:", "ERROR_CODE_INVALID_REQUEST");
                            break;
                        case AdRequest.ERROR_CODE_NETWORK_ERROR:
                            Log.e("ADMOB ERRO:", "ERROR_CODE_NETWORK_ERROR");
                            break;
                        case AdRequest.ERROR_CODE_NO_FILL:
                            Log.e("ADMOB ERRO:", "ERROR_CODE_NO_FILL");
                            break;
                        default:
                            Log.e("ADMOB ERRO:", "NENHUM_ERRO");
                            break;
                    }
                }

            });


            //propaganda Google
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        } else {

            myLayoutBase.setBackgroundResource(R.drawable.banner_logo);
            //params.height = 0;


        }


    }

    private void initiatePopupWindow() {
        try {


// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) Sermoes.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutPop = inflater.inflate(R.layout.screen_popup,
                    (ViewGroup) findViewById(R.id.popup_element), false);

            pwindo = new PopupWindow(layoutPop, 300, 370, true);
            pwindo.showAtLocation(layoutPop, Gravity.CENTER, 0, 0);


            Button btnClosePopup = layoutPop.findViewById(R.id.btn_close_popup);
            btnClosePopup.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class UriWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();
            myWebView.loadUrl(url);
            return false;
            /*
            if (host.contains(siteNome) || host.contains(googleDriveHost))
            {
                myWebView.loadUrl(url);

                // This is my web site, so do not override; let my WebView load
                // the page
                if(mWebviewPop!=null)
                {
                    mWebviewPop.setVisibility(View.GONE);
                    mContainer.removeView(mWebviewPop);
                    mWebviewPop=null;
                }


                return false;
            }

            if(host.equals("m.facebook.com") || host.equals("www.facebook.com"))
            {
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
             */
        }


        public void onPageFinished(WebView view, String url) {

            if (url.startsWith("https://m.facebook.com/plugins/close_popup.php?") ||
                    url.startsWith("https://www.facebook.com/plugins/close_popup.php?")) {

                if (mWebviewPop != null) {

                    Log.e("Face", url);
                    mWebviewPop.setVisibility(View.GONE);
                    mContainer.removeView(mWebviewPop);
                    mWebviewPop = null;
                }


                return;
            }

            super.onPageFinished(view, url);
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            //super.onReceivedSslError(view, handler, error);
        }
    }


    class CustomChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(esteContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new UriWebViewClient());
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setSavePassword(false);
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            mContainer.addView(mWebviewPop);

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();


            return true;
        }


        @Override
        public void onCloseWindow(WebView window) {


            Log.d("onCloseWindow", "called");
        }

    }


}
