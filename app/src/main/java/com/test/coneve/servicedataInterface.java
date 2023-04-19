package com.test.coneve;

import android.os.Binder;

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
    public MutableLiveData<FirebaseUser> getCurrentProfile()
    {
        return currentInstance.profile;
    }
    public MutableLiveData<HashMap<String,EventsDataModel>> getEvetsObserver(){return currentInstance.events;}

}
