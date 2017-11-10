package com.afexplore.util.keepalive;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * Since Espresso does not run in UIThread, but further uses it when needed, HomeActivity is destroyed
 * when opening a Chrome custom tab during instrumented test. So the starting intent will be connected
 * to this empty service in order to keep it alive during the interaction time with the custom tab.
 * */
public class KeepAliveService extends Service {

    private static final Binder sBinder = new Binder();

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }
}
