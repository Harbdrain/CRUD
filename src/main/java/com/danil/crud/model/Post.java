package com.danil.crud.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Post {
    private final int id;
    private String content;
    private long created;
    private long updated;
    private List<Label> labels;
    private PostStatus status;

    public Post(int id, String content) {
        this.id = id;
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
        this.status = PostStatus.UNDER_REVIEW;
    }

    public long getTimeCreated() {
        return created;
    }

    public long getTimeUpdated() {
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

    public void removeLabel(int labelId) {
        Iterator<Label> iterator = labels.iterator();
        while (iterator.hasNext()) {
            Label label = iterator.next();
            if (label.getId() == labelId) {
                iterator.remove();
                break;
            }
        }
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public void delete() {
        this.status = PostStatus.DELETED;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: " + this.id + ", ");
        builder.append("content: " + this.content + ", ");
        builder.append("created: " + this.created + ", ");
        builder.append("updated: " + this.updated + ", ");
        builder.append("status: " + this.status + ", ");
        builder.append("labels: [ ");
        for (Label label : labels) {
            builder.append("{" + label + "}, ");
        }
        builder.append("]");
        return builder.toString();
    }

    public boolean isDeleted() {
        return this.status == PostStatus.DELETED;
    }
}
