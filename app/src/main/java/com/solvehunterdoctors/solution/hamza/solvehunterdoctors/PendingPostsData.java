package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

/**
 * Created by C.M on 25/10/2018.
 */

public class PendingPostsData {

    private String uploadedById;
    private String body;
    private String postId;

    public String getUploadedById() {
        return uploadedById;
    }

    public PendingPostsData() {
    }

    public String getBody() {
        return body;
    }

    public PendingPostsData(String uploadedById, String body) {
        this.uploadedById = uploadedById;
        this.body = body;

    }

    public void setUploadedById(String uploadedById) {
        this.uploadedById = uploadedById;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
