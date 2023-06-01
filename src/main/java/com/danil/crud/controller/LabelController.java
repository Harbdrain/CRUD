package com.danil.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.repository.GsonLabelRepositoryImpl;
import com.danil.crud.repository.LabelRepository;
import com.danil.crud.view.LabelView;

public class LabelController {
    private final LabelRepository labelRepository = new GsonLabelRepositoryImpl("db/labels.json");
    private final LabelView labelView = new LabelView();

    public void create(String content) {
        if (content.isEmpty()) {
            return;
        }
        labelRepository.save(new Label(labelRepository.getMaxId(), content));
        labelView.statusOK();
    }

    public void update(int id, String content) {
        if (id < 0 || content.isEmpty()) {
            return;
        }

        Label label = labelRepository.getById(id);
        if (label == null || label.isDeleted()) {
            return;
        }

        label.setName(content);
        labelRepository.update(label);

        labelView.statusOK();
    }

    public void deleteById(int id) {
        if (id < 0) {
            return;
        }
        labelRepository.deleteById(id);
        labelView.statusOK();
    }

    public void list() {
        HashMap<Integer, Label> labelMap = labelRepository.getAllExceptDeleted();
        if (labelMap == null) {
            return;
        }
        List<Label> labelList = new ArrayList<Label>(labelMap.values());
        labelView.showList(labelList);
    }

    public void getById(int id) {
        if (id < 0) {
            return;
        }
        Label label = labelRepository.getById(id);
        if (label == null || label.isDeleted()) {
            return;
        }
        labelView.show(label);
    }
}
