package com.example.caatulgupta.postcomment;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class PostCommentAsyncTask extends AsyncTask<String,Void,ArrayList<PostComment>> {

    PostCommentDownloadListener listener;

    public PostCommentAsyncTask(PostCommentDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<PostComment> doInBackground(String... strings) {
        ArrayList<PostComment> postComments = new ArrayList<>();
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



//            JSONArray rootArray = new JSONArray(result);
//            for(int i=0;i<rootArray.length();i++){
//                JSONObject details = rootArray.getJSONObject(i);
//                String title = details.getString("title");
//                String body = details.getString("body");
//                int id = details.getInt("id");
//                int user_id = details.getInt("userId");
//                PostComment postComment = new PostComment(id,user_id,title,body);
//                postComments.add(postComment);
//            }

            Gson gson = new Gson();
            ArrayList<PostComment> postCommentArrayList = gson.fromJson(result,new TypeToken<ArrayList<PostComment>>(){}.getType());
//            for(int i=0;i<postCommentArrayList.size();i++){
//                postComments.add(postCommentArrayList.get(i));
//            }

            postComments.addAll(postCommentArrayList);
            Log.i("PCAT","ID: "+postComments.get(0).getTitle());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
// catch (JSONException e) {
//            e.printStackTrace();
//        }

        return postComments;
    }

    @Override
    protected void onPostExecute(ArrayList<PostComment> postComments) {
        super.onPostExecute(postComments);
        listener.onDownload(postComments);
    }
}
