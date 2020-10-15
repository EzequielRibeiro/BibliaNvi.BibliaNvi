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
    public static final String NOTIFICATION_CHANNEL_ID = "101016";
    private SharedPreferences settings;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        settings = context.getSharedPreferences("versDiaPreference", Activity.MODE_PRIVATE);

        try {
            if (MainActivity.isDataBaseDownload(context))
                versiculoDoDia();
                criarNotification();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void versiculoDoDia() throws ParseException {

        bibliaHelp = new BibliaBancoDadosHelper(context);
        VersDoDia versDoDia;
    
       do{
           versDoDia = bibliaHelp.getVersDoDia();
         SharedPreferences.Editor editor = settings.edit();
         editor.putString("assunto", versDoDia.getAssunto());
         editor.putString("versDia", versDoDia.getText());
         editor.putString("livroNome", versDoDia.getBooksName());
         editor.putString("capVersDia", versDoDia.getChapter());
         editor.putString("verVersDia", versDoDia.getVersesNum());
         editor.commit();

       }while (versDoDia.getText().isEmpty());

    }

    private void criarNotification() throws ParseException {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int importance = NotificationManager.IMPORTANCE_LOW;
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
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Bible", importance);
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
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setLargeIcon(bipmap)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setSubText(context.getString(R.string.versiculo_do_dia))
                        .setContentText(settings.getString("assunto", " ") +
                                " - " + settings.getString("livroNome", " ") +
                                " " + settings.getString("capVersDia", " ") +
                                ":" + settings.getString("verVersDia", " ") + " ");

        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notifyID, mBuilder.build());

    }
}
