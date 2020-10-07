package com.projeto.biblianvi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.projeto.biblianvi.biblianvi.R;

import java.util.List;

/**
 * Created by Ezequiel on 08/05/2017.
 */

public class VersiculoGridViewAdapter extends BaseAdapter {
    private Activity activity;
    private Button button;
    private Biblia biblia;
    private BibliaBancoDadosHelper bibliaBancoDadosHelper;
    private static LayoutInflater inflater = null;
    private List<Integer> idColorList;


    public VersiculoGridViewAdapter(List<Integer> idColorList, Activity activity, Biblia b) {
        this.idColorList = idColorList;
        this.activity = activity;
        biblia = b;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.recyclerview_item, parent, false);

        button = (Button) v.findViewById(R.id.buttonGrid);
        button.setTextSize(18);
        button.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        button.setTextColor(activity.getResources().getColor(idColorList.get(position)));

        button.setText(Integer.toString(position + 1));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button b = (Button) v;
                Toast.makeText(activity, b.getText(), Toast.LENGTH_LONG).show();

                biblia.setVerseNum(b.getText().toString());

                Intent intent = new Intent(activity, Lista_Biblia.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("livro", biblia.getBooksName());
                intent.putExtra("capitulo", biblia.getChapter());
                intent.putExtra("versiculo", biblia.getVersesNum());
                intent.putExtra("termoBusca", "nada");

                activity.startActivity(intent);

                Log.e("Biblia", biblia.getBooksName() + " " + biblia.getChapter() + " " + biblia.getVersesNum());
            }
        });

        return button;
    }
}
