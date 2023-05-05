package com.test.coneve;


import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;

public class EventsDataModel implements Serializable {

    String id;
    String startDate;
    String endDate;
    MutableLiveData<Bitmap> eventPoster;
    String description;
    String name;
    String pimageid;
    String reglink;
    String Venue;
    String starttime;
    String endtime;
    TagWord tags;
    enum fields{
        startDate(1),
        endDate(2),
        description(4),
        name(8),
        pimageid(16),
        reglink(32),
        venue(64),
        startime(128),
        endtime(256),
        tags(512);
        fields(int word)
        {
            this.word = word;
        }
        private int word;
        public int getWord()
        {
            return this.word;
        }
    }

    public void update(int comp_word, EventsDataModel update_data)
    {
        if((comp_word & fields.startDate.getWord())>0)
            setStartDate(update_data.getStartDate());
        if((comp_word & fields.endDate.getWord())>0)
            setEndDate(update_data.getEndDate());
        if((comp_word & fields.description.getWord())>0)
            setDescription(update_data.getDescription());
        if((comp_word & fields.name.getWord())>0)
            setName(update_data.getName());
        if((comp_word & fields.pimageid.getWord())>0)
            setPimageid(update_data.getPimageid());
        if((comp_word & fields.reglink.getWord())>0)
            setReglink(update_data.getReglink());
        if((comp_word & fields.venue.getWord())>0)
            setVenue(update_data.getVenue());
        if((comp_word & fields.startime.getWord())>0)
            setStarttime(update_data.getStarttime());
        if((comp_word & fields.endtime.getWord())>0)
            setEndtime(update_data.getEndtime());
        if((comp_word & fields.tags.getWord())>0)
            setTags(update_data.getTags());
    }
    public int compare(EventsDataModel comp)
    {
        int comp_word = 0;
        if(getStartDate()==null||!getStartDate().equals(comp.getStartDate()))
            comp_word |= fields.startDate.getWord();
        if(getEndDate()==null||!getEndDate().equals(comp.getEndDate()))
            comp_word |= fields.endDate.getWord();
        if(getDescription()==null||!getDescription().equals(comp.getDescription()))
            comp_word |= fields.description.getWord();
        if(getName()==null||!getName().equals(comp.getName()))
            comp_word |= fields.name.getWord();
        if(getPimageid()==null||!getPimageid().equals(comp.getPimageid()))
            comp_word |= fields.pimageid.getWord();
        if(getReglink()==null||!getReglink().equals(comp.getReglink()))
            comp_word |= fields.reglink.getWord();
        if(getVenue()==null||!getVenue().equals(comp.getVenue()))
            comp_word |= fields.venue.getWord();
        if(getStarttime()==null||!getStarttime().equals(comp.getStarttime()))
            comp_word |= fields.startime.getWord();
        if(getEndtime()==null||!getEndtime().equals(comp.getEndtime()))
            comp_word |= fields.endtime.getWord();
        if(getTags()==null||!tags.equal(comp.tags))
            comp_word |=fields.tags.getWord();
        return comp_word;
    }
    private static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    static class ComparatorList{
        static class TimeCmp implements Comparator<EventsDataModel>{
            @Override
            public int compare(EventsDataModel o1, EventsDataModel o2) {
                try {
                    Date startEvent1 = DATE_TIME_FORMATTER.parse(o1.getStartDate()+" "+o1.getStarttime());
                    Date startEvent2 = DATE_TIME_FORMATTER.parse(o2.getStartDate()+" "+o2.getStarttime());

                    // first compare based on date
//                    int dateCompareResult = startDate1.compareTo(startDate2);
//
//                    if(dateCompareResult != 0 ) return dateCompareResult;
//                    else return startTime1.compareTo(startTime2);
                    return startEvent2.compareTo(startEvent1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }
    }
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPimageid() {
        return pimageid;
    }

    public String getReglink() {
        return reglink;
    }

    public String getVenue() {
        return Venue;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPimageid(String pimageid) {
        this.pimageid = pimageid;
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(pimageid);
        HelperClass.FetchBitmapfromfirebase(pimageid, new Callback<Bitmap>() {
            @Override
            public void callback(Bitmap object) {
                eventPoster.setValue(object);
            }
        });
    }

    public void setReglink(String reglink) {
        this.reglink = reglink;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public TagWord getTags() {
        return tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTags(TagWord tags) {
        this.tags = tags;
    }
    public EventsDataModel() {
        //TODO: Set Default Value for eventPoster
        eventPoster = new MutableLiveData<Bitmap>();
        //eventPoster = new MutableLiveData<Bitmap>(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.loading));
    }
}