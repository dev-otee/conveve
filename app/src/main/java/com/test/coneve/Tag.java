package com.test.coneve;

import static java.lang.Math.pow;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.MutableLiveData;

public class Tag {
    private String uri;
    private int index;
    private int word;
    private String name;
    private MutableLiveData<Bitmap> icon;
    public Tag()
    {
        name = "dafault";
        icon = new MutableLiveData<Bitmap>();
        icon.setValue(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.chef));
    }

    public Tag(String name,int position,int word)
    {
        this.name = name;
        this.index = index;
        word = (int)pow(2,position);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setWord(int word) {
        this.word = (int) Math.pow(2,word);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public int getWord() {
        return word;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        HelperClass.FetchBitmapfromfirebase(uri, new Callback<Bitmap>() {
            @Override
            public void callback(Bitmap object) {
                icon.setValue(object);
            }
        });
    }

    public MutableLiveData<Bitmap> getIcon() {
        return icon;
    }

    public void setIcon(MutableLiveData<Bitmap> icon) {
        this.icon = icon;
    }
}
