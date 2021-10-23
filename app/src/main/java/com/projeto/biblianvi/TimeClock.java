package com.projeto.biblianvi;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.projeto.biblianvi.biblianvi.R;

import java.util.Calendar;

public class TimeClock extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static int hour = 10;
    private static int minute = 30;
    private static final int requestCode = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        try {
            hour = getActivity().getSharedPreferences("alarme", Activity.MODE_PRIVATE).getInt("hora", 10);
            minute = getActivity().getSharedPreferences("alarme", Activity.MODE_PRIVATE).getInt("minuto", 30);

        } catch (ClassCastException castException) {

            hour = Integer.valueOf(getActivity().getSharedPreferences("alarme", Activity.MODE_PRIVATE).getString("hora", "10"));
            minute = Integer.valueOf(getActivity().getSharedPreferences("alarme", Activity.MODE_PRIVATE).getString("minuto", "30"));

        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.setTitle(getString(R.string.confirmar_alarme_aviso));

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.e("Time:: ", i + ":" + i1);

        getActivity().getSharedPreferences("alarme", Activity.MODE_PRIVATE).edit().putInt("hora", i).commit();
        getActivity().getSharedPreferences("alarme", Activity.MODE_PRIVATE).edit().putInt("minuto", i1).commit();

        if (checarAlarmeExiste(getActivity())) {
            cancelarAgendarAlarmeVersiculo();
        }
        agendarAlarmeVersiculo(getActivity());

        Toast.makeText(getActivity(), getString(R.string.hora_redefinida)
                        + " " + i + ":"
                        + i1 + "h"
                , Toast.LENGTH_LONG).show();

    }


    public static void agendarAlarmeVersiculo(Context context) {


        SharedPreferences settings = context.getSharedPreferences("alarme", Activity.MODE_PRIVATE);

        if (!settings.contains("hora") || !settings.contains("minuto")) {
            settings.edit().putInt("hora", 10).commit();
            settings.edit().putInt("minuto", 30).commit();
        }

        try {
            hour = context.getSharedPreferences("alarme", Activity.MODE_PRIVATE).getInt("hora", 10);
            minute = context.getSharedPreferences("alarme", Activity.MODE_PRIVATE).getInt("minuto", 30);

        } catch (ClassCastException castException) {

            hour = Integer.valueOf(context.getSharedPreferences("alarme", Activity.MODE_PRIVATE).getString("hora", "10"));
            minute = Integer.valueOf(context.getSharedPreferences("alarme", Activity.MODE_PRIVATE).getString("minuto", "30"));

            context.getSharedPreferences("alarme", Activity.MODE_PRIVATE).edit().remove("hora").commit();
            context.getSharedPreferences("alarme", Activity.MODE_PRIVATE).edit().remove("minute").commit();

            context.getSharedPreferences("alarme", Activity.MODE_PRIVATE).edit().putInt("hora", hour).commit();
            context.getSharedPreferences("alarme", Activity.MODE_PRIVATE).edit().putInt("minuto", minute).commit();

        }

        Intent it = new Intent(context, VersiculoDiario.class);
        PendingIntent p = PendingIntent.getBroadcast(context, requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, p);
        Log.e("alarme ", "agendarAlarmeVersiculo");
        checarAlarmeExiste(context);

    }


    public static boolean checarAlarmeExiste(Context context) {

        Intent tempIntent = new Intent(context, VersiculoDiario.class);
        tempIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boolean alarmUp = (PendingIntent.getBroadcast(context, requestCode, tempIntent, PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp)
            Log.e("alarme ", "ativado");
        else {
            Log.e("alarme ", "desativado");
        }
        return alarmUp;

    }

    private void cancelarAgendarAlarmeVersiculo() {

        Intent intent = new Intent(getActivity(), VersiculoDiario.class);
        AlarmManager alarmManager =
                (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(getContext(), requestCode, intent,
                        PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.e("alarme ", "cancelado");
        } else
            Log.e("alarme ", "não cancelado");


    }

}
