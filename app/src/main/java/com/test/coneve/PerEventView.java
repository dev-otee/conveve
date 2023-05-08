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
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
                Intent calendarIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, event.getName())
                        .putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getVenue())
                        .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getTimeInMillis(event.getStartDate(), event.getStarttime()))
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getTimeInMillis(event.getEndDate(), event.getEndtime()));

                startActivity(calendarIntent);

            }
        });
        ((Button)findViewById(R.id.addFeedback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),per_event_feedback.class);
                intent.putExtra(getString(R.string.packageID)+getString(R.string.eventID),eventId);
                startActivity(intent);
            }
        });
    }

    private long getTimeInMillis(String inputDate, String inputTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        Date mDate = null;

        try {
            mDate = dateFormat.parse(inputDate+" "+inputTime);
        } catch (ParseException e) {
            Toast.makeText(this, "Error in getting event date", Toast.LENGTH_SHORT).show();
        }

        return mDate.getTime();
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

                TextView time = (TextView) findViewById(R.id.time_event);
                time.setText("Event Time: \n"+event.starttime+" - "+event.endtime);
                TextView displayName = (TextView) findViewById(R.id.eventDisplayName);
                displayName.setText("About: "+event.getName());
                TextView description = (TextView) findViewById(R.id.Description);
                description.setText(event.getDescription());
                TextView date = (TextView) findViewById(R.id.date_event);
                // set date
                if(event.getEndDate() == null || event.getEndDate().equals(event.getStartDate())){
                    date.setText("Event Date:\n"+event.getStartDate());
                } else{
                    date.setText("Event Date:\n"+event.getStartDate()+" - "+event.getEndDate());
                }

                event.eventPoster.observe(PerEventView.this, new Observer<Bitmap>() {
                    @Override
                    public void onChanged(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.image_poster)).setImageBitmap(bitmap);
                    }
                });

                Button registerBtn = (Button) findViewById(R.id.register);
                Button addFeedback = (Button) findViewById(R.id.addFeedback);
                ImageView share = (ImageView) findViewById(R.id.share_event);

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

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /// Concatenate the texts into a single string
                        String combinedText = "Hey! Checkout this event happening at IIIT Delhi \n"
                                +"\nEvent: "+event.getName()
                                +"\nOn: "+event.getStartDate()
                                +"\nTime: "+event.getStarttime()+" - "+event.getEndtime()
                                +"\nAbout: \n"+event.getDescription()
                                +"\n\n\nShared via Conveve App";

                        // Create the intent for sharing
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, combinedText);

                        startActivity(Intent.createChooser(intent, "Share text via: "));


                    }
                });

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, 0);
    }

//    private boolean compareWithCurrentTime(String endDate, String endTime) {
//        // Get the current date and time
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//        // Define the date and time format patterns
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//
//        // Parse the given date and time strings into LocalDateTime objects
//        LocalDateTime givenDateTime = LocalDateTime.parse(endDate + " " + endTime,
//                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
//
//        // Compare the current date and time with the given date and time
//        int comparisonResult = currentDateTime.compareTo(givenDateTime);
//
//        if (comparisonResult>0) return true;
//
//        return false;
//    }

    public boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}