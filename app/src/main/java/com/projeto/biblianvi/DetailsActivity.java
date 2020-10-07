package com.projeto.biblianvi;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class DetailsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(MainActivityFragment.TAG, "in DetailsActivity onCreate");

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, it means
            // that our MainActivity is being shown with both
            // the titles and the text, so this activity is
            // no longer needed. Bail out and let the MainActivity
            // do all the work.
            finish();
            return;
        }

        if (getIntent() != null) {
            // This is another way to instantiate a details
            // fragment. 
            DetailsFragment details =
                    DetailsFragment.newInstance(getIntent().getExtras());

            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, details)
                    .commit();


        }
    }
}
