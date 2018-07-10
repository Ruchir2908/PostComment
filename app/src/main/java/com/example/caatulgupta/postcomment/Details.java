package com.example.caatulgupta.postcomment;

import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Details extends AppCompatActivity {

    TextView titleTextView, bodyTextView, usernameTextView, nameTextView, emailTextView;
    ProgressBar commentsProgressBar;
    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;
    Bundle bundle;
    ArrayList<String> comments = new ArrayList<>();
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        bundle = intent.getExtras();

        titleTextView = findViewById(R.id.postTextView);
        bodyTextView = findViewById(R.id.bodyTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        listView = findViewById(R.id.commentsListView);
        button = findViewById(R.id.button);
        commentsProgressBar = findViewById(R.id.detailsProgressBar);

        adapter =  new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,comments);
        listView.setAdapter(adapter);

//        titleTextView.setText(bundle.getString("title"));
        Log.i("TITLE1111",bundle.getString("title"));
        titleTextView.setText(bundle.getString("title"));
        bodyTextView.setText(bundle.getString("body"));

        UserAsyncTask task = new UserAsyncTask(new UserDetailsListener(){
            @Override
            public void onDownload(UserDetails userDetail) {
//                userDetails.clear();
//                    userDetails = new UserDetails(userDetail.getName(),userDetail.getUsername(),userDetail.getEmail());

                nameTextView.setText("NAME: "+userDetail.getName());
                usernameTextView.setText("USERNAME: "+userDetail.getUsername());
                emailTextView.setText("EMAIL: "+userDetail.getEmail());
            }
        });

        task.execute("https://jsonplaceholder.typicode.com/users/"+bundle.getInt("userId"));


        fetchComments();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Details.this,MainActivity.class);
                intent1.putExtra("userId",bundle.getInt("userId"));
                startActivity(intent1);
            }
        });

    }

    private void fetchComments() {

        commentsProgressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

        CommentsAsyncTask task = new CommentsAsyncTask(new CommentsListener() {
            @Override
            public void onDownload(ArrayList<String> comment) {
                comments.clear();
                comments.addAll(comment);
                adapter.notifyDataSetChanged();
                commentsProgressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });
        task.execute("https://jsonplaceholder.typicode.com/posts/"+bundle.getInt("id")+"/comments");

    }
}
