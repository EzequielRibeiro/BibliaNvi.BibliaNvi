package com.projeto.biblianvi;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

import com.projeto.biblianvi.BibliaBancoDadosHelper.VersDoDia;
import com.projeto.biblianvi.biblianvi.R;
import java.text.ParseException;

/**
 * Created by Ezequiel on 25/05/2016.
 */

public class VersiculoDiario extends BroadcastReceiver {
    private int notifyID = 125;
    private BibliaBancoDadosHelper bibliaHelp;
    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        settings = context.getSharedPreferences("versDiaPreference", Activity.MODE_PRIVATE);
        editor = settings.edit();

        try {
            if (MainActivity.isDataBaseDownload(context))
                versiculoDoDia();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void versiculoDoDia() throws ParseException {

        bibliaHelp = new BibliaBancoDadosHelper(context);
        VersDoDia versDoDia;
        int id = settings.getInt("id", -1);

        do {
            versDoDia = bibliaHelp.getVersDoDia();
        } while (versDoDia.getIdSelecionado() == id || versDoDia.getText().isEmpty());

        editor.clear();
        editor.putInt("id", versDoDia.getIdSelecionado());
        editor.putString("assunto", versDoDia.getAssunto());
        editor.putString("versDia", versDoDia.getText());
        editor.putString("livroNome", versDoDia.getBooksName());
        editor.putString("capVersDia", versDoDia.getChapter());
        editor.putString("verVersDia", versDoDia.getVersesNum());
        editor.commit();
        criarNotification(versDoDia);

    }

    private void criarNotification(VersDoDia versDoDia) throws ParseException {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager;

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(MainActivity.IDCHANNEL, MainActivity.CHANNELNAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bitmap bipmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.large_icon_bible);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, MainActivity.IDCHANNEL)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setLargeIcon(bipmap)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setSubText(context.getString(R.string.versiculo_do_dia))
                        .setContentText(versDoDia.getAssunto() +
                                " - " + versDoDia.getBooksName() +
                                " " + versDoDia.getChapter() +
                                ":" + versDoDia.getVersesNum() + " ");

        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notifyID, mBuilder.build());

    }
}
