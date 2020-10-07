package com.projeto.biblianvi.biblianvi;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.projeto.biblianvi.Biblia;
import com.projeto.biblianvi.BibliaBancoDadosHelper;
import com.projeto.biblianvi.Lista_Biblia;


public class myRecycleAdapterVerse extends RecyclerView.Adapter<myRecycleAdapterVerse.ViewHolderVerse> {
    private int numberVerse;
    private Biblia biblia;
    private BibliaBancoDadosHelper dadosHelper;
    private Activity activity;

    public myRecycleAdapterVerse(int numberVerse, Biblia biblia, BibliaBancoDadosHelper dadosHelper, Activity activity) {
        this.numberVerse = numberVerse;
        this.biblia = biblia;
        this.dadosHelper = dadosHelper;
        this.activity = activity;

    }

    @Override
    public myRecycleAdapterVerse.ViewHolderVerse onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

        ViewHolderVerse vh = new ViewHolderVerse(v);
        return vh;
    }

    public void onBindViewHolder(ViewHolderVerse holder, int position) {

        holder.myButton.setText(String.valueOf(position + 1));
        if (dadosHelper.getIsChapterLido(biblia.getBooksName(), Integer.toString(position + 1)))
            holder.myButton.setTextColor(activity.getResources().getColor(R.color.green));
    }


    @Override
    public int getItemCount() {
        return numberVerse;
    }

    public class ViewHolderVerse extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button myButton;

        public ViewHolderVerse(View itemView) {
            super(itemView);
            myButton = (Button) itemView.findViewById(R.id.buttonGrid);
            myButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Button b = (Button) view;
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

    }

    public interface myAdapter {

    }


}
