package com.visa.say2pay;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MultipartRequest extends AsyncTask {
    public Context context;
    Response response = null;
    public MultipartBody.Builder multipartBody;
    public OkHttpClient okHttpClient;

    public MultipartRequest(Context context) {
        this.context = context;
        this.multipartBody = new MultipartBody.Builder();
        this.multipartBody.setType(MultipartBody.FORM);
        this.okHttpClient = new OkHttpClient();
    }

    // Add String
    public void addString(String name, String value) {
        this.multipartBody.addFormDataPart(name, value);
    }

    // Add Image File
    public void addFile(String name, Bitmap bitmap, String fileName) {
        Log.d("Multipart", "Post File");
        this.multipartBody.addFormDataPart(name, fileName, RequestBody.create(MediaType.parse("image/jpeg"), persistImage(bitmap, "image")));
    }

    // Add Zip File
    public void addZipFile(String name, String filePath, String fileName) {
        this.multipartBody.addFormDataPart(name, fileName, RequestBody.create(MediaType.parse("application/zip"), new File(filePath)));
    }

    // Execute Url
    public void execute(String url, String name, String filename, File sourceFile ) {
        RequestBody requestBody = null;
        Request request = null;

        try {
            requestBody = this.multipartBody
                    .setType(MultipartBody.FORM)
                    .build();
            // Set Your Authentication key here.
            request = new Request.Builder().url(url).header("Content-Type", "multipart/form-data").post(requestBody).build();


            Log.v("Multipart", "====== REQUEST ======\n" + request);
            OkHttpHandler okHttpHandler = new OkHttpHandler();
            response = okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = this.context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        int code = 200;
        String strResponse = "Null";

        Log.d("Multipart", "Waiting...");
        Log.v("Multipart", "====== RESPONSE ======\n" + response);
        try {
            if (!response.isSuccessful())
                throw new IOException();

            code = response.networkResponse().code();

            /*
             * "Successful response from server"
             */
            if (response.isSuccessful()) {
                strResponse = response.body().string();
            }
            /*
             * "Invalid URL or Server not available, please try again."
             */
            else if (code == HttpStatus.SC_NOT_FOUND) {
                strResponse = "Invalid URL or Server not available, please try again";
            }
            /*
             * "Connection timeout, please try again."
             */
            else if (code == HttpStatus.SC_REQUEST_TIMEOUT) {
                strResponse = "Connection timeout, please try again";
            }
            /*
             * "Invalid URL or Server is not responding, please try again."
             */
            else if (code == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                strResponse = "Invalid URL or Server is not responding, please try again";
            }
        } catch (
                Exception e)

        {
            Log.e("Exception", e.getMessage());
        } finally {
//        requestBody = null;
//        request = null;
            response = null;
            multipartBody = null;
            if (okHttpClient != null)
                okHttpClient = null;

            System.gc();
        }
        Log.d("Multipart" ,strResponse);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}