package com.test.coneve;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class OrganizerActivity extends AppCompatActivity {

    TextView name;
    TextView startdate,enddate;
    TextView starttime;
    TextView endtime;
    TextView RegistrationLink;
    TextView Venue;
    TextView description;

    Uri imagelink;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    OrganizerActivity ref;
    TagWord tword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);
        imagelink = null;
        name = findViewById(R.id.nameAnswer);
        startdate = findViewById(R.id.dateAnswer);
        enddate = findViewById(R.id.dateAnswer2);
        starttime = findViewById(R.id.starttimeAnswer);
        endtime = findViewById(R.id.endtimeAnswer);
        RegistrationLink = findViewById(R.id.reglinkAnswer);
        Venue = findViewById(R.id.venueAnswer);
        description = findViewById(R.id.descriptionAnswer);
        ((Button)findViewById(R.id.datePickerStart)).setOnClickListener(new choosestartdateonclick());
        ((Button)findViewById(R.id.datePickerEnd)).setOnClickListener(new chooseenddateonclick());
        ((Button)findViewById(R.id.starttimePicker)).setOnClickListener(new chooosestarttimeonclick());
        ((Button)findViewById(R.id.endtimePicker)).setOnClickListener(new choooseendtimeonclick());
        ((ImageView)findViewById(R.id.uploadImageField)).setOnClickListener(new uploadImageonclick());
        ((Button)findViewById(R.id.org_create)).setOnClickListener(new createEvent());
        tword = new TagWord();
        ref = this;
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        try{
                            ((ImageView) findViewById(R.id.uploadImageField)).setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)));
                        }catch (Exception exception)
                        {
                            Log.d("urifetchsuccess","replacing placeholder with image",exception);
                        }
                        imagelink = uri;
                    } else {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        Toast.makeText(this, "unable to get image", Toast.LENGTH_SHORT).show();
                    }
                });
        RecyclerView TagsRecyclerview = ((RecyclerView)findViewById(R.id.recyclerViewTags));
        TagsRecyclerview.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.HORIZONTAL,false));
        TagsRVAdapter tagadapter = new TagsRVAdapter(this, new Callback<Tag>() {
            @Override
            public void callback(Tag object) {
                tword.addTag(object);
            }
        }, new Callback<Tag>() {
            @Override
            public void callback(Tag object) {
                tword.removeTag(object);
            }
        });
        TagsRecyclerview.setAdapter(tagadapter);
        Intent bindingservice = new Intent(getBaseContext(),maintainerService.class);
        bindService(bindingservice, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ((servicedataInterface)iBinder).getTagList().observeForever(new Observer<HashMap<String, Tag>>() {
                    @Override
                    public void onChanged(HashMap<String, Tag> stringTagHashMap) {
                        tagadapter.setData(stringTagHashMap.values());
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, 0);
    }



    class choosestartdateonclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(ref, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    startdate.setVisibility(View.VISIBLE);
                    if(startdate.getText().toString()==null){
                        Toast.makeText(ref, "Please choose a start date!", Toast.LENGTH_SHORT).show();
                    }else {

                        // Format the selected date as "yyyy/MM/dd"
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(i, i1, i2);
                        String formattedDate = dateFormat.format(calendar.getTime());
                        Log.d("DatePicker", formattedDate);

                        startdate.setText(formattedDate);
                    }
                }
            },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();

        }
    }

    class chooseenddateonclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(ref, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    enddate.setVisibility(View.VISIBLE);
                    if(enddate.getText().toString()==null){
                        Toast.makeText(ref, "Please choose an end date!", Toast.LENGTH_SHORT).show();
                    }else {
                        // Format the selected date as "yyyy/MM/dd"
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(i, i1, i2);
                        String formattedDate = dateFormat.format(calendar.getTime());

                        enddate.setText(formattedDate);
                    }
                }
            },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();

        }
    }

    class chooosestarttimeonclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(ref, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    starttime.setVisibility(View.VISIBLE);
                    if(starttime.getText().toString()==null){
                        Toast.makeText(ref, "Please choose a start time!", Toast.LENGTH_SHORT).show();
                    }else{
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", i, i1);
                        Log.d("TimePicker",selectedTime);
                        starttime.setText(selectedTime);
                    }

                }
            },c.get(Calendar.HOUR),c.get(Calendar.MINUTE), DateFormat.is24HourFormat(getBaseContext())).show();

        }
    }
    class choooseendtimeonclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(ref, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    endtime.setVisibility(View.VISIBLE);
                    if(endtime.getText().toString()==null){
                        Toast.makeText(ref, "Please choose an end time!", Toast.LENGTH_SHORT).show();
                    }else {
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", i, i1);
                        endtime.setText(selectedTime);
                    }
                }
            },c.get(Calendar.HOUR),c.get(Calendar.MINUTE), DateFormat.is24HourFormat(getBaseContext())).show();

        }
    }
    class uploadImageonclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .build());
        }
    }


    boolean isClear(TextView[] textViews)
    {
        for(TextView txtview : textViews)
        {
            if(txtview.getText().length()==0)
                return false;
        }
        return  true;
    }
    class createEvent implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            EventsDataModel model = new EventsDataModel();
            TextView txt[] = {name,startdate,enddate,starttime,endtime,endtime,Venue,description};
            if(isClear(txt)&&imagelink!=null)
            {

                EventsDataModel model1 = new EventsDataModel();
                model1.setStartDate(startdate.getText().toString());
                model1.setEndDate(enddate.getText().toString());
                model1.setDescription(description.getText().toString());
                model1.setReglink(RegistrationLink.getText().toString());
                model1.setName(name.getText().toString());
                //model1.setPimageid(piclink);
                model1.setVenue(Venue.getText().toString());
                model1.setStarttime(starttime.getText().toString());
                model1.setEndtime(endtime.getText().toString());
                model1.tags.setTagWord(tword);
                DatabaseReference fbs = FirebaseDatabase.getInstance().getReference("Events");
                fbs.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot snapshot) {
                        String id =  (((Integer)(int)snapshot.getChildrenCount()).toString());
                        byte[] bytes = null;

                        try {

                            InputStream is = getContentResolver().openInputStream(imagelink);
                            bytes = new byte[is.available()];
                            is.read(bytes);

                        }catch (Exception exception)
                        {
                            Log.d("uploadingimage","unable to open image",exception);
                        }
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("event_icons").child("images"+id+".jpg");
                        storageReference.putBytes(bytes);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                model1.setPimageid(uri.toString());
                                model1.setId(String.valueOf(id));
                                fbs.child(id).setValue(model1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        finish();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("imageuridownlaod","failed",e);
                            }
                        });
                        //fbs.child().setValue(model1);
                    }
                });

            }
            else
            {
                Toast.makeText(ref, "Please fill the fields", Toast.LENGTH_SHORT).show();
            }

        }
    }
}