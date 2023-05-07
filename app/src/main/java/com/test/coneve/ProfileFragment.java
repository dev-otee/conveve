package com.test.coneve;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseUser user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile");
        Intent intent = new Intent(getContext(),maintainerService.class);
        getContext().bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                ((servicedataInterface)iBinder).getCurrentProfile(getActivity(), null).observe(getActivity(), new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged(FirebaseUser firebaseUser) {
                        if(firebaseUser == null)
                            return;
                        user = firebaseUser;
                        String displayName = user.getDisplayName();
                        List<UserInfo> userData = (List<UserInfo>) user.getProviderData();
                        if(displayName==null||displayName=="")
                            for (UserInfo info:
                                    userData) {
                                if(info.getDisplayName()!=null)
                                    displayName = info.getDisplayName();
                            }
                        ((TextView)getActivity().findViewById(R.id.username)).setText(displayName);
                        ((TextView)getActivity().findViewById(R.id.email)).setText(user.getEmail());

                        //adding profile picture
                        try {
                            HelperClass.FetchBitmap(user.getPhotoUrl(), new Callback<Bitmap>() {
                                @Override
                                public void callback(Bitmap object) {
                                    ((ImageView)getActivity().findViewById(R.id.profileImage)).setImageBitmap(object);
                                }
                            });
//                            HttpsURLConnection getProfilePhoto = (HttpsURLConnection) (new URL(user.getPhotoUrl().toString())).openConnection();
//                            getProfilePhoto.connect();
//                            if(getProfilePhoto.getResponseCode() != HttpURLConnection.HTTP_OK){
//                                return;
//                            }
//
//                            InputStream iStream = getProfilePhoto.getInputStream();
//                            Bitmap bmf = BitmapFactory.decodeStream(iStream);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, 0);
        return view;
    }
}