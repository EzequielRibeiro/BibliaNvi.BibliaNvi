package com.projeto.biblianvi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.projeto.biblianvi.biblianvi.R;

import java.util.List;

public class Grafico_Dois extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTheme(android.R.style.Widget_Holo_Light);

        setContentView(R.layout.activity_grafico_dois);
        BibliaBancoDadosHelper bibliaBancoDadosHelper = new BibliaBancoDadosHelper(getApplicationContext());

        ListView myListView = (ListView) findViewById(R.id.list_grafico_dois);
        List<Biblia> allBooks = bibliaBancoDadosHelper.getSumAllVersosReadByTestament(2);
        ListGraficoAdapter listGraficoAdapter = new ListGraficoAdapter(getApplicationContext(), allBooks);
        myListView.setAdapter(listGraficoAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_grafico_um, menu);
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            /*
            case R.id.action_settings:
                Intent settingsActivity = icon_new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            case R.id.action_devocional:
                Intent in = icon_new Intent(getApplicationContext(),NetworkActivityDevocional.class);
                startActivity(in);
                return true;
                */
            case R.id.action_exit:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
