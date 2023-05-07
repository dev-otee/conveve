package com.test.coneve;

import android.app.Activity;
import android.content.Context;
import android.os.Binder;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class servicedataInterface extends Binder {
    private maintainerService currentInstance;
    public servicedataInterface(maintainerService setInstance)
    {
        currentInstance = setInstance;
    }
    public MutableLiveData<HashMap<String,Tag>> getTagList(){return currentInstance.tagList;}
    public MutableLiveData<FirebaseUser> getCurrentProfile(FragmentActivity context,Callback<FirebaseUser> user)
    {
        if(currentInstance.profile.getValue() == null)
        {
            LoginDialogueFragment loginFragment = new LoginDialogueFragment(user);
            loginFragment.show(context.getSupportFragmentManager(),"Login");
        }
        return currentInstance.profile;
    }
    public MutableLiveData<ProfileData> getProfileData()
    {
        return currentInstance.getProfileData();
    }
    public void setProfileData(ProfileData pdata)
    {
        currentInstance.setProfileData(pdata);
    }
    public MutableLiveData<FirebaseUser> setCurrentProfile()
    {
        return currentInstance.profile;
    }
    public MutableLiveData<HashMap<String,EventsDataModel>> getEvetsObserver(){return currentInstance.events;}

}
