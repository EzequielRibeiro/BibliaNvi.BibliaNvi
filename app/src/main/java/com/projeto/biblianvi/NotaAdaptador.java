package com.projeto.biblianvi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.projeto.biblianvi.biblianvi.R;

import java.util.ArrayList;

/**
 * Created by Ezequiel on 27/04/2016.
 */
public class NotaAdaptador extends ArrayAdapter<Anotacao> {


    private Context context;
    private Activity activity;


    public NotaAdaptador(Context context, Activity activity, ArrayList<Anotacao> notas) {
        super(context, 0, notas);
        this.context = context;
        this.activity = activity;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Anotacao nota = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_nota, parent, false);
        }
        // Lookup view for data population
        TextView textViewAssunto = convertView.findViewById(R.id.textViewAssunto);
        TextView textViewData = convertView.findViewById(R.id.textViewData);
        TextView textViewTexto = convertView.findViewById(R.id.textViewTexto);
        TextView textViewAnoId = convertView.findViewById(R.id.textViewAnoId);
        Button buttonExcluiNota = convertView.findViewById(R.id.buttonExcluiNota);
        buttonExcluiNota.setTag(position);

        // Populate the data into the template view using the data object
        textViewAssunto.setText(nota.getTitulo());
        textViewData.setText(nota.getData());
        textViewTexto.setText(nota.getTexto());
        textViewAnoId.setText(nota.getId());

        buildDialodExcuirNota(buttonExcluiNota, position);

        return convertView;
    }

    private void buildDialodExcuirNota(Button button, final int position) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle("Exluir anotação");

                builder.setMessage("A anotação será exluída definitivamente");

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Anotacao nota = getItem(position);

                        Toast.makeText(context, "Removido", Toast.LENGTH_SHORT).show();
                        ActivityAnotacao.notas.remove(position);
                        ActivityAnotacao.notaAdaptador.notifyDataSetChanged();
                        DBAdapterFavoritoNota dbAdapterFavoritoNota = new DBAdapterFavoritoNota(getContext());
                        dbAdapterFavoritoNota.open();
                        dbAdapterFavoritoNota.deleteNota(Long.valueOf(nota.getId()));
                        dbAdapterFavoritoNota.close();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }


    private class ItemSuporte {

        private TextView titulo;
        private TextView texto;
        private TextView data;

        public ItemSuporte(View v) {

            titulo = v.findViewById(R.id.textViewAssunto);
            texto = v.findViewById(R.id.textViewTexto);
            data = v.findViewById(R.id.textViewData);


        }

    }
}
