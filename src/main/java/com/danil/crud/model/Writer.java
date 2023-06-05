package com.danil.crud.model;

import java.util.List;

public class Writer {
    private Integer id;
    private String firstName;
    private String lastName;
    List<Post> posts;
    WriterStatus status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public WriterStatus getStatus() {
        return status;
    }

    public void setStatus(WriterStatus status) {
        this.status = status;
    }

    public void delete() {
        this.status = WriterStatus.DELETED;
    }

    public boolean isDeleted() {
        return this.status == WriterStatus.DELETED;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: " + this.id + ", ");
        builder.append("firstName: " + this.firstName + ", ");
        builder.append("lastName: " + this.lastName + ", ");
        builder.append("status: " + this.status + ", ");
        builder.append("posts: [ ");
        for (Post post : posts) {
            builder.append("{" + post + "}, ");
        }
        builder.append("]");
        return builder.toString();
    }
}
