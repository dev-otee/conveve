package com.test.coneve;

public class TagWord {
    private int arr_max;
    private int [] arr;

    public int getArr_max() {
        return arr_max;
    }

    public void setArr_max(int arr_max) {
        this.arr_max = arr_max;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    public TagWord()
    {
        arr = new int[1];
        arr_max = 0;
    }
    public void addTag(Tag tag)
    {
        if(tag.getIndex()>arr_max)
        {
            int [] arr = new int[tag.getWord()];
            for(int i=0;i<arr_max;i++)
                arr[i] = this.arr[i];
            this.arr_max = tag.getIndex();
        }
        arr[tag.getIndex()] |= tag.getWord();
    }
    public boolean hasTag(Tag tag)
    {
        if(tag.getIndex()<arr_max)
            return false;
        if((arr[tag.getIndex()]&tag.getWord())<1)
            return false;
        return true;
    }
}
