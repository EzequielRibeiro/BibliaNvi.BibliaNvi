package com.projeto.biblianvi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.projeto.biblianvi.biblianvi.R;

import java.util.ArrayList;
import java.util.Iterator;

public class DicionarioActivity extends Activity {


    private ListView listView;
    private AdapterDicionar arrayAdapter;
    private ArrayList<BibliaBancoDadosHelper.Dicionario> list;
    private ArrayAdapter<String> auto;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView textViewDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicionario);

        listView = findViewById(R.id.listViewDicionario);
        frameLayout = findViewById(R.id.frameDicionario);
        textViewDeveloper = findViewById(R.id.textViewDeveloperDicio);

        textViewDeveloper.setText(MainActivity.VERSIONAPP);

        list = new BibliaBancoDadosHelper(this).getDicionarioPalavra();

        arrayAdapter = new AdapterDicionar(DicionarioActivity.this, list);

        listView.setAdapter(arrayAdapter);

        auto = new ArrayAdapter<String>(DicionarioActivity.this, android.R.layout.simple_spinner_dropdown_item);

        Iterator<BibliaBancoDadosHelper.Dicionario> iterator = list.iterator();
        while (iterator.hasNext()) {
            auto.add(iterator.next().getPalavra());
        }

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(auto);
        autoCompleteTextView.setTextColor(getResources().getColor(android.R.color.black));
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT) ||
                        (actionId == EditorInfo.IME_ACTION_GO)) {


                    int max = listView.getCount();

                    String palavra = autoCompleteTextView.getText().toString();


                    for (int i = 0; i <= max - 1; i++) {

                        BibliaBancoDadosHelper.Dicionario dic = (BibliaBancoDadosHelper.Dicionario) listView.getItemAtPosition(i);

                        if (dic.getPalavra().startsWith(palavra)) {

                            listView.setSelection(i);

                            i = max - 1;
                        }
                    }

                }


                return false;
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int max = listView.getCount();

                String palavra = (String) parent.getItemAtPosition(position);


                for (int i = 0; i <= max - 1; i++) {

                    BibliaBancoDadosHelper.Dicionario dic = (BibliaBancoDadosHelper.Dicionario) listView.getItemAtPosition(i);

                    if (dic.getPalavra().startsWith(palavra)) {

                        listView.setSelection(i);

                        i = max - 1;
                    }
                }

            }
        });

        LinearLayout layoutAlfaDiciona = findViewById(R.id.layoutAlfaDiciona);
        TextView text;


        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {

            text = new TextView(this);
            text.setTextSize(20);
            text.setText(Character.toString(alphabet));
            text.setTag(Character.toString(alphabet));
            text.setPadding(5, 0, 0, 0);
            text.setOnClickListener(new LetraEscolha());

            layoutAlfaDiciona.addView(text);

        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView textViewPalavra = view.findViewById(R.id.textViewDicio);
                Log.e("Dicionario", Integer.toString((Integer) textViewPalavra.getTag()));

                BibliaBancoDadosHelper.Dicionario dic =
                        new BibliaBancoDadosHelper(DicionarioActivity.this).getDicionarioTexto(textViewPalavra.getTag().toString());


                linearLayout = new LinearLayout(DicionarioActivity.this);
                linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setPadding(5, 5, 5, 5);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.cinzaclaro));

                TextView textView = new TextView(DicionarioActivity.this);
                String t = dic.getTexto();
                textView.setText(t);
                textView.setTextColor(getResources().getColor(android.R.color.black));
                textView.setTextSize(22);


                Button button = new Button(DicionarioActivity.this);
                button.setText("Fechar");
                button.setHeight(50);
                button.setWidth(80);
                button.setMaxHeight(50);
                button.setMaxWidth(80);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frameLayout.removeView(scrollView);
                    }
                });

                linearLayout.addView(textView);
                linearLayout.addView(button);

                scrollView = new ScrollView(DicionarioActivity.this);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                scrollView.addView(linearLayout);

                frameLayout.addView(scrollView);


            }
        });

    }

    private class LetraEscolha implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int max = listView.getCount();

            String tag = v.getTag().toString();

            Log.e("LEtra", tag);

            for (int i = 0; i <= max - 1; i++) {

                BibliaBancoDadosHelper.Dicionario dic = (BibliaBancoDadosHelper.Dicionario) listView.getItemAtPosition(i);

                if (dic.getPalavra().startsWith(tag)) {

                    listView.setSelection(i);

                    i = max - 1;
                }


            }


        }
    }

}
