package com.projeto.biblianvi;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.projeto.biblianvi.biblianvi.R;

import java.text.ParseException;

/**
 * Created by Ezequiel on 25/05/2016.
 */

public class VersiculoDiario extends BroadcastReceiver {

    String assunto;
    String livro;
    String cap;
    String vers;
    private int notifyID = 0;
    private BibliaBancoDadosHelper bibliaHelp;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        try {
            if (MainActivity.isDataBaseDownload(context))
                criarNotification();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void versiculoDoDia() throws ParseException {

        bibliaHelp = new BibliaBancoDadosHelper(context);

        SharedPreferences settings;

        bibliaHelp.versDoDiaText();
        settings = context.getSharedPreferences("versDiaPreference", Activity.MODE_PRIVATE);
        assunto = settings.getString("assunto", "...");
        livro = settings.getString("livroNome", "...");
        cap = settings.getString("capVersDia", "...");
        vers = settings.getString("verVersDia", "...");

    }

    private void criarNotification() throws ParseException {
        versiculoDoDia();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(context.getString(R.string.versiculo_do_dia) + assunto + " (" + livro + " " + cap + ":" + vers + ")");

        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notifyID, mBuilder.build());

    }
}
