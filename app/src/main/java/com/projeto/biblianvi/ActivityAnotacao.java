package com.projeto.biblianvi;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.projeto.biblianvi.biblianvi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityAnotacao extends Activity {

    static public NotaAdaptador notaAdaptador;
    static public ArrayList<Anotacao> notas;
    Button buttonNotaSalvar;
    Button buttonNotaCancel;
    EditText editTextTittuloNota, editTextNotaTexto;
    FrameLayout item;
    View child;
    private ListView listView;
    private LinearLayout layoutNotaAdcionar;
    private DBAdapterFavoritoNota dbAdapterFavoritoNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotacao);

        listView = findViewById(R.id.listNota);
        layoutNotaAdcionar = findViewById(R.id.layoutNotaAdcionar);
        dbAdapterFavoritoNota = new DBAdapterFavoritoNota(getApplicationContext());


        carregarLista();

        layoutNotaAdcionar.setOnClickListener(new View.OnClickListener() {

                                                  @Override
                                                  public void onClick(View v) {

                                                      item = findViewById(R.id.frameNota);
                                                      child = getLayoutInflater().inflate(R.layout.nota_adicionar, null);
                                                      item.addView(child);

                                                      buttonNotaSalvar = findViewById(R.id.buttonNotaSalvar);
                                                      buttonNotaCancel = findViewById(R.id.buttonNotaCancel);
                                                      editTextTittuloNota = findViewById(R.id.editTextTittuloNota);
                                                      editTextNotaTexto = findViewById(R.id.editTextNotaTexto);

                                                      frameComponentes();

                                                  }
                                              }

        );
    }

    private void frameComponentes() {

        buttonNotaSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                String currentDateandTime = sdf.format(new Date());


                String titulo = editTextTittuloNota.getText().toString();
                String texto = editTextNotaTexto.getText().toString();
                String data = currentDateandTime;

                if (titulo.length() > 1 || texto.length() > 1) {

                    dbAdapterFavoritoNota.open();
                    long id = dbAdapterFavoritoNota.insertNota(titulo, texto, data);
                    dbAdapterFavoritoNota.close();
                    Anotacao anotacao = new Anotacao();
                    anotacao.setId((int) id);
                    anotacao.setTitulo(titulo);
                    anotacao.setTexto(texto);
                    anotacao.setData(data);
                    notas.add(anotacao);
                    notaAdaptador.notifyDataSetChanged();
                    item.removeView(child);

                    Toast.makeText(getApplicationContext(), R.string.nota_save, Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), R.string.digite_algo, Toast.LENGTH_LONG).show();

                }
            }
        });

        buttonNotaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item.removeView(child);
            }
        });


    }

    protected void onResume() {
        super.onResume();
        carregarLista();

    }

    private void carregarLista() {

        notas = new ArrayList<Anotacao>();
        Cursor cursor;
        dbAdapterFavoritoNota.open();
        cursor = dbAdapterFavoritoNota.getAllValuesNota();

        Anotacao anotacao;
        if (cursor.moveToFirst()) {
            do {
                anotacao = new Anotacao();
                anotacao.setId(cursor.getInt(0));
                anotacao.setTitulo(cursor.getString(1));
                anotacao.setTexto(cursor.getString(2));
                anotacao.setData(cursor.getString(3));
                notas.add(anotacao);
            } while (cursor.moveToNext());
        }

        dbAdapterFavoritoNota.close();
        notaAdaptador = new NotaAdaptador(getApplicationContext(), this, notas);
        listView.setAdapter(notaAdaptador);

        if (notas.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.no_notes, Toast.LENGTH_LONG).show();
        }

    }

    public void onDestroy() {
        super.onDestroy();
        if (notas != null)
            notas.clear();
    }

}