package com.danil.crud.model;

public class Label {
    private Integer id;
    private String name;
    private LabelStatus status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LabelStatus getStatus() {
        return this.status;
    }

    public void setStatus(LabelStatus status) {
        this.status = status;
    }

    public void delete() {
        this.status = LabelStatus.DELETED;
    }

    public boolean isDeleted() {
        return this.status == LabelStatus.DELETED;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + ", " + "name: " + this.name + ", " + "status: " + this.status;
    }
}
