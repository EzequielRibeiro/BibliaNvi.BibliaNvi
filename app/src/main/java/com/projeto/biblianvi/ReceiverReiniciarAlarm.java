package com.projeto.biblianvi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.projeto.biblianvi.TimeClock.agendarAlarmeVersiculo;
import static com.projeto.biblianvi.TimeClock.checarAlarmeExiste;

/**
 * Created by Ezequiel on 25/05/2016.
 */

public class ReceiverReiniciarAlarm extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            this.context = context;
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                if (!checarAlarmeExiste(context))
                    agendarAlarmeVersiculo(context);
                Log.e("Broadcast", "Iniciado");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
