package com.test.coneve;

import java.util.ArrayList;
import java.util.List;

public class TagWord {
    private int arr_max;

    private ArrayList<Integer> arr;

    public int getArr_max() {
        return arr_max;
    }

    public void setArr_max(int arr_max) {
        this.arr_max = arr_max;
    }

    public ArrayList<Integer> getArr() {
        return arr;
    }

    public void setArr(ArrayList<Integer> arr) {
        this.arr = arr;
    }

    public TagWord()
    {
        arr = new ArrayList<Integer>();
        arr.add(0);
        arr_max = 0;
    }

    public void setTagWord(TagWord incoming)
    {
        arr = incoming.getArr();
        arr_max = incoming.getArr_max();
    }
    public void removeTag(Tag tag)
    {
        int mask = tag.getWord();
        mask = ~mask;
        getArr().set(tag.getIndex(),getArr().get(tag.getIndex())&mask);
        if(tag.getIndex() == getArr_max())
        {
            int last_holder = 0;
            for (int i = 0; i < arr_max; i++) {
                if(getArr().get(i)>0)
                    last_holder = i;
            }
            setArr_max(last_holder);
        }
    }
    public void addTag(Tag tag)
    {
        if(tag.getIndex()>arr_max)
        {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            arr.addAll(this.getArr());
            for(int i=getArr_max()+1;i<=tag.getIndex();i++)
                arr.add(i,0);
            this.arr_max = tag.getIndex();
            this.arr = arr;
        }
        arr.set(tag.getIndex(),arr.get(tag.getIndex())|tag.getWord());
    }
    public boolean equal(TagWord word)
    {
        if(getArr_max() != word.getArr_max())
            return false;

        for(int i=0;i<=arr_max;i++)
            if(arr.get(i)!=word.arr.get(i))
                return false;
        return true;
    }
    public boolean hasTag(Tag tag)
    {
        if(tag.getIndex()<arr_max)
            return false;
        if((arr.get(tag.getIndex())&tag.getWord())<1)
            return false;
        return true;
    }
    public boolean hasTag(TagWord tagWord)
    {
        if(getArr_max()<tagWord.getArr_max())
        return false;
        for (int i = 0; i <=arr_max; i++) {
            if((tagWord.arr.get(i)&arr.get(i))!=tagWord.arr.get(i))
                return false;
        }
        return true;
        
    }
}
