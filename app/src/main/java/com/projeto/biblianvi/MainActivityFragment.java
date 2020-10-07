package com.projeto.biblianvi;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.projeto.biblianvi.biblianvi.R;

public class MainActivityFragment extends Activity {
    public static final String TAG = "MainActivityFragment";
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "in MainActivity onCreate");
        super.onCreate(savedInstanceState);
        FragmentManager.enableDebugLogging(true);
        setContentView(R.layout.activity_fragment);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }


    public void onAttachFragment(Fragment fragment) {
        Log.v(TAG, "in MainActivity onAttachFragment. fragment id = "
                + fragment.getId());
        super.onAttachFragment(fragment);
    }

    @Override
    public void onStart() {
        Log.v(TAG, "in MainActivity onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "in MainActivity onResume");


    }

    @Override
    public void onPause() {
        Log.v(TAG, "in MainActivity onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.v(TAG, "in MainActivity onStop");
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "in MainActivity onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "in MainActivity onDestroy");
        super.onDestroy();
    }

    public boolean isMultiPane() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    public void showDetails(int index, boolean click) {
        Log.v(TAG, "in MainActivity showDetails(" + index + ")");

        if (isMultiPane()) {
            // Check what fragment is shown, replace if needed.
            DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(index);

                // Execute a transaction, replacing any existing
                // fragment inside the frame with the new one.
                Log.v(TAG, "about to run FragmentTransaction...");
                FragmentTransaction ft
                        = getFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.animator.fragment_open_enter,
                //		R.animator.fragment_open_exit);
                ft.setCustomAnimations(R.animator.bounce_in_down,
                        R.animator.slide_out_right);
                //ft.setCustomAnimations(R.animator.fade_in,
                //		R.animator.fade_out);
                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.details, details);
                //ft.addToBackStack(TAG);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.

            if (click) {
                Intent intent = new Intent();
                intent.setClass(this, DetailsActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        }
    }
}