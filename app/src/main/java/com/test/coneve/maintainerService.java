package com.test.coneve;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
//TODO:handle transitiont to passive of eventobserver

public class maintainerService extends Service {



    public MutableLiveData<FirebaseUser> profile;
    public MutableLiveData<HashMap<String,EventsDataModel>> events;
    public MutableLiveData<HashMap<String,Tag>> tagList;

    private boolean activemode;
    private int bindings;
    private activeEventObserver actobserver;
    private activeTagObserver tagObserver;
    private DatabaseReference root;

    public maintainerService() {
        bindings = 0;
        events = new MutableLiveData<HashMap<String,EventsDataModel>>();
        events.setValue(new HashMap<String,EventsDataModel>());

        profile = new MutableLiveData<FirebaseUser>();
        profile.setValue(null);
        actobserver = new activeEventObserver();
        root = FirebaseDatabase.getInstance().getReference();
        tagList = new MutableLiveData<HashMap<String,Tag>>();
        tagList.setValue(new HashMap<String,Tag>());
        tagObserver = new activeTagObserver();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {

        FirebaseDatabase.getInstance().getReference("Events").addValueEventListener(actobserver);

        return START_STICKY;
    }

    class activeTagObserver implements ValueEventListener{

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Iterable<DataSnapshot> taglist = snapshot.getChildren();
            HashMap<String,Tag> ctaglist = tagList.getValue();
            for (DataSnapshot tagraw:
                 taglist) {
                Tag temp = tagraw.getValue(Tag.class);
                ctaglist.put(tagraw.getKey(),temp);
            }
            tagList.setValue(ctaglist);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }
    class activeEventObserver implements ValueEventListener{

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Iterable<DataSnapshot> snapshots = snapshot.getChildren();
            HashMap<String,EventsDataModel> current = events.getValue();
            for (DataSnapshot event_raw:
                    snapshots) {
                EventsDataModel temp = event_raw.getValue(EventsDataModel.class);
                EventsDataModel current_model = current.get(event_raw.getKey());
                if(current_model == null)
                    current.put(event_raw.getKey(),event_raw.getValue(EventsDataModel.class));
                else {
                    int comp_word = current_model.compare(temp);
                    if (comp_word > 0) {
                        current_model.update(comp_word,temp);
                    }
                }
                Log.d("check","trying");
            }
            events.setValue(current);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("event error",error.getDetails());
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        bindings++;
        if(bindings == 1)
        {
            root.child(getString(R.string.path_firebase_event)).addValueEventListener(actobserver);
            root.child("Tags").addValueEventListener(tagObserver);
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
            root.child("Tags").removeEventListener(tagObserver);
        }
        return false;
    }
}