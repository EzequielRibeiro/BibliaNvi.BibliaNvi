package com.projeto.biblianvi;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Ezequiel on 25/05/2016.
 */

public class ReceiverReiniciarAlarm extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            agendarAlarmeVersiculo();
            Log.e("Broadcast", "Iniciado");
        }

    }


    private void agendarAlarmeVersiculo() {

        Intent it = new Intent(context, VersiculoDiario.class);
        PendingIntent p = PendingIntent.getBroadcast(context, 101015, it, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SharedPreferences settings = context.getSharedPreferences("alarme", Activity.MODE_PRIVATE);

        int h = Integer.parseInt(settings.getString("hora", "10"));
        int m = Integer.parseInt(settings.getString("minuto", "30"));

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.MINUTE, m);
        //c.set(Calendar.SECOND, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, p);

    }

}
