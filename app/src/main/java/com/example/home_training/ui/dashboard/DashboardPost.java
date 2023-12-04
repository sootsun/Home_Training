package com.example.home_training.ui.dashboard;

import java.util.ArrayList;
import java.util.List;

public class DashboardPost {
    private String title;
    private String content;
    private String id;
    private List<String> comments;

    public void Post(String title, String content, String id){
        this.title = title;
        this.content = content;
        this.id = id;
        this.comments = new ArrayList<>();
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getId(){
        return id;
    }

    public List<String> getComments(){
        return comments;
    }

    public void addComment(String comment){
        comments.add(comment);
    }
}
