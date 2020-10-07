package com.projeto.biblianvi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projeto.biblianvi.biblianvi.R;

import java.util.ArrayList;

/**
 * Created by Ezequiel on 06/05/2016.
 */
public class AdapterDicionar extends ArrayAdapter<BibliaBancoDadosHelper.Dicionario> {

    private Context context;


    public AdapterDicionar(Context context, ArrayList<BibliaBancoDadosHelper.Dicionario> dicionarios) {
        super(context, 0, dicionarios);
        this.context = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BibliaBancoDadosHelper.Dicionario dic = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_dicionario, parent, false);
        }
        // Lookup view for data population
        TextView textViewPalavra = convertView.findViewById(R.id.textViewDicio);
        textViewPalavra.setText(dic.getPalavra());
        textViewPalavra.setTag(dic.getId());

        return convertView;
    }

}
