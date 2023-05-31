package com.danil.crud.model;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private static int maxID = 0;
    private final int id;
    private String content;
    private long created;
    private long updated;
    private List<Label> labels;
    private PostStatus status;

    public Post(String content) {
        this.id = maxID++;
        this.content = content;
        this.created = System.currentTimeMillis() / 1000L;
        this.updated = this.created;
        this.labels = new ArrayList<>();
        this.status = PostStatus.ACTIVE;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updated = System.currentTimeMillis() / 1000L;
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public void addLabel(Label label) {
        this.labels.add(label);
    }

    // TODO: removeLabel() ?
}
