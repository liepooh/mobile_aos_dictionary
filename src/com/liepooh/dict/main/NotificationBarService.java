package com.liepooh.dict.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.RemoteViews;

public class NotificationBarService extends Service
{
    /*
     *---------------------------------------------------------
     *                    
     *                     공          통
     *                     
     *---------------------------------------------------------
     */
    final public String TAG = "NotificationBarService";
    
    /*
     *---------------------------------------------------------
     *                    
     *                     액티비티 오버라이딩
     *                     
     *---------------------------------------------------------
     */
    @Override
    public void onCreate()
    {
        Log.i(TAG, "onCreate()");
        
        Intent showIntect = new Intent(this, NoticeONBroadCast.class);
        PendingIntent showPIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, showIntect, 0);
        
        Intent hideIntect = new Intent(this, NoticeOFFBroadCast.class);
        PendingIntent hidePIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, hideIntect, 0);
        
        Intent closeIntect = new Intent(this, NoticeCloseBroadCast.class);
        PendingIntent closePIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, closeIntect, 0);
        
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.service_notificationbar);
        remoteViews.setOnClickPendingIntent(R.id.button_notice_on, showPIntent);
        remoteViews.setOnClickPendingIntent(R.id.button_notice_off, hidePIntent);
        remoteViews.setOnClickPendingIntent(R.id.button_notice_close, closePIntent);
                
        Builder noticeBuilder = new NotificationCompat.Builder(this);
        noticeBuilder.setContentTitle("Test");
        noticeBuilder.setContentText("Test");
        noticeBuilder.setSmallIcon(R.drawable.ic_launcher);
        noticeBuilder.setContent(remoteViews);
        Notification notification = noticeBuilder.build();
        
        startForeground(1234, notification);
        
        super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i(TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onDestroy()
    {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i(TAG, "onBind()");
        return null;
    }
}
