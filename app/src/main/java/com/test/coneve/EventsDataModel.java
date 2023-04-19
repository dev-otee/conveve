package com.test.coneve;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class EventsDataModel implements Serializable {

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
    enum fields{
        startDate(1),
        endDate(2),
        description(4),
        name(8),
        pimageid(16),
        reglink(32),
        venue(64),
        startime(128),
        endtime(256);
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
    }
    public int compare(EventsDataModel comp)
    {
        int comp_word = 0;
        if(getStartDate() != comp.getStartDate())
            comp_word |= fields.startDate.getWord();
        if(getEndDate() != comp.getEndDate())
            comp_word |= fields.endDate.getWord();
        if(getDescription() != comp.getDescription())
            comp_word |= fields.description.getWord();
        if(getName() != comp.getName())
            comp_word |= fields.name.getWord();
        if(getPimageid() != comp.getPimageid())
            comp_word |= fields.pimageid.getWord();
        if(getReglink() != comp.getReglink())
            comp_word |= fields.reglink.getWord();
        if(getVenue() != comp.getVenue())
            comp_word |= fields.venue.getWord();
        if(getStarttime() != comp.getStarttime())
            comp_word |= fields.startime.getWord();
        if(getEndtime() != comp.getEndtime())
            comp_word |= fields.endtime.getWord();
        return comp_word;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(pimageid);
                eventPoster.setValue(HelperClass.FetchBitmap(pimageid));
            }
        }).start();
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

    public EventsDataModel() {
        //TODO: Set Default Value for eventPoster
        //eventPoster = new MutableLiveData<Bitmap>(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.loading));
    }
}