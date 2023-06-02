package com.danil.crud.view;

import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.utils.ControllerUtils;

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

    public void processInput(String input) {
        String[] data = input.split(" ", 2);
        String command = data[0];

        try {
            if (command.equals("create")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                data = data[1].split(" ", 2);
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int writerId = Integer.parseInt(data[0]);
                String content = data[1];
                ControllerUtils.postController.create(writerId, content);
            }

            if (command.equals("update")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                data = data[1].split(" ", 2);
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[0]);
                String content = data[1];
                ControllerUtils.postController.update(id, content);
            }

            if (command.equals("addlabel")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                data = data[1].split(" ", 2);
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[0]);
                int labelId = Integer.parseInt(data[1]);
                ControllerUtils.postController.addLabel(id, labelId);
            }

            if (command.equals("delete")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[1]);
                ControllerUtils.postController.deleteById(id);
            }

            if (command.equals("dellabel")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                data = data[1].split(" ", 2);
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[0]);
                int labelId = Integer.parseInt(data[1]);
                ControllerUtils.postController.dellabel(id, labelId);
            }

            if (command.equals("list")) {
                if (data.length != 1) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                ControllerUtils.postController.list();
            }

            if (command.equals("get")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[1]);
                ControllerUtils.postController.getById(id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Bad input. Type 'help' for help.");
        }
    }
}
