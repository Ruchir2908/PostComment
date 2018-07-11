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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    PostCommentAdapter adapter;
    FloatingActionButton fab;
    ArrayList<PostComment> postComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        fab = findViewById(R.id.fab);

        adapter = new PostCommentAdapter(this,postComments);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,Details.class);
                Bundle bundle = new Bundle();
                PostComment postComment = postComments.get(i);
                bundle.putString("title",postComment.getTitle());
                Log.i("TITLE",postComment.getUser_id()+"");
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
//        fab.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

//        PostCommentAsyncTask task = new PostCommentAsyncTask(new PostCommentDownloadListener() {
//            @Override
//            public void onDownload(ArrayList<PostComment> postComment) {
//                postComments.clear();
//                postComments.addAll(postComment);
//                adapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                listView.setVisibility(View.VISIBLE);
//            }
//        });
//        task.execute("https://jsonplaceholder.typicode.com/posts");






        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        PostCommentService service = retrofit.create(PostCommentService.class);
        Call<ArrayList<PostComment>> call = service.getPost();
        call.enqueue(new Callback<ArrayList<PostComment>>() {
            @Override
            public void onResponse(Call<ArrayList<PostComment>> call, Response<ArrayList<PostComment>> response) {
                postComments.addAll(response.body());
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ArrayList<PostComment>> call, Throwable t) {

            }
        });

    }

}
