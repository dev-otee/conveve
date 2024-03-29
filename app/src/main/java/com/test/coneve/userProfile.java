package com.test.coneve;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userProfile {
    FirebaseUser user;
    public static userProfile currentInstance;
    public static userProfile getUserInstance()
    {
        if(currentInstance == null)
            currentInstance = new userProfile();
        return currentInstance;
    }
    Bitmap profilepic;
    private userProfile()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {

            profilepic = null;
            return;
        }
        profilepic = HelperClass.FetchBitmap(user.getPhotoUrl());
    }

    boolean isSignedIn()
    {
        if(user==null)
            return false;
        return true;
    }
}
