package com.test.coneve;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class maintainerService extends Service {
    public maintainerService() {
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}