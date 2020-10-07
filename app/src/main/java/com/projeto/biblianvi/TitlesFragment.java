package com.projeto.biblianvi;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;


public class TitlesFragment extends ListFragment {
    int mCurCheckPosition = 0;
    private MainActivityFragment myActivity = null;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle icicle) {
        Log.v(MainActivityFragment.TAG,
                "in TitlesFragment onInflate. AttributeSet contains:");
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.v(MainActivityFragment.TAG, "    " + attrs.getAttributeName(i) +
                    " = " + attrs.getAttributeValue(i));
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);


        super.onInflate(activity, attrs, icicle);
    }

    @Override
    public void onAttach(Activity myActivity) {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onAttach; activity is: " + myActivity);
        super.onAttach(myActivity);
        this.myActivity = (MainActivityFragment) myActivity;
    }

    @Override
    public void onCreate(Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onCreate. Bundle contains:");
        if (icicle != null) {
            for (String key : icicle.keySet()) {
                Log.v(MainActivityFragment.TAG, "    " + key);
            }
        } else {
            Log.v(MainActivityFragment.TAG, "    icicle is null");
        }
        super.onCreate(icicle);
        if (icicle != null) {
            // Restore last state for checked position.
            mCurCheckPosition = icicle.getInt("curChoice", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater myInflater, ViewGroup container, Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onCreateView. container is " + container);

        return super.onCreateView(myInflater, container, icicle);
    }

    @Override
    public void onViewCreated(View view, Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onViewCreated");


        super.onViewCreated(view, icicle);
    }

    @Override
    public void onActivityCreated(Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onActivityCreated. icicle contains:");


        if (icicle != null) {
            for (String key : icicle.keySet()) {
                Log.v(MainActivityFragment.TAG, "    " + key);
            }
        } else {
            Log.v(MainActivityFragment.TAG, "    icicle is null");
        }
        super.onActivityCreated(icicle);

        BibliaBancoDadosHelper bibliaHelp = new BibliaBancoDadosHelper(getActivity().getApplicationContext());
        List<Biblia> bookNameList = bibliaHelp.getAllBooksName();
        String[] livro = new String[bookNameList.size()];

        for (int i = 0; i <= bookNameList.size() - 1; i++) {
            livro[i] = bookNameList.get(i).getBooksName();
        }

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                livro));

        ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setSelection(mCurCheckPosition);

        myActivity.showDetails(mCurCheckPosition, false);


    }


    @Override
    public void onStart() {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onPause");
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onSaveInstanceState. Bundle is " + icicle);
        super.onSaveInstanceState(icicle);
        if (icicle != null) {
            Log.v(MainActivityFragment.TAG, "icicle contains the following:");
            for (String key : icicle.keySet()) {
                Log.v(MainActivityFragment.TAG, "    " + key);
            }
        }
        icicle.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onViewStateRestored(Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onViewStateRestored");
        super.onViewStateRestored(icicle);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {

        Log.v(MainActivityFragment.TAG, "in TitlesFragment onListItemClick. pos = "
                + pos);


        myActivity.showDetails(pos, true);
        mCurCheckPosition = pos;


        if (mFirebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Long.toString(id));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, l.getItemAtPosition(pos).toString());
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, l.getItemAtPosition(pos).toString());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            Log.i("Analytics", l.getItemAtPosition(pos).toString());

        }

    }

    @Override
    public void onStop() {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.v(MainActivityFragment.TAG, "in TitlesFragment onDetach");
        super.onDetach();
        myActivity = null;
    }
}
