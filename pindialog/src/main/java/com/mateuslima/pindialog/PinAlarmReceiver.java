package com.mateuslima.pindialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PinAlarmReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("INFO","onReceive sucesso");
        context.sendBroadcast(new Intent("openDialog"));
    }
}
