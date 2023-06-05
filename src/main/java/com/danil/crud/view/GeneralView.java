package com.danil.crud.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GeneralView {
    LabelView labelView = new LabelView();
    PostView postView = new PostView();
    WriterView writerView = new WriterView();

    public void processInput(String input) {
        if (input.equals("help")) {
            showHelp();
            return;
        }
        if (input.equals("exit")) {
            return;
        }

        String[] data = input.split(" ", 2);
        if (data.length != 2) {
            System.out.println("Bad input. Type 'help' for help.");
            return;
        }

        String command = data[0];
        if (command.equals("label")) {
            labelView.processInput(data[1]);
        } else if (command.equals("post")) {
            postView.processInput(data[1]);
        } else if (command.equals("writer")) {
            writerView.processInput(data[1]);
        } else {
            System.out.println("Bad input. Type 'help' for help.");
        }
    }

    private void showHelp() {
        StringBuilder builder = new StringBuilder();
        builder.append("label create <content>\n");
        builder.append("label update <label_id> <new_content>\n");
        builder.append("label delete <label_id>\n");
        builder.append("label list\n");
        builder.append("label get <label_id>\n\n");
        builder.append("post create <writer_id> <content>\n");
        builder.append("post update <post_id> <new_content>\n");
        builder.append("post addlabel <post_id> <label_id>\n");
        builder.append("post delete <post_id>\n");
        builder.append("post dellabel <post_id> <label_id>\n");
        builder.append("post list\n");
        builder.append("post get <post_id>\n\n");
        builder.append("writer create <first_name> <last_name>\n");
        builder.append("writer update <writer_id> <first_name> <last_name>\n");
        builder.append("writer delete <writer_id>\n");
        builder.append("writer list\n");
        builder.append("writer get <writer_id>\n\n");
        builder.append("exit");
        System.out.println(builder);
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            do {
                System.out.println("==================================");
                input = reader.readLine();
                processInput(input);
            } while (!input.equals("exit"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
