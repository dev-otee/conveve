package com.test.coneve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;

public class PerEventView extends AppCompatActivity {
    String eventId;
    EventsDataModel event;
    FirebaseUser profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_event_view);
        Intent startIntent = getIntent();
        eventId = startIntent.getStringExtra(getString(R.string.packageID)+getString(R.string.eventID));
        ((ImageView)findViewById(R.id.attend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(getApplicationContext(),maintainerService.class);

            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent serviceIntent = new Intent(this,maintainerService.class);
        bindService(serviceIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                servicedataInterface Interface = (servicedataInterface) iBinder;
                event = Interface.getEvetsObserver().getValue().get(eventId);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, 0);
    }
}