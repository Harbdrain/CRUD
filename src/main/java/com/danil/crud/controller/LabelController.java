package com.danil.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.utils.RepositoryUtils;
import com.danil.crud.utils.ViewUtils;

public class LabelController {
    public void create(String content) {
        if (content.isEmpty()) {
            return;
        }
        RepositoryUtils.labelRepository.create(new Label(RepositoryUtils.labelRepository.getMaxId(), content));
        ViewUtils.labelView.statusOK();
    }

    public void update(int id, String content) {
        if (id < 0 || content.isEmpty()) {
            return;
        }

        Label label = RepositoryUtils.labelRepository.getById(id);
        if (label == null || label.isDeleted()) {
            return;
        }

        label.setName(content);
        RepositoryUtils.labelRepository.update(label);

        ViewUtils.labelView.statusOK();
    }

    public void deleteById(int id) {
        if (id < 0) {
            return;
        }
        RepositoryUtils.labelRepository.deleteById(id);
        ViewUtils.labelView.statusOK();
    }

    public void list() {
        HashMap<Integer, Label> labelMap = RepositoryUtils.labelRepository.getAll();
        if (labelMap == null) {
            return;
        }
        List<Label> labelList = labelMap.values().stream()
                .filter(e -> !e.isDeleted())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        ViewUtils.labelView.showList(labelList);
    }

    public void getById(int id) {
        if (id < 0) {
            return;
        }
        Label label = RepositoryUtils.labelRepository.getById(id);
        if (label == null || label.isDeleted()) {
            return;
        }
        ViewUtils.labelView.show(label);
    }
}
