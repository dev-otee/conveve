package com.test.coneve;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class maintainerService extends Service {



    public MutableLiveData<FirebaseUser> profile;
    public MutableLiveData<HashMap<String,EventsDataModel>> events;

    private boolean activemode;
    private int bindings;
    private activeEventObserver actobserver;

    private DatabaseReference root;

    public maintainerService() {
        bindings = 0;
        events.setValue(new HashMap<String,EventsDataModel>());
        profile.setValue(null);
        actobserver = new activeEventObserver();
        root = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        return START_STICKY;
    }

    class activeEventObserver implements ValueEventListener{

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Iterable<DataSnapshot> snapshots = snapshot.getChildren();
            HashMap<String,EventsDataModel> replacement = new HashMap<String,EventsDataModel>();
            HashMap<String,EventsDataModel> current = events.getValue();
            for (DataSnapshot event_raw:
                    snapshots) {
                EventsDataModel temp = event_raw.getValue(EventsDataModel.class);
                EventsDataModel current_model = current.get(event_raw.getKey());
                replacement.put(event_raw.getKey(),temp);
            }
            events.setValue(replacement);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        bindings++;
        if(bindings == 1)
        {
            root.child(getString(R.string.path_firebase_event)).addValueEventListener(actobserver);
        }
        return new servicedataInterface(this);
    }

    @Override
    public boolean onUnbind (Intent intent)
    {
        bindings--;
        if(bindings == 0)
        {
            root.child(getString(R.string.path_firebase_event)).removeEventListener(actobserver);
        }
        return false;
    }
}