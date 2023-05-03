package com.test.coneve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.net.URL;

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
//                Log.d("MyLog","Event fetched");

                ((TextView) findViewById(R.id.time_event)).setText("Event Time: "+event.starttime+" - "+event.endtime);
                ((TextView) findViewById(R.id.eventDisplayName)).setText("About: "+event.getName());
                ((TextView) findViewById(R.id.Description)).setText(event.getDescription());

                event.eventPoster.observe(PerEventView.this, new Observer<Bitmap>() {
                    @Override
                    public void onChanged(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.image_poster)).setImageBitmap(bitmap);
                    }
                });

                Button registerBtn = (Button) findViewById(R.id.register);

                String url = event.getReglink();
                Log.d("MyLog",url);
                if(url == null || !isValidUrl(url)) {
                    registerBtn.setVisibility(View.GONE);
                }

                registerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(url);
                        Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(urlIntent);
                    }
                });

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, 0);
    }

    public boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}