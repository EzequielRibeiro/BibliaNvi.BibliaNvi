package com.projeto.biblianvi.biblianvi;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projeto.biblianvi.Biblia;
import com.projeto.biblianvi.BibliaBancoDadosHelper;

public class myRecycleAdapterChapter extends RecyclerView.Adapter<myRecycleAdapterChapter.ViewHolder> {
    private int numberChapters;
    private Biblia biblia;
    private BibliaBancoDadosHelper dadosHelper;
    private Activity activity;
    private TextView textView;

    public myRecycleAdapterChapter(int numberChapters, Biblia biblia, BibliaBancoDadosHelper dadosHelper, Activity activity, TextView textView) {
        this.numberChapters = numberChapters;
        this.biblia = biblia;
        this.dadosHelper = dadosHelper;
        this.activity = activity;
        this.textView = textView;
    }

    @Override
    public myRecycleAdapterChapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

        ViewHolder vh = new ViewHolder(v, parent);
        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.myButton.setText(String.valueOf(position + 1));
        Log.e("POSITION:::::: ", String.valueOf(position));
        if (dadosHelper.getIsChapterLido(biblia.getBooksName(), Integer.toString(position + 1)))
            holder.myButton.setTextColor(activity.getResources().getColor(R.color.green));
    }


    @Override
    public int getItemCount() {
        return numberChapters;
    }


    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button myButton;
        private View viewGroupParent;

        public ViewHolder(View itemView, ViewGroup viewGroup) {
            super(itemView);
            myButton = (Button) itemView.findViewById(R.id.buttonGrid);
            myButton.setOnClickListener(this);
            viewGroupParent = viewGroup;
        }

        @Override
        public void onClick(View view) {

            Button b = (Button) view;

            biblia.setChapter(b.getText().toString());

            int versiculos = dadosHelper.getQuantidadeVersos(biblia.getBooksName(), biblia.getChapter());

            RecyclerView recyclerView = (RecyclerView) viewGroupParent;
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(null);
            recyclerView.getRecycledViewPool().clear();
            recyclerView.setLayoutManager(new GridLayoutManager(activity, 5));
            myRecycleAdapterVerse myRecycleAdapterVerse = new myRecycleAdapterVerse(versiculos, biblia, dadosHelper, activity);
            recyclerView.setAdapter(myRecycleAdapterVerse);

            myRecycleAdapterVerse.notifyDataSetChanged();
            textView.setText(R.string.versiculo);

        }
    }

}
