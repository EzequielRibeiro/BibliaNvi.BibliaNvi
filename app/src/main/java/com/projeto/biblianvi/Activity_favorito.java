package com.projeto.biblianvi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.projeto.biblianvi.biblianvi.R;

import java.util.LinkedList;
import java.util.List;

public class Activity_favorito extends Activity {


    public static final String TABELANAME = "favorito";
    public static final String CAMPOS = "(id integer primary key,idVerso TINYINT(3) not null)";
    public FavoritoAdapter favoritoAdapter;
    private ListView listaFavorito;
    private List<Biblia> listBiblia;
    private DBAdapterFavoritoNota dbAdapterFavoritoNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);

        listaFavorito = findViewById(R.id.listViewFavorito);

        dbAdapterFavoritoNota = new DBAdapterFavoritoNota(this);
        dbAdapterFavoritoNota.open();

        Cursor cursor = dbAdapterFavoritoNota.getAllValuesFavorite();


        listBiblia = new LinkedList<Biblia>();

        Biblia biblia;

        if (cursor != null && cursor.getCount() > 0)
            if (cursor.moveToFirst()) {
                do {
                    biblia = new Biblia();
                    biblia.setIdVerse(cursor.getString(0));
                    biblia.setChapter(cursor.getString(1));
                    biblia.setVerseNum(cursor.getString(2));
                    biblia.setText(cursor.getString(3));
                    biblia.setBookVersion(cursor.getString(4));
                    biblia.setBooksName(cursor.getString(5));

                    listBiblia.add(biblia);

                } while (cursor.moveToNext());
            }

        dbAdapterFavoritoNota.close();
        favoritoAdapter = new FavoritoAdapter(Activity_favorito.this, listBiblia);

        listaFavorito.setAdapter(favoritoAdapter);


    }

    public void deleteFavorite(View v) {

        Button b = (Button) v;
        confirmarDelete((String) b.getTag(R.string.idVerse), (int) b.getTag(R.string.idPosition));

    }

    private void confirmarDelete(String idVer, int position) {


        TextView textView = new TextView(this);
        textView.setText("Deseja apagar esse versículo dos favoritos ?");
        textView.setTextSize(18);

        TextView textViewTitle = new TextView(this);
        textViewTitle.setText("Apagar versículo");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(textView);
        // alertDialogBuilder.setCustomTitle(textViewTitle);


        alertDialogBuilder.setPositiveButton("Confimar", new dialogo(idVer, position));


        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    private class FavoritoAdapter extends ArrayAdapter<Biblia> {

        List<Biblia> biblias;

        public FavoritoAdapter(Context context, List<Biblia> biblias) {
            super(context, 0, biblias);
            this.biblias = biblias;
        }

        @Override
        public Biblia getItem(int position) {
            return biblias.get(position);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_list_favorito, parent, false);
            }

            Biblia b = getItem(position);
            Button delete = convertView.findViewById(R.id.buttonDeleteFavorite);
            delete.setTag(R.string.idVerse, b.getIdVerse());
            delete.setTag(R.string.idPosition, position);

            TextView textViewText = convertView.findViewById(R.id.textViewFavorText);
            textViewText.setText(Html.fromHtml(b.getText() + " " + "<b>" + b.getBooksName() + "</b> " +
                    b.getChapter() + ':' + b.getVersesNum()));

            return convertView;
        }


    }

    private class dialogo implements DialogInterface.OnClickListener {

        private String idVerso;
        private int posicao;

        public dialogo(String idVerso, int posicao) {

            this.idVerso = idVerso;
            this.posicao = posicao;

        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dbAdapterFavoritoNota.open();
            dbAdapterFavoritoNota.deleteFavorite(Long.valueOf(idVerso));
            dbAdapterFavoritoNota.close();
            listBiblia.remove(posicao);
            favoritoAdapter.notifyDataSetChanged();
        }
    }

}
