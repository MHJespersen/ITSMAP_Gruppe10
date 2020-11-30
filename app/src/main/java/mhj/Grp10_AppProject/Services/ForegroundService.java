package mhj.Grp10_AppProject.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mhj.Grp10_AppProject.Model.Repository;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.Utilities.Constants;

//Foreground service. Inspired by the demo from lesson 5.
public class ForegroundService extends Service {

    ExecutorService execService;
    boolean started = false;
    private final Repository repo =  Repository.getInstance(this);

    public ForegroundService() {
    }
    @Override
    public void onCreate() {
        Log.d(Constants.SERVICE_CREATE, "In service onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(Constants.SERVICE_START, "In service onStartCommand");

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(Constants.SERVICE_CHANNEL, "Foreground Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, Constants.SERVICE_CHANNEL)
                .setContentTitle("Foreground service started")
                .setSmallIcon(R.drawable.ic_service_draw)
                .build();

        startForeground(Constants.NOTIFICATION_ID, notification);

        StartThread();
        return START_STICKY;
    }

    private Notification getMyActivityNotification(String text){

        Notification notification = new NotificationCompat.Builder(this, Constants.SERVICE_CHANNEL)
                .setContentTitle(text)
                .setSmallIcon(R.drawable.ic_service_draw)
                .build();

        return notification;
    }

    private void StartThread(){
        if(!started) {
            started = true;
            RecursiveWork();
        }
    }

    private void RecursiveWork(){
        if(execService == null){
            execService = Executors.newSingleThreadExecutor();
        }
        execService.submit(new Runnable() {
            @Override
            public void run() {
                //Check
                //And call UpdateNotification with an update
                if(started) {
                    RecursiveWork();
                }
            }
        });
    }

    private void UpdateNotification(String message)
    {
        //Update notification on phone
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}