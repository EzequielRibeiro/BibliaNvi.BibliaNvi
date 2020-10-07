/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.projeto.biblianvi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.projeto.biblianvi.biblianvi.R;

import java.util.concurrent.ExecutorService;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {


    private ExecutorService executor = null;
    private ProgressDialog p;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        // Loads the XML preferences file.
        addPreferencesFromResource(R.xml.preferences);
        setTheme(android.R.style.Widget_Holo_Light);


        Preference buttonLimparVersLido = findPreference("buttonLimparLidos");
        Preference alterFont = findPreference("fonteEstilo");
        Preference alterTamFonte = findPreference("fontePref");


        alterFont.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {


                SharedPreferences.Editor ed = preference.getEditor();
                ed.putString("fonteEstilo", (String) newValue);
                ed.commit();


                SharedPreferences sp = getSharedPreferences("altPref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("alteracao", true);
                editor.commit();


                return false;
            }
        });

        alterTamFonte.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {


                SharedPreferences.Editor ed = preference.getEditor();
                ed.putString("fontePref", (String) newValue);
                ed.commit();

                SharedPreferences sp = getSharedPreferences("altPref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("alteracao", true);
                editor.commit();

                return false;
            }
        });


        buttonLimparVersLido.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("AVISO !!! Isso irá limpar as estatística.");


                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new BibliaBancoDadosHelper(getApplicationContext()).limparVersLidos();

                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                builder.show();


                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Registers a callback to be invoked whenever a user changes a preference.
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregisters the listener set in onResume().
        // It's best practice to unregister listeners when your app isn't using them to cut down on
        // unnecessary system overhead. You do this in onPause().
        getPreferenceScreen()
                .getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        if (p != null && p.isShowing())
            p.cancel();

    }

    protected void onDestroy() {
        super.onDestroy();


    }

    // Fires when the user changes a preference.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Sets refreshDisplay to true so that when the user returns to the tela_browser
        // activity, the display refreshes to reflect the icon_new settings.
        NetworkActivityDevocional.refreshDisplay = true;
    }


}
