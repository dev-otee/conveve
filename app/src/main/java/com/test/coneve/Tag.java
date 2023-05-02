package com.test.coneve;

import static java.lang.Math.pow;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

public class Tag {
    private int index;
    private int word;
    private String name;
    private MutableLiveData<Bitmap> icon;
    public Tag()
    {
        name = "dafault";
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
        this.word = word;
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
}
