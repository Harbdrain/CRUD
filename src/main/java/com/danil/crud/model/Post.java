package com.danil.crud.model;

import java.util.List;

public class Post {
    private Integer id;
    private String content;
    private Long created;
    private Long updated;
    private List<Label> labels;
    private PostStatus status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
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
        this.labels.removeIf(e -> e.getId() == labelId);
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
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
