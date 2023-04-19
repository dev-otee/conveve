package com.test.coneve;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.lifecycle.MutableLiveData;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class maintainerService extends Service {

    private int bindings;
    public MutableLiveData<FirebaseUser> profile;
    //TODO :  Add event data

    public maintainerService() {
        bindings = 0;
        FirebaseDatabase.getInstance().getReference("Events");
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        bindings++;

        return new servicedataInterface(this);
    }

    @Override
    public boolean onUnbind (Intent intent)
    {
        bindings--;
        return false;
    }
}