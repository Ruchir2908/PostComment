package com.example.caatulgupta.postcomment;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class CommentsAsyncTask extends AsyncTask<String,Void,ArrayList<String>>{

    ArrayList<String> comments = new ArrayList<>();
    CommentsListener listener;

    public CommentsAsyncTask(CommentsListener listener){
        this.listener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        String urlString = strings[0];

        try {
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String result = "";
            while (scanner.hasNext()){
                result = result + scanner.next();
            }

            JSONArray root =new JSONArray(result);
            for(int i=0;i<root.length();i++){
                JSONObject data = root.getJSONObject(i);
                String comment = data.getString("body");
                comments.add(comment);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return comments;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        listener.onDownload(strings);
    }
}
