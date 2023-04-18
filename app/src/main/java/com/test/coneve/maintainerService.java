package com.test.coneve;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class maintainerService extends Service {


    public MutableLiveData<FirebaseUser> profile;
    //TODO :  Add event data

    public maintainerService() {

    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

    }
}