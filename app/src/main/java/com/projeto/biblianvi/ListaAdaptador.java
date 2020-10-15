package com.projeto.biblianvi;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.projeto.biblianvi.biblianvi.R;

import java.util.List;

/**
 * Created by Ezequiel on 09/07/2015.
 */
public class ListaAdaptador extends BaseAdapter {


    private final int LIDO = 1;
    private Context context;
    private LayoutInflater mInflater;
    private List<Biblia> itensList;
    private Biblia biblia;
    private BibliaBancoDadosHelper bancoHelper;
    private Intent intent;
    private ItemSuporteBiblia itemSuporteBiblia;
    private boolean mostrarVersiculosLidos;
    private SharedPreferences sharedPrefs;
    private boolean modoNoturno = false;
    private boolean pesquiar = false;
    private String fonte = "Arial";


    public ListaAdaptador(Context cont, List<Biblia> itens, boolean b) {

        context = cont;
        mInflater = LayoutInflater.from(this.context);
        itensList = itens;
        pesquiar = b;

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(cont);

        mostrarVersiculosLidos = sharedPrefs.getBoolean("mostrarVersiculosLidos", true);
        modoNoturno = sharedPrefs.getBoolean("noturnoPref", false);

        SharedPreferences sp = context.getSharedPreferences("versiculo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("texto_biblico", "vazio");
        editor.commit();


        fonte = sharedPrefs.getString("fonteEstilo", "Arial");

    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_biblia_aberta, null);
            itemSuporteBiblia = new ItemSuporteBiblia(convertView);
            convertView.setTag(itemSuporteBiblia);

        } else {

            itemSuporteBiblia = (ItemSuporteBiblia) convertView.getTag();

        }

        biblia = getItem(position);

        //altera o tamanho da fonte
        tamanhoFonte(itemSuporteBiblia);

        if (!mostrarVersiculosLidos) {
            itemSuporteBiblia.textoAberto.setText(Html.fromHtml(biblia.toString().replace("green", "black")));

        } else {

            itemSuporteBiblia.textoAberto.setText(Html.fromHtml(biblia.toString()));

        }

        if (modoNoturno) {
            modoNoturno(itemSuporteBiblia, biblia);
        }

        //Habilita o botão de abrir livro quando for pesquisa
        if (pesquiar) {
            itemSuporteBiblia.buttonAbrirLivro.setVisibility(View.VISIBLE);
            itemSuporteBiblia.buttonAbrirLivro.setEnabled(true);
            itemSuporteBiblia.textoAberto.setText(Html.fromHtml(biblia.toPesquisarString()));

            itemSuporteBiblia.buttonAbrirLivro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    chamarLivro(getItem(position));
                    Log.e("teste", "teste");

                }
            });


        }

        return convertView;
    }

    private void tamanhoFonte(ItemSuporteBiblia text) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String t = sharedPrefs.getString("fontePref", "18");
        text.textoAberto.setTextSize(Integer.parseInt(t));
    }


    private void modoNoturno(ItemSuporteBiblia text, Biblia currentListData) {

        text.textoAberto.setTextColor(Color.WHITE);
        text.textoAberto.setBackgroundColor(context.getResources().getColor(R.color.black));

        if (!mostrarVersiculosLidos)
            text.textoAberto.setText(Html.fromHtml(currentListData.toString().replace("green", "white")));

    }

    public boolean isPackageInstalled(String packageName) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void close() {
        if (bancoHelper != null) {
            bancoHelper = null;
        }
    }

    public void chamarLivro(Biblia bi) {

        intent = new Intent(context, Lista_Biblia.class);
        intent.putExtra("livro", bi.getBooksName());
        intent.putExtra("capitulo", bi.getChapter());
        intent.putExtra("versiculo", bi.getVersesNum());
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public int getCount() {
        return itensList.size();
    }

    @Override
    public Biblia getItem(int position) {
        return itensList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private class ItemSuporteBiblia {


        TextView textoAberto;
        Button buttonAbrirLivro;


        @SuppressLint("WrongConstant")
        public ItemSuporteBiblia(View v) {

            textoAberto = v.findViewById(R.id.textoAberto);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                textoAberto.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

            buttonAbrirLivro = v.findViewById(R.id.buttonAbrirLivro);


            if (!fonte.contains("Arial")) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), "Font/" + fonte);
                textoAberto.setTypeface(font);

            }

        }

    }

}

