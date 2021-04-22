package com.projeto.biblianvi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.projeto.biblianvi.biblianvi.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NetworkActivityDevocional extends Activity {
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL = "http://biblianvi.6te.net/tag.xml";
    // private static final String URL =  "http://biblianvi.byethost17.com/xml/tag.xml";

    //"http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;
    // The user's current network preference setting.
    public static String sPref = null;
    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    WebView myWebView;
    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkReceiver receiver = new NetworkReceiver();
    private ImageView ref;
    private FrameLayout frameLayoutDevoc;
    private WebView webViewPop;
    private Context esteContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.tela_browser);
        // Makes Progress bar Visible
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        myWebView = findViewById(R.id.webViewBrowser);

        esteContext = this.getApplicationContext();

        // Register BroadcastReceiver to track connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);


        // ref  = (ImageView) findViewById(R.id.imageViewRefreshFeed);


        if (ref != null)
            ref.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadPage();
                }
            });

    }

    // Refreshes the display if the network connection and the
    // pref settings allow it.
    @Override
    public void onStart() {
        super.onStart();

        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");

        updateConnectedFlags();

        // Only loads the page if refreshDisplay is true. Otherwise, keeps previous
        // display. For example, if the user has set "Wi-Fi only" in prefs and the
        // device loses its Wi-Fi connection midway through the user using the app,
        // you don't want to refresh the display--this would force the display of
        // an error page instead of stackoverflow.com content.
        if (refreshDisplay) {
            loadPage();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            Log.e("3G", Boolean.toString(mobileConnected));
            Log.e("WiFi", Boolean.toString(wifiConnected));

        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    // Uses AsyncTask subclass to download the XML feed from stackoverflow.com.
    // This avoids UI lock up. To prevent network operations from
    // causing a delay that results in a poor user experience, always perform
    // network operations on a separate thread from the UI.
    private void loadPage() {

        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                || ((sPref.equals(WIFI)) && (wifiConnected))) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    new DownloadXmlTask().execute(URL);
                }
            });
            // AsyncTask subclass

        } else {
            showErrorPage();
        }


    }

    public void onBackPressed() {
        finish();
        return;
    }

    protected void onStop() {
        super.onStop();


    }

    protected void onPostResume() {
        super.onPostResume();


    }

    protected void onResume() {
        super.onResume();

        //requestNewInterstitial();
        propaganda();


    }

    // Displays an error if the app is unable to load content.
    private void showErrorPage() {
        //setContentView(R.layout.tela_browser);

        // The specified network connection is not available. Displays error message.
        WebView myWebView = findViewById(R.id.webViewBrowser);
        myWebView.setBackgroundColor(Color.TRANSPARENT);
        myWebView.loadData(getResources().getString(R.string.connection_error),
                "text/html; charset=UTF-8", null);
    }


    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    // Populates the activity's options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    // Handles the user's menu selection.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    // Uploads XML from stackoverflow.com, parses it, and combines it with
    // HTML markup. Returns HTML string.
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
        List<StackOverflowXmlParser.Entry> entries = null;
        String title = null;
        String url = null;
        String summary = null;
        String text = null;
        String autor = null;
        String publicacao = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("dd-M-yyyy kk:mm:ss");

        // Checks whether the user set the preference to include summary text
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean pref = sharedPrefs.getBoolean("summaryPref", false);

        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
        htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
                formatter.format(rightNow.getTime()) + "</em>");

        try {
            stream = downloadUrl(urlString);
            entries = stackOverflowXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (StackOverflowXmlParser.Entry entry : entries) {
            htmlString.append("<p><a href='");
            htmlString.append(entry.link);
            htmlString.append("'>" + entry.title + "</a></p>");
            htmlString.append("<p>" + entry.text + "</p>");
            htmlString.append("<p>Publicado: " + entry.publicacao + "</p>");
            // If the user set the preference to include summary text,
            // adds it to the display.
            if (pref) {
                htmlString.append(entry.summary);
            }
        }
        return htmlString.toString();
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {


            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.xml_error);
            }

        }

        @Override
        protected void onPostExecute(String result) {
            // setContentView(R.layout.tela_browser);
            // Displays the HTML string in the UI via a WebView

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            //Get outer container

            myWebView.setBackgroundColor(Color.TRANSPARENT);
            myWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            myWebView.setWebChromeClient(new MyCustomChromeClient());
            myWebView.setWebViewClient(new WebViewClientDevoc());
            myWebView.loadData(result, "text/html; charset=UTF-8", null);

            WebSettings settings = myWebView.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setJavaScriptEnabled(true);
            settings.setAppCacheEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setSupportMultipleWindows(true);


          /*  if(myWebView != null) {
                myWebView.setWebViewClient(icon_new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        if(url.contains("http://biblianvi.byethost17.com")){

                            view.loadUrl(url);
                            Log.d("URL 1",url);

                        }else{

                            Intent browserIntent = icon_new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);

                            Log.d("URL 2", url);


                            return true;
                        }
                           return false;
                    }
                });
            } */


        }
    }

    /**
     * This BroadcastReceiver intercepts the android.net.ConnectivityManager.CONNECTIVITY_ACTION,
     * which indicates a connection change. It checks whether the type is TYPE_WIFI.
     * If it is, it checks whether Wi-Fi is connected and sets the wifiConnected flag in the
     * tela_browser activity accordingly.
     */
    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // Checks the user prefs and the network connection. Based on the result, decides
            // whether
            // to refresh the display or keep the current display.
            // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
            if (WIFI.equals(sPref) && networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // If device has its Wi-Fi connection, sets refreshDisplay
                // to true. This causes the display to be refreshed when the user
                // returns to the app.
                refreshDisplay = true;
                Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();

                // If the setting is ANY network and there is a network connection
                // (which by process of elimination would be mobile), sets refreshDisplay to true.
            } else if (ANY.equals(sPref) && networkInfo != null) {
                refreshDisplay = true;

                Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show();
                // Otherwise, the app can't download content--either because there is no network
                // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
                // is no Wi-Fi connection.
                // Sets refreshDisplay to false.
            } else {
                refreshDisplay = false;
                Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class WebViewClientDevoc extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            String host = Uri.parse(url).getHost();

            if (url.contains("http://biblianvi.byethost17.com")) {

                desativarPop();

                view.loadUrl(url);
                Log.d("URL 1", url);

                return true;

            } else {

                if (host.equals("m.facebook.com") || host.equals("www.facebook.com")) {

                    Log.d("URL FACEBOOK", url);
                    return false;

                }

                desativarPop();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);

                Log.d("URL 2", url);
                return false;

            }

        }

        private void desativarPop() {


            if (webViewPop != null) {
                webViewPop.setVisibility(View.GONE);
                frameLayoutDevoc.removeView(webViewPop);

                webViewPop = null;
            }


        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            //super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }


    }


    private class MyCustomChromeClient extends WebChromeClient {


        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            webViewPop = new WebView(esteContext);
            webViewPop.setVerticalScrollBarEnabled(false);
            webViewPop.setHorizontalScrollBarEnabled(false);
            webViewPop.setWebViewClient(new WebViewClientDevoc());
            webViewPop.getSettings().setJavaScriptEnabled(true);
            webViewPop.getSettings().setSavePassword(false);
            webViewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            frameLayoutDevoc.addView(webViewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(webViewPop);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            Log.d("onCloseWindow", "called");
        }


        public void onProgressChanged(WebView view, int progress) {

            setProgress(progress * 100);

        }


    }
}
