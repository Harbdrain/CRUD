package com.danil.crud.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.danil.crud.controller.LabelController;
import com.danil.crud.model.Label;

public class LabelView {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    LabelController controller = new LabelController();

    public void view() {
        showHelp();
        while (true) {
            try {
                String[] line = reader.readLine().split(" ", 2);
                String command = line[0];

                if (command.equals("list")) {
                    List<Label> list = controller.getAll();
                    for (Label label : list) {
                        System.out.println(label);
                    }

                } else if (command.equals("save")) {
                    if (line.length != 2) {
                        continue;
                    }
                    String[] data = line[1].split(" ", 2);
                    if (data.length != 2) {
                        continue;
                    }

                    int id = Integer.parseInt(data[0]);
                    controller.save(id, data[1]);
                    System.out.println("OK");

                } else if (command.equals("update")) {
                    if (line.length != 2) {
                        continue;
                    }
                    String[] data = line[1].split(" ", 2);
                    if (data.length != 2) {
                        continue;
                    }

                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    controller.update(id, name);
                    System.out.println("OK");

                } else if (command.equals("delete")) {
                    if (line.length != 2) {
                        continue;
                    }
                    String data = line[1];
                    int id = Integer.parseInt(data);
                    controller.delete(id);
                    System.out.println("OK");

                } else if (command.equals("exit")) {
                    break;
                } else if (command.equals("help")) {
                    showHelp();
                } else {
                    System.out.println("Bad input. Type 'help' for help.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Bad input. Type 'help' for help.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showHelp() {
        System.out.println("Usage:");
        System.out.println("save <id> <content>");
        System.out.println("list");
        System.out.println("update <id> <content>");
        System.out.println("delete <id>");
        System.out.println("help");
    }
}
