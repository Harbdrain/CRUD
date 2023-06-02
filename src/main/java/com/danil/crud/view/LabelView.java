package com.danil.crud.view;

import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.utils.ControllerUtils;

public class LabelView {
    public void statusOK() {
        System.out.println("OK");
    }

    public void showList(List<Label> list) {
        for (Label label : list) {
            System.out.println(label);
        }
    }

    public void show(Label label) {
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
                ControllerUtils.labelController.create(content);
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
                ControllerUtils.labelController.update(id, content);
            }

            if (command.equals("delete")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }

                int id = Integer.parseInt(data[1]);
                ControllerUtils.labelController.deleteById(id);
            }

            if (command.equals("list")) {
                if (data.length != 1) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                ControllerUtils.labelController.list();
            }

            if (command.equals("get")) {
                if (data.length != 2) {
                    System.out.println("Bad input. Type 'help' for help.");
                    return;
                }
                int id = Integer.parseInt(data[1]);
                ControllerUtils.labelController.getById(id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Bad input. Type 'help' for help.");
        }
    }
}
