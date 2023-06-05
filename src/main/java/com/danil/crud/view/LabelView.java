package com.danil.crud.view;

import java.util.List;

import com.danil.crud.controller.LabelController;
import com.danil.crud.model.Label;

public class LabelView {
    LabelController labelController = new LabelController();

    private void statusOK() {
        System.out.println("OK");
    }

    private void showList(List<Label> list) {
        for (Label label : list) {
            System.out.println(label);
        }
    }

    private void show(Label label) {
        System.out.println(label);
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

                String content = data[1];
                Label result = labelController.create(content);
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
                Label result = labelController.update(id, content);
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
                labelController.deleteById(id);
                statusOK();
            }

            if (command.equals("list")) {
                if (data.length != 1) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                List<Label> result = labelController.list();
                showList(result);
            }

            if (command.equals("get")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                int id = Integer.parseInt(data[1]);
                Label result = labelController.getById(id);
                if (result != null) {
                    show(result);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Bad input. Type 'help' for help.");
        }
    }
}
