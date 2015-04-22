package com.liepooh.dict.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NoticeOFFBroadCast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent serviceIntent = new Intent(context, com.liepooh.dict.main.ShortcutService.class);
        context.stopService(serviceIntent);
    }
}
