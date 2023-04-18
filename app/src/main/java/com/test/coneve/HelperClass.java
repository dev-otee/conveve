package com.test.coneve;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HelperClass {
    public static Bitmap FetchBitmap(Uri uri)
    {
        try{
            HttpURLConnection connection = (HttpURLConnection) (new URL(uri.toString())).openConnection();
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
        }
        return null;
    }
}
