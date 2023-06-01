package com.danil.crud.controller;

import java.util.ArrayList;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.repository.GsonLabelRepositoryImpl;
import com.danil.crud.repository.LabelRepository;

public class LabelController {
    LabelRepository repository = new GsonLabelRepositoryImpl("db/labels.json");

    public void save(String content) {
        repository.save(new Label(repository.getMaxId(), content));
    }

    public List<Label> getAll() {
        return new ArrayList<Label>(repository.getAllExceptDeleted().values());
    }

    public void update(int id, String content) {
        repository.update(new Label(id, content));
    }

    public void delete(int id) {
        repository.deleteById(id);
    }
}
