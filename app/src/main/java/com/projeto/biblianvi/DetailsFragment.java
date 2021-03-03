package com.projeto.biblianvi;

import android.app.Activity;
import android.app.Fragment;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.projeto.biblianvi.biblianvi.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {

    private int mIndex = 0;
    private GridView gridview;
    private BibliaBancoDadosHelper bibliaHelp;
    private static Biblia biblia;
    private CapituloGridViewAdapter capituloGridViewAdapter;
    private List<Integer> idColorListChapter;
    private static ProgressBar progressBar;
    private TextView textViewGridView;


    public static DetailsFragment newInstance(int index) {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment newInstance(" + index + ")");

        DetailsFragment df = new DetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        df.setArguments(args);
        return df;
    }

    public static DetailsFragment newInstance(Bundle bundle) {
        int index = bundle.getInt("index", 0);
        return newInstance(index);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {

        Log.v(MainActivityFragment.TAG,
                "in DetailsFragment onInflate. AttributeSet contains:");
        for (int i = 0; i < attrs.getAttributeCount(); i++)
            Log.v(MainActivityFragment.TAG, "    " + attrs.getAttributeName(i) +
                    " = " + attrs.getAttributeValue(i));
        super.onInflate(activity, attrs, savedInstanceState);

    }

    @Override
    public void onAttach(Activity myActivity) {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onAttach; activity is: " +
                myActivity);
        super.onAttach(myActivity);
    }

    @Override
    public void onCreate(Bundle myBundle) {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onCreate. Bundle contains:");
        if (myBundle != null) {
            for (String key : myBundle.keySet()) {
                Log.v(MainActivityFragment.TAG, "    " + key);
            }
        } else {
            Log.v(MainActivityFragment.TAG, "    myBundle is null");
        }
        super.onCreate(myBundle);

        mIndex = getArguments().getInt("index", 0);

    }

    public int getShownIndex() {
        return mIndex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onCreateView. container = " +
                container);

        // Don't tie this fragment to anything through the inflater. Android
        // takes care of attaching fragments for us. The container is only
        // passed in so we can know about the container where this View
        // hierarchy is going to go. If we're not going anywhere, don't
        // bother to create the view hierarchy and just return null.
        if (container == null) {
            Log.v(MainActivityFragment.TAG, "container is null. No need to inflate.");
            return null;
        }


        View v = inflater.inflate(R.layout.details, container, false);
        textViewGridView = v.findViewById(R.id.textViewGridView);
        textViewGridView.setText(getString(R.string.capitulo));
        progressBar = (ProgressBar) v.findViewById(R.id.progressBarGrid);

        bibliaHelp = new BibliaBancoDadosHelper(getActivity().getApplicationContext());
        List<Biblia> bookNameList = bibliaHelp.getAllBooksName();

        String[] livro = new String[bookNameList.size()];

        for (int i = 0; i <= bookNameList.size() - 1; i++) {
            livro[i] = bookNameList.get(i).getBooksName();
        }


        int capitulos = bibliaHelp.getQuantidadeCapitulos(livro[mIndex]);
        Log.e("Capitulo", "valor " + capitulos);
        idColorListChapter = new ArrayList<>();
        for (int i = 0; i < capitulos; i++) {
            idColorListChapter.add(R.color.white);
        }

        biblia = new Biblia();
        biblia.setBooksName(livro[mIndex]);

        gridview = v.findViewById(R.id.gridview);
        capituloGridViewAdapter = new CapituloGridViewAdapter(idColorListChapter, getActivity(), biblia, textViewGridView);
        gridview.setAdapter(capituloGridViewAdapter);

        new LoadChapterTask(capituloGridViewAdapter, idColorListChapter, getActivity(), "chapter").execute(null, null, null);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onViewCreated for View: " + view);
        super.onViewCreated(view, icicle);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        Log.v(MainActivityFragment.TAG,
                "in DetailsFragment onActivityCreated. savedState contains:");
        if (savedState != null) {
            for (String key : savedState.keySet()) {
                Log.v(MainActivityFragment.TAG, "    " + key);
            }
        } else {
            Log.v(MainActivityFragment.TAG, "    savedState is null");
        }
        super.onActivityCreated(savedState);
    }

    @Override
    public void onViewStateRestored(Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onViewStateRestored for view hierarchy");
        super.onViewStateRestored(icicle);
    }

    @Override
    public void onStart() {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onPause");
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle icicle) {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onSaveInstanceState. Bundle is " + icicle);
        super.onSaveInstanceState(icicle);
        if (icicle != null) {
            Log.v(MainActivityFragment.TAG, "icicle contains the following:");
            for (String key : icicle.keySet()) {
                Log.v(MainActivityFragment.TAG, "    " + key);
            }
        }
    }

    @Override
    public void onStop() {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onDestroyView, view = " +
                getView());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.v(MainActivityFragment.TAG, "in DetailsFragment onDetach");
        super.onDetach();
    }

    public static class LoadChapterTask extends AsyncTask<Void, Void, Void> {

        private List<Integer> list;
        private BibliaBancoDadosHelper bancoDadosHelper;
        private String checkByChapterVerse;
        private BaseAdapter adapter;

        protected void onProgressUpdate(Void... progress) {

        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }

        public LoadChapterTask(BaseAdapter adapter, List<Integer> list, Activity activity, String checkByChapterVerse) {
            this.list = list;
            bancoDadosHelper = new BibliaBancoDadosHelper(activity);
            this.checkByChapterVerse = checkByChapterVerse;
            this.adapter = adapter;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {

                for (int i = 0; i < list.size(); i++) {

                    if (checkByChapterVerse.equals("chapter")) {
                        if (bancoDadosHelper.getIsChapterLido(biblia.getBooksName(), Integer.toString(i + 1)))
                            list.set(i, R.color.green);
                    } else {
                        if (bancoDadosHelper.getIsVerseLido(biblia.getBooksName(), biblia.getChapter(), Integer.toString(i + 1)))
                            list.set(i, R.color.green);
                    }
                }
            } catch (SQLiteException exception) {

                exception.printStackTrace();
            }
            return null;
        }
    }

}