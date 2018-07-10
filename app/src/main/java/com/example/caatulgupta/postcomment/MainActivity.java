package com.example.caatulgupta.postcomment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    PostCommentAdapter adapter;
    ArrayList<PostComment> postComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        adapter = new PostCommentAdapter(this,postComments);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,Details.class);
                Bundle bundle = new Bundle();
                PostComment postComment = postComments.get(i);
                bundle.putString("title",postComment.getTitle());
                Log.i("TITLE",postComment.getTitle());
                bundle.putString("body",postComment.getBody());
                bundle.putInt("userId",postComment.getUser_id());
                bundle.putInt("id",postComment.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        int id = intent.getIntExtra("userId",-1);
        if(id!=-1){
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

            PostCommentAsyncTask task = new PostCommentAsyncTask(new PostCommentDownloadListener() {
                @Override
                public void onDownload(ArrayList<PostComment> postComment) {
                    postComments.clear();
                    postComments.addAll(postComment);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            });
            task.execute("https://jsonplaceholder.typicode.com/users/"+id+"/posts");
        }

    }

    public void fetchData(View view) {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

        PostCommentAsyncTask task = new PostCommentAsyncTask(new PostCommentDownloadListener() {
            @Override
            public void onDownload(ArrayList<PostComment> postComment) {
                postComments.clear();
                postComments.addAll(postComment);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });
        task.execute("https://jsonplaceholder.typicode.com/posts");

    }

}
