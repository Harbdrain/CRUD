package com.danil.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;
import com.danil.crud.repository.GsonWriterRepositoryImpl;
import com.danil.crud.repository.WriterRepository;
import com.danil.crud.view.WriterView;

public class WriterController {
    WriterRepository writerRepository = new GsonWriterRepositoryImpl("db/writers.json");
    WriterView writerView = new WriterView();

    public void create(String firstName, String lastName) {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return;
        }

        Writer writer = new Writer(writerRepository.getMaxId(), firstName, lastName);
        writerRepository.save(writer);
        writerView.statusOK();
    }

    public void update(int id, String firstName, String lastName) {
        if (id < 0 || firstName.isEmpty() || lastName.isEmpty()) {
            return;
        }

        Writer writer = writerRepository.getById(id);
        if (writer == null || writer.isDeleted()) {
            return;
        }

        writer.setFirstName(firstName);
        writer.setLastName(lastName);
        writerRepository.update(writer);

        writerView.statusOK();
    }

    public void delete(int id) {
        if (id < 0) {
            return;
        }

        writerRepository.deleteById(id);
        writerView.statusOK();
    }

    public void list() {
        HashMap<Integer, Writer> writerMap = writerRepository.getAllExceptDeleted();
        if (writerMap == null) {
            return;
        }

        List<Writer> writerList = new ArrayList<>(writerMap.values());
        for (Writer writer : writerList) {
            Iterator<Post> postIterator = writer.getPosts().iterator();
            while (postIterator.hasNext()) {
                Post post = postIterator.next();
                if (post.isDeleted()) {
                    postIterator.remove();
                }
            }
        }

        writerView.showList(writerList);
    }

    public void get(int id) {
        if (id < 0) {
            return;
        }

        Writer writer = writerRepository.getById(id);
        if (writer == null || writer.isDeleted()) {
            return;
        }

        Iterator<Post> postIterator = writer.getPosts().iterator();
        while (postIterator.hasNext()) {
            Post post = postIterator.next();
            if (post.isDeleted()) {
                postIterator.remove();
                continue;
            }

            Iterator<Label> labelIterator = post.getLabels().iterator();
            while (labelIterator.hasNext()) {
                Label label = labelIterator.next();
                if (label.isDeleted()) {
                    labelIterator.remove();
                }
            }
        }

        writerView.show(writer);
    }
}
