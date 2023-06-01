package com.danil.crud.view;

import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;

public class PostView {
    public void statusOK() {
        System.out.println("OK");
    }

    public void showList(List<Post> list) {
        for (Post post : list) {
            System.out.print("ID: " + post.getId() + ", " +
                    "content: " + post.getContent() + ", " +
                    "created: " + post.getCreated() + ", " +
                    "updated: " + post.getUpdated() + ", " +
                    "status: " + post.getStatus() + ", " +
                    "label IDs: [");
            for (Label label : post.getLabels()) {
                System.out.print(label.getId() + ", ");
            }
            System.out.println("]");
        }
    }

    public void show(Post post) {
        System.out.println(post);
    }
}
