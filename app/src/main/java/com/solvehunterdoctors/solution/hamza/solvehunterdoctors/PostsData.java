package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

/**
 * Created by C.M on 24/10/2018.
 */

public class PostsData  {
    private  String body;
    private  long likes;
    private long comments;
    private String uploadedById;
    private String id;
    public PostsData() {}

    public PostsData(String body, long likes, long comments, String uploadedById) {
        this.body = body;
        this.likes = likes;
        this.comments = comments;
        this.uploadedById = uploadedById;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public void setUploadedById(String uploadedById) {
        this.uploadedById = uploadedById;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public long getLikes() {
        return likes;
    }

    public long getComments() {
        return comments;
    }

    public String getUploadedById() {
        return uploadedById;
    }

    public String getId() {
        return id;
    }
}
