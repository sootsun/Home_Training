package com.example.home_training.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.home_training.R;

import java.util.List;

public class PostAdapter extends ArrayAdapter<DashboardPost> {
    private Context context;
    private List<DashboardPost> posts;

    public PostAdapter(@NonNull Context context, List<DashboardPost> posts) {
        super(context, 0, posts);
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_post, parent, false);
        }

        DashboardPost currentPost = posts.get(position);

        TextView titleTextView = listItemView.findViewById(R.id.titleTextView);
        TextView contentTextView = listItemView.findViewById(R.id.contentTextView);

        titleTextView.setText(currentPost.getTitle());
        contentTextView.setText(currentPost.getContent());

        return listItemView;
    }
}
