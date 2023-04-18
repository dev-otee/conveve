package com.test.coneve;

import android.os.Binder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

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

}
