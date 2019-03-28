package com.swaroop.navigation;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static com.swaroop.navigation.HomesData.lats;

public class MyService extends Service {
    int i =0;
     Runnable runnableCode;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //notificationDialog();
        final Handler handler = new Handler();
// Define the code block to be executed
         runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                //Log.d("Handlers", "Called on main thread");
                //notificationDialog();
                Log.i("data", String.valueOf(lats));
                if(!lats.isEmpty() && i==0) {
                    notificationDialog();
                    i = 1;
                    //Toast.makeText(MyService.this, "The new Service was Created", Toast.LENGTH_LONG).show();
                    // Repeat this the same runnable code block again another 2 seconds
                    // 'this' is referencing the Runnable object
                    Log.i("i", String.valueOf(i));
                    //handler.removeCallbacks(runnableCode);
                }
                handler.postDelayed(this, 10000);
            }
        };
// Start the initial runnable task by posting through the handler

            handler.post(runnableCode);


        /*Log.i("data", String.valueOf(lats));
        Toast.makeText(MyService.this, "The new Service was Created", Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //notificationDialog();
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }
    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.house)
                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Homes.com")
                .setContentText("Check out Homes on your way")
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
    }
}