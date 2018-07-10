package com.example.caatulgupta.postcomment;

import android.os.AsyncTask;
import android.util.Log;

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

public class UserAsyncTask extends AsyncTask<String,Void,UserDetails> {

//    ArrayList<UserDetails> userDetails = new ArrayList<>();
    UserDetails userDetails;
    UserDetailsListener listener;

    public UserAsyncTask(UserDetailsListener listener) {
        this.listener = listener;
    }

    @Override
    protected UserDetails doInBackground(String... strings) {

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

            JSONObject root =new JSONObject(result);


//            Log.i("Name",root.getString("email"));

                userDetails = new UserDetails(root.getString("name"),root.getString("username"),root.getString("email"));
//                userDetails.add(userDetail);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userDetails;
    }

    @Override
    protected void onPostExecute(UserDetails userDetails) {
        Log.i("CheckIn","Name " + userDetails.getName());
        Log.i("CheckIn","UserName " + userDetails.getUsername());
        Log.i("CheckIn","Email " + userDetails.getEmail());
        listener.onDownload(userDetails);
    }
}
