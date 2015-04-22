package com.liepooh.dict.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NoticeCloseBroadCast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent closeBarIntent = new Intent(context, com.liepooh.dict.main.NotificationBarService.class);
        context.stopService(closeBarIntent);
        
        Intent closeShortcutIntent = new Intent(context, com.liepooh.dict.main.ShortcutService.class);
        context.stopService(closeShortcutIntent);
    }
}
