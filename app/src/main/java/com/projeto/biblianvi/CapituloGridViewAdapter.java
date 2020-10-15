package com.projeto.biblianvi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.projeto.biblianvi.biblianvi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ezequiel on 06/05/2017.
 */

public class CapituloGridViewAdapter extends BaseAdapter {

    private Button button;
    private Biblia biblia;
    private ViewGroup viewGroupParent;
    private TextView textViewGridView;
    private Activity activity;
    private static LayoutInflater inflater = null;
    private List<Integer> idColorList;

    public CapituloGridViewAdapter(List<Integer> idColorList, Activity activity, Biblia b, TextView t) {
        this.idColorList = idColorList;
        biblia = b;
        textViewGridView = t;
        this.activity = activity;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return idColorList.size();
    }

    public Integer getItem(int position) {
        return idColorList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.recyclerview_item, parent, false);

        viewGroupParent = parent;
        button = (Button) v.findViewById(R.id.buttonGrid);
        button.setTextSize(18);
        button.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        button.setTextColor(activity.getResources().getColor(idColorList.get(position)));

        button.setText(Integer.toString(position + 1));

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                try {
                    Button b = (Button) v;

                    biblia.setChapter(b.getText().toString());
                    BibliaBancoDadosHelper bibliaHelp = new BibliaBancoDadosHelper(activity);
                    int versiculos = bibliaHelp.getQuantidadeVersos(biblia.getBooksName(), biblia.getChapter());

                    List<Integer> idColorListVerse = new ArrayList<>();
                    for (int i = 0; i < versiculos; i++) {
                        idColorListVerse.add(R.color.white);
                    }

                    GridView gridViewVersiculos = (GridView) viewGroupParent;
                    VersiculoGridViewAdapter versiculoGridViewAdapter = new VersiculoGridViewAdapter(idColorListVerse, activity, biblia);
                    gridViewVersiculos.setAdapter(versiculoGridViewAdapter);
                    textViewGridView.setText(R.string.versiculo);

                    new DetailsFragment.LoadChapterTask(versiculoGridViewAdapter, idColorListVerse, activity, "verse").execute(null, null, null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        return button;
    }


}
