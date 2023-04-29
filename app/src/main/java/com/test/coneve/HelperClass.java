package com.test.coneve;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HelperClass {
    public static Bitmap FetchBitmap(String uri)
    {
        try{

            HttpURLConnection connection = (HttpURLConnection) (new URL(uri)).openConnection();
            connection.connect();
            InputStream data = connection.getInputStream();
            int length = data.available();
            if(length == 0)
                throw new Exception("Image File Empty");
            byte[] databytes = new byte[length];
            data.read(databytes);
            return BitmapFactory.decodeByteArray(databytes,0,length);
        }catch (MalformedURLException exception)
        {
            //TODO : Implement Exception Dialogue Fragment;
        }
        catch (IOException exception)
        {
            //TODO : Implement Exception Dialogue Fragment;
        }
        catch (Exception exception)
        {
            //TODO : Implement Exception Dialogue Fragment;
            Log.d("helperclass","exception",exception);
        }
        return null;
    }
    public static Bitmap FetchBitmap(Uri uri)
    {

        return FetchBitmap(uri.toString());
    }

    public static void FetchBitmapfromfirebase(String uri,Callback<Bitmap> end) {
        final int ONE_MB = 1024*1024;
        Bitmap result = null;
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri);

        imageRef.getBytes(5*ONE_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                end.callback(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO:Deal with failiure
                Log.d("helper","FetchBitmapfromfirebase",e);
            }
        });
    }
}
