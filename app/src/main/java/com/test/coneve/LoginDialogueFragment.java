package com.test.coneve;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginDialogueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginDialogueFragment extends DialogFragment {
    BeginSignInRequest signInRequest;
    SignInClient oneTapClient;

    FirebaseAuth mauth;
    final int REQ_ONE_TAP =5;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public LoginDialogueFragment() {
        mauth = FirebaseAuth.getInstance();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginDialogueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginDialogueFragment newInstance(String param1, String param2) {
        LoginDialogueFragment fragment = new LoginDialogueFragment();
        return fragment;
    }
    @Override
     public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        try {
            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
            String idTocken = credential.getGoogleIdToken();
            if(idTocken == null) {
                Log.d("Sign In", "IdTocken NULL");
                return;
            }
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idTocken,null);
            mauth.signInWithCredential(firebaseCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Intent serviceIntent = new Intent(getContext(),maintainerService.class);
                    if(task.isSuccessful())
                    {
                        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                        root.child("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    DataSnapshot current_user_branch = task.getResult().child("Users").child(mauth.getCurrentUser().getUid());
                                    if(!current_user_branch.exists())
                                        root.child("Users").child(mauth.getCurrentUser().getUid()).setValue("");
                                }
                                dismiss();
                            }
                        });
                        getContext().bindService(serviceIntent, new ServiceConnection() {
                            @Override
                            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                                ((servicedataInterface)iBinder).setCurrentProfile().setValue(mauth.getCurrentUser());
                                dismiss();
                            }
                            @Override
                            public void onServiceDisconnected(ComponentName componentName) {

                            }
                        }, 0);
                    }
                    else {
                        Log.d("Sign In","Failed",task.getException());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Sign In","Login Not completed",e);
                    dismiss();
                }
            });
        }catch (Exception exception)
        {
            Log.d("Sign In","exception",exception);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_dialogue, container, false);
        view.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient = Identity.getSignInClient(getContext());
               signInRequest= BeginSignInRequest.builder()
                        .setGoogleIdTokenRequestOptions(
                                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                        .setFilterByAuthorizedAccounts(false)
                                        .setSupported(true)
                                        .setServerClientId(getString(R.string.googleAuth2Clientid))
                                        .build()
                        )
                        .build();
               oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(getActivity(), new OnSuccessListener<BeginSignInResult>() {
                   @Override
                   public void onSuccess(BeginSignInResult beginSignInResult) {
                       try{

                           startIntentSenderForResult(beginSignInResult.getPendingIntent().getIntentSender(),REQ_ONE_TAP,null,0,0,0,null);
                       }catch (Exception exception)
                       {
                           Log.d("Sign In","Success",exception);
                       }
                   }
               }).addOnFailureListener(getActivity(), new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.d("Sign In","Failed",e);
                   }
               });

            }
        });
        return view;
    }

}