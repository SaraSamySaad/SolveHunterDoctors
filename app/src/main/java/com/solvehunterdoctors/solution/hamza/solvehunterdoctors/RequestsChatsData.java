package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

public class RequestsChatsData  {
    private String problemHint;
    private String docId;
    private String userId;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public RequestsChatsData(String problemHint, String docId, String userId) {
        this.problemHint = problemHint;
        this.docId = docId;
        this.userId = userId;
    }

    public String getProblemHint() {
        return problemHint;
    }

    public void setProblemHint(String problemHint) {
        this.problemHint = problemHint;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocId() {
        return docId;
    }

    public String getUserId() {
        return userId;
    }

    public RequestsChatsData() {
    }
}
