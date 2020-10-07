package com.projeto.biblianvi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.projeto.biblianvi.biblianvi.R;

import java.util.List;


public class Activity_busca_avancada extends Activity {

    boolean searchFind = false;
    /* tipoDeBusca = "buscarTestamento"
     0 = Tota biblia,  1 = NT,   2 = VT,  3 = Livro */
    private String tipoDeBusca = "3";
    private Button botaoPesquisar;
    private EditText editText;
    private Spinner spinnerLivros;
    private RadioButton radioNovo, radioVelho, radioBib;
    private ProgressBar progressBarBusca;
    private LinearLayout linearLayout;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_busca_avancada);

        botaoPesquisar = findViewById(R.id.buttonPesquisaAvanc);
        editText = findViewById(R.id.editTextAvanc);
        spinnerLivros = findViewById(R.id.spinnerBuscaAvanc);
        radioVelho = findViewById(R.id.radio_velho);
        radioNovo = findViewById(R.id.radio_novo);
        radioBib = findViewById(R.id.radio_Biblia);
        progressBarBusca = findViewById(R.id.progressBarBusca);
        progressBarBusca.setVisibility(View.GONE);

        botaoPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pesquisar();
            }

        });


        //editText.setHint("Digite uma palavra ou frase");
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {


            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT) ||
                        (actionId == EditorInfo.IME_ACTION_GO)) {

                    pesquisar();
                }

                return false;
            }
        });


        BibliaBancoDadosHelper bibliaHelp = new BibliaBancoDadosHelper(getApplicationContext());
        List<Biblia> bookNameList = bibliaHelp.getAllBooksName();
        String[] livro = new String[bookNameList.size()];

        for (int i = 0; i <= bookNameList.size() - 1; i++) {
            livro[i] = bookNameList.get(i).getBooksName();
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, livro);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerLivros.setAdapter(aa);

        spinnerLivros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                radioVelho.setChecked(false);
                radioNovo.setChecked(false);
                radioBib.setChecked(false);
                tipoDeBusca = "3";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                tipoDeBusca = "3";
            }

        });

        mAdView = findViewById(R.id.adViewPesq);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    private void pesquisar() {

        if (editText.getText().length() >= 2) {

            Intent intent = new Intent(getApplicationContext(), Lista_Biblia.class);

            intent.putExtra("livro", spinnerLivros.getSelectedItem().toString());
            intent.putExtra("capitulo", "0");
            intent.putExtra("versiculo", "0");
            intent.putExtra("buscar", "true");
            intent.putExtra("buscarTestamento", "0");
            intent.putExtra("termoBusca", editText.getText().toString());

            //salva o termo da busca para ser usado por Biblia para realçar a cor da palavra
            SharedPreferences settings = getSharedPreferences("termo_busca", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("busca", editText.getText().toString());
            editor.commit();

            String[] terms;

            if (tipoDeBusca.equals("0")) {

                intent.putExtra("buscarTestamento", "0");
                // startActivity(intent);
                terms = new String[]{"0", editText.getText().toString()};
                new PesquisarBanco(getApplicationContext()).execute(terms);

            } else if (tipoDeBusca.equals("1")) {

                intent.putExtra("buscarTestamento", "1");
                // startActivity(intent);
                terms = new String[]{"1", editText.getText().toString()};
                new PesquisarBanco(getApplicationContext()).execute(terms);

            } else if (tipoDeBusca.equals("2")) {

                intent.putExtra("buscarTestamento", "2");
                // startActivity(intent);
                terms = new String[]{"2", editText.getText().toString()};
                new PesquisarBanco(getApplicationContext()).execute(terms);

            } else if (tipoDeBusca.equals("3")) {

                intent.putExtra("buscarTestamento", "3");
                // startActivity(intent);
                terms = new String[]{"3", editText.getText().toString(), spinnerLivros.getSelectedItem().toString()};
                new PesquisarBanco(getApplicationContext()).execute(terms);
            }


        } else {
            editText.setHint(getString(R.string.digite_palavra));
        }
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {

            switch (view.getId()) {
                case R.id.radio_velho:
                    if (checked)
                        tipoDeBusca = "1";
                    break;
                case R.id.radio_novo:
                    if (checked)
                        tipoDeBusca = "2";
                    break;
                case R.id.radio_Biblia:
                    if (checked)
                        tipoDeBusca = "0";
                    break;

            }


        }

    }

    protected void onResume() {
        super.onResume();

        LinearLayout myLayoutBase = findViewById(R.id.linearLayoutBusca);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) myLayoutBase.getLayoutParams();

        if (MainActivity.isNetworkAvailable(getApplicationContext())) {

            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            //propaganda Google
            AdView mAdView = findViewById(R.id.adViewGraf);
            if (mAdView != null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            } else {
                Log.e("Erro Admob", "Tela Gráfico");
            }


        } else {

            //  params.height = 0;

        }


    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (searchFind) {
            searchFind = false;
            if (linearLayout != null) {
                ViewGroup parent = (ViewGroup) linearLayout.getParent();
                if (parent != null) {
                    parent.removeView(linearLayout);
                    linearLayout = null;
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    private class PesquisarBanco extends AsyncTask<String, Integer, String> {


        private BibliaBancoDadosHelper bibliaHelp;
        private ListaAdaptador listaAdaptador;
        private ListView listView;
        private FrameLayout frameLayout;
        private List<Biblia> lista;
        private LinearLayout linearLayoutBusca;


        public PesquisarBanco(Context context) {

            bibliaHelp = new BibliaBancoDadosHelper(context);
            listView = new ListView(context);
            linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayoutBusca = findViewById(R.id.linearLayoutBusca);
            frameLayout = findViewById(R.id.framelayoutBuscar);

        }

        protected String doInBackground(String... params) {

            if (params[0].equals("0")) {

                lista = bibliaHelp.pesquisarBiblia(params[1]);

            } else if (params[0].equals("1") || params[0].equals("2")) {

                lista = bibliaHelp.pesquisarBibliaTestamento(params[1], params[0]);

            } else if (params[0].equals("3")) {

                lista = bibliaHelp.pesquisarBibliaLivro(params[2], params[1]);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {


            if (!lista.isEmpty()) {

                int i = lista.size();

                listaAdaptador = new ListaAdaptador(Activity_busca_avancada.this, lista, true);

                listView.setAdapter(listaAdaptador);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;

                linearLayout.setLayoutParams(params);
                listView.setLayoutParams(params);

                Button button = new Button(getApplicationContext());
                button.setText(R.string.new_search);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (frameLayout != null && linearLayout != null)
                            frameLayout.removeView(linearLayout);
                    }
                });

                linearLayout.addView(button);
                linearLayout.addView(listView);
                frameLayout.addView(linearLayout);
                searchFind = true;
                Toast.makeText(getBaseContext(), i + getString(R.string.foram_encontrados), Toast.LENGTH_LONG).show();


            } else {
                searchFind = false;
                Toast.makeText(getBaseContext(), R.string.nada_encontrado, Toast.LENGTH_LONG).show();

            }


            progressBarBusca.setVisibility(View.GONE);
            linearLayoutBusca.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPreExecute() {

            linearLayoutBusca.setVisibility(View.INVISIBLE);
            progressBarBusca.setVisibility(View.VISIBLE);


            Boolean modoNoturno = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("noturnoPref", true);

            if (modoNoturno) {
                linearLayout.setBackgroundColor(getResources().getColor(R.color.black));
            } else {
                linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
            }


        }

        protected void onProgressUpdate(Integer... values) {


        }


    }


}
