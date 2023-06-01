package com.danil.crud.view;

import java.util.List;

import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;

public class WriterView {
    public void statusOK() {
        System.out.println("OK");
    }

    public void showList(List<Writer> list) {
        for (Writer writer : list) {
            System.out.print("ID: " + writer.getId() + ", " +
                    "firstName: " + writer.getFirstName() + ", " +
                    "lastName: " + writer.getLastName() + ", " +
                    "status: " + writer.getStatus() + ", " +
                    "post IDs: [");
            for (Post post : writer.getPosts()) {
                System.out.print(post.getId() + ", ");
            }
            System.out.println("]");
        }
    }

    public void show(Writer writer) {
        System.out.println(writer);
    }
}
