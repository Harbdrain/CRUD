package com.danil.crud.controller;

import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.LabelStatus;
import com.danil.crud.repository.LabelRepository;
import com.danil.crud.repository.gson.GsonLabelRepositoryImpl;

public class LabelController {
    private final LabelRepository labelRepository = new GsonLabelRepositoryImpl();

    public Label create(String name) {
        if (name.isEmpty()) {
            return null;
        }
        Label label = new Label();
        label.setName(name);
        label.setStatus(LabelStatus.ACTIVE);
        return labelRepository.create(label);
    }

    public Label update(int id, String content) {
        if (id < 0 || content.isEmpty()) {
            return null;
        }

        Label label = labelRepository.getById(id);
        if (label == null) {
            return null;
        }

        label.setName(content);
        return labelRepository.update(label);
    }

    public void deleteById(int id) {
        if (id < 0) {
            return;
        }
        labelRepository.deleteById(id);
    }

    public List<Label> list() {
        return labelRepository.getAll();
    }

    public Label getById(int id) {
        if (id < 0) {
            return null;
        }
        return labelRepository.getById(id);
    }
}
