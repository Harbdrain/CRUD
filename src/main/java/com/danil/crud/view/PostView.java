package com.danil.crud.view;

import java.util.List;

import com.danil.crud.controller.PostController;
import com.danil.crud.model.Label;
import com.danil.crud.model.Post;

public class PostView {
    PostController postController = new PostController();

    private void statusOK() {
        System.out.println("OK");
    }

    private void showList(List<Post> list) {
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

    private void show(Post post) {
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
                Post result = postController.create(writerId, content);
                if (result != null) {
                    statusOK();
                }
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
                Post result = postController.update(id, content);
                if (result != null) {
                    statusOK();
                }
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
                Post result = postController.addLabel(id, labelId);
                if (result != null) {
                    statusOK();
                }
            }

            if (command.equals("delete")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[1]);
                postController.deleteById(id);
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
                Post result = postController.dellabel(id, labelId);
                if (result != null) {
                    statusOK();
                }
            }

            if (command.equals("list")) {
                if (data.length != 1) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                List<Post> result = postController.list();
                showList(result);
            }

            if (command.equals("get")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[1]);
                Post result = postController.getById(id);
                if (result != null) {
                    show(result);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Bad input. Type 'help' for help.");
        }
    }
}
