package com.visa.say2pay;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHandler extends AsyncTask {

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("OkHttpHandler", (String) o);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Request.Builder builder = new Request.Builder();
        String URL = (String) params[0];
        Log.d("OkHttpHandler", URL);
        builder.url(URL);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}