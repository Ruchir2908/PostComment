package com.example.caatulgupta.postcomment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PostCommentAdapter extends ArrayAdapter {

    ArrayList<PostComment> postComments;
    LayoutInflater inflater;
    Context context;


    public PostCommentAdapter(@NonNull Context context, ArrayList<PostComment> postComment) {
        super(context, 0);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.postComments = postComment;
        this.context = context;
    }

    @Override
    public int getCount() {
        return postComments.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output = inflater.inflate(R.layout.post_comment_layout,null);
        TextView title = output.findViewById(R.id.titleTextView);
        TextView body = output.findViewById(R.id.bodyTextView);
        PostComment postComment = postComments.get(position);
        title.setText(postComment.getTitle());
        body.setText(postComment.getBody());
        return output;
    }
}
