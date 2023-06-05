package com.danil.crud.view;

import java.util.List;

import com.danil.crud.controller.WriterController;
import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;

public class WriterView {
    WriterController writerController = new WriterController();

    private void statusOK() {
        System.out.println("OK");
    }

    private void showList(List<Writer> list) {
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

    private void show(Writer writer) {
        System.out.println(writer);
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

                String firstName = data[0];
                String lastName = data[1];
                Writer result = writerController.create(firstName, lastName);
                if (result != null) {
                    statusOK();
                }
            }

            if (command.equals("update")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                data = data[1].split(" ", 3);
                if (data.length != 3) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[0]);
                String firstName = data[1];
                String lastName = data[2];
                Writer result = writerController.update(id, firstName, lastName);
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
                writerController.deleteById(id);
            }

            if (command.equals("list")) {
                if (data.length != 1) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                List<Writer> result = writerController.list();
                showList(result);
            }

            if (command.equals("get")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[1]);
                Writer result = writerController.getById(id);
                if (result != null) {
                    show(result);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Bad input. Type 'help' for help.");
        }
    }
}
