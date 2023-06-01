package com.danil.crud.view;

import java.util.List;

import com.danil.crud.model.Label;

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
}
