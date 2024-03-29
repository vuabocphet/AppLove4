package com.tinh.dev.applove.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


import com.tinh.dev.applove.MainActivity;
import com.tinh.dev.applove.R;
import com.tinh.dev.applove.dataBase.DataBase;

import java.util.Calendar;


public class ExampleServicea extends Service {
    public static final String CHANNEL_ID = "exampleServiceChannel";
    public static final int NOTIFICATION_ID = 001;

    private DataBase dataBase;
    private RemoteViews views;




    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(ExampleServicea.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        NotificationCompat.Builder notificationCompat=new NotificationCompat.Builder(this,CHANNEL_ID);
        notificationCompat.setSmallIcon(R.drawable.boy);
        notificationCompat.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        views=new RemoteViews(getPackageName(), R.layout.custon_layout);

        dataBase=new DataBase(this);
        getDataName();
        updateNgayyeu();
        notificationCompat.setContentIntent(pendingIntent);





        notificationCompat.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        notificationCompat.setCustomBigContentView(views);

        NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,notificationCompat.build());
        startForeground(1,notificationCompat.build());
        Log.e("Oki","Oki");
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(serviceChannel);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getDataName() {
        Cursor cursor = dataBase.getTen();
        if (cursor.moveToNext()) {
            String nameboy = cursor.getString(1);
            String namegirl = cursor.getString(2);
            Typeface typeface=Typeface.createFromAsset(getAssets(),"love_girl.ttf");
            views.setTextViewText(R.id.txta, nameboy);
            views.setTextViewText(R.id.txtb,namegirl);
        }
    }

    public void getDataDate() {

        Cursor cursor = dataBase.getSoNgayYeu();
        if (cursor.moveToNext()) {
            int datelove = cursor.getInt(1);
            if (datelove == 0) {
                views.setTextViewText(R.id.txtc,"Start love");
            } else {
                views.setTextViewText(R.id.txtc,String.valueOf(datelove) + "\tngày" + "\n\t\tyêu");
            }

        }

    }
    public void updateNgayyeu() {
       Cursor cursor4 = dataBase.getNgayyeu();
        Calendar calendarTwo = Calendar.getInstance();
        int a1 = (int) (calendarTwo.getTimeInMillis() / (1000 * 60 * 60 * 24));
        if (cursor4.moveToNext()) {
            int a2 = cursor4.getInt(1);
            int a = a1 - a2;
            dataBase.updateSoNgayYeu(a, 1);

        }
        getDataDate();
    }




}
