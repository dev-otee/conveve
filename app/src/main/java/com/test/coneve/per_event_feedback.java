package com.test.coneve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class per_event_feedback extends AppCompatActivity {
    String eventID;
    servicedataInterface dataInterface;
    DatabaseReference fbdb;
    HashMap<String,feedback> feedbacklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_event_feedback);
        Intent intent = getIntent();
        Intent serviceIntent = new Intent(this,maintainerService.class);
        bindService(serviceIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ProfileData pdata = ((servicedataInterface)iBinder).getProfileData().getValue();
                if(pdata.getOrganiser())
                {
                    ((TextView)findViewById(R.id.feedback)).setVisibility(View.GONE);
                    ((Button)findViewById(R.id.postFeedback)).setVisibility(View.GONE);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, 0);
        feedbacklist = new HashMap<String,feedback>();
        eventID = intent.getStringExtra(getString(R.string.packageID)+getString(R.string.eventID));
        fbdb = FirebaseDatabase.getInstance().getReference("Events_Feedback").child(eventID);
        fbdb.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    fbdb.setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            }
        });
        ((Button)findViewById(R.id.postFeedback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     fbdb.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                         @Override
                         public void onSuccess(DataSnapshot dataSnapshot) {
                             int len =(int) dataSnapshot.getChildrenCount();
                             String key = "fb"+(len+1);
                            feedback fdbk = new feedback();
                            fdbk.setMessage(((EditText)findViewById(R.id.feedback)).getText().toString());
                            fdbk.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            fbdb.child(key).setValue(fdbk).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            });
                         }
                     });
            }
        });
        RecyclerView feedbackrv = ((RecyclerView)findViewById(R.id.feedbackRV));
        feedbackRVA adpater = new feedbackRVA();
        feedbackrv.setAdapter(adpater);
        feedbackrv.setLayoutManager(new LinearLayoutManager(this));
        fbdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> list = snapshot.getChildren();
                for (DataSnapshot fdback:
                     list) {
                    feedbacklist.put(fdback.getKey(),fdback.getValue(feedback.class));
                }
                adpater.setData(feedbacklist.values());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}