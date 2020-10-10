package com.projeto.biblianvi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projeto.biblianvi.biblianvi.R;

import java.util.List;

public class ListGraficoAdapter<B> extends ArrayAdapter {

    private Context context;
    private List<Biblia> biblias;


    public ListGraficoAdapter(Context context, List<Biblia> biblias) {
        super(context, 0, biblias);
        this.context = context;
        this.biblias = biblias;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Biblia b = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_list_stats, parent, false);
        }
        // Lookup view for data population
        TextView textViewGrafNameBook = convertView.findViewById(R.id.textViewGrafNameBook);
        textViewGrafNameBook.setText(b.getBooksName());

        try {
            LinearLayout linearLayout = convertView.findViewById(R.id.linearLayoutProgress3);
            int per = (b.getTotalDeVersosLidos() * 100) / b.getTotalDeVersiculos();
            TextView textViewGrafBar = convertView.findViewById(R.id.textViewGrafBar);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textViewGrafBar.getLayoutParams();
            params.width = (per * linearLayout.getWidth()) / 100;
            textViewGrafBar.setLayoutParams(params);

            TextView textViewGrafPercent = convertView.findViewById(R.id.textViewGrafPercent);
            textViewGrafPercent.setText(Integer.toString(per) + '%');

        } catch (ArithmeticException a) {
            Log.e("error", a.getMessage());
        }

        ListGraficoAdapter.this.notifyDataSetChanged();
        return convertView;
    }

    @Override
    public Biblia getItem(int position) {
        return biblias.get(position);
    }

}
