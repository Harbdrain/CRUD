package com.danil.crud.model;

public class Label {
    private final int id;
    private String name;
    private LabelStatus status;

    public Label(int id, String name) {
        this.name = name;
        this.id = id;
        this.status = LabelStatus.ACTIVE;
    }

    public int getId() {
        return id;
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

    public void delete() {
        this.status = LabelStatus.DELETED;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + ", " + "name: " + this.name + ", " + "status: " + this.status;
    }

    public boolean isDeleted() {
        return this.status == LabelStatus.DELETED;
    }

}
