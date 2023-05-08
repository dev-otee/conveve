package com.test.coneve;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class HelperClass {
    public static String UserNamefromUser(FirebaseUser user)
    {
        if(user==null)
            return null;
        String name;
        name = user.getDisplayName();
        if(name == null||name.length()==0)
        {
            List<UserInfo> ulist= (List<UserInfo>)user.getProviderData();
            for (UserInfo info:
                    ulist
                 ) {
                if(info.getDisplayName()!=null&&info.getDisplayName().length()>0)
                {
                    name = info.getDisplayName();
                    break;
                }
            }
        }
        return name;
    }
    public static void FetchBitmap(String uri, Callback<Bitmap> callback)
    {
        try{

//            HttpURLConnection connection = (HttpURLConnection) (new URL(uri)).openConnection();
//            connection.connect();
//            InputStream data = connection.getInputStream();
//            int length = data.available();
//            if(length == 0)
//                throw new Exception("Image File Empty");
//            byte[] databytes = new byte[length];
//            data.read(databytes);
////            return BitmapFactory.decodeByteArray(databytes,0,length);
//            callback.callback(BitmapFactory.decodeByteArray(databytes,0,length));
//
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        HttpURLConnection connection = (HttpURLConnection) (new URL(uri)).openConnection();
                        connection.connect();
                        if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK)
                        {
                            return;
                        }
//            return BitmapFactory.decodeByteArray(databytes,0,length);
                        callback.callback(BitmapFactory.decodeStream(connection.getInputStream()));
                    }catch (MalformedURLException exception)
                    {
                        Log.d("MyLog","malformed url",exception);
                        //TODO : Implement Exception Dialogue Fragment;
                    }
                    catch (IOException exception)
                    {
                        Log.d("MyLog","io-exception",exception);
                        //TODO : Implement Exception Dialogue Fragment;
                    }catch (Exception exception)
                    {
                        Log.d("Helper","exception",exception);
                    }

                }
            }).start();
        }
        catch (Exception exception)
        {
            //TODO : Implement Exception Dialogue Fragment;
            Log.d("MyLog","exception",exception);
        }
//        return null;
    }
    public static void FetchBitmap(Uri uri, Callback<Bitmap> callback)
    {

        FetchBitmap(uri.toString(), callback);
    }
    static HashMap<String, MutableLiveData<Bitmap>> bitmapCache = new HashMap<String,MutableLiveData<Bitmap>>();
    static int count =0;
    public static void FetchBitmapfromfirebase(String uri,Callback<Bitmap> end) {
        MutableLiveData<Bitmap> imgval = bitmapCache.get(uri.toString());
        if(imgval == null)
        {
           imgval = new MutableLiveData<Bitmap>();
           final MutableLiveData<Bitmap> mgval = imgval;
            bitmapCache.put(uri.toString(),imgval);
            final int ONE_MB = 1024*1024;
            Bitmap result = null;
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri);

            imageRef.getBytes(5*ONE_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                MutableLiveData<Bitmap> cval;
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap cur_image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    end.callback(cur_image);
                    cval = mgval;
                    cval.setValue(cur_image);
                    count++;
                    Log.d("ImageLoaded",((Integer)count).toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //TODO:Deal with failiure
                    Log.d("helper","FetchBitmapfromfirebase",e);
                }
            });
        }

        imgval.observeForever(new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                end.callback(bitmap);
            }
        });

    }
}
