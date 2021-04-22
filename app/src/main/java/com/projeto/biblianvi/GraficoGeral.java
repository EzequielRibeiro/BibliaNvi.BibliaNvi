package com.projeto.biblianvi;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.projeto.biblianvi.biblianvi.R;

import org.jetbrains.annotations.NotNull;

public class GraficoGeral extends TabActivity {


    private static final int QUANT_VERSOS = 31062;
    private TextView textViewTotalLido;
    private AdView mAdView;

    public static float quantVersosLidos(Context context) {

        int i;

        BibliaBancoDadosHelper bi = new BibliaBancoDadosHelper(context);

        i = bi.getQuantVersosLidosTotal();


        return (float) (i * 100) / QUANT_VERSOS;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_geral);


        textViewTotalLido = findViewById(R.id.textViewTotalLido);

        Resources ressources = getResources();
        TabHost tabHost = getTabHost();

        // Android tab
        Intent intentAndroid = new Intent().setClass(this, Grafico_Um.class);
        TabHost.TabSpec tabSpecAndroid = tabHost
                .newTabSpec("Android")
                .setIndicator("", ressources.getDrawable(R.drawable.icon_tab_old))
                .setContent(intentAndroid);

        // Apple tab
        Intent intentApple = new Intent().setClass(this, Grafico_Dois.class);
        TabHost.TabSpec tabSpecApple = tabHost
                .newTabSpec("Apple")
                .setIndicator("", ressources.getDrawable(R.drawable.icon_tab_new))
                .setContent(intentApple);

        // add all tabs
        tabHost.addTab(tabSpecAndroid);
        tabHost.addTab(tabSpecApple);


        //set Windows tab as default (zero based)
        tabHost.setCurrentTab(2);

        mAdView = findViewById(R.id.adViewGraf);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }


            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                LinearLayout myLayoutBase = findViewById(R.id.linearLayoutGrafAd);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) myLayoutBase.getLayoutParams();
                params.height = 0;
                mAdView.setLayoutParams(params);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


    }

    protected void onResume() {
        super.onResume();
        textViewTotalLido.setText(String.format("%.2f", quantVersosLidos(getApplicationContext())) + "%");

    }

    protected void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    protected void onStop() {
        super.onStop();


    }

    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

          /*
            case R.id.action_settings:
                Intent settingsActivity = icon_new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            case R.id.action_devocional:
                Intent in = icon_new Intent(getApplicationContext(),NetworkActivityDevocional.class);
                startActivity(in);
                return true;
                */
            case R.id.action_exit:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
