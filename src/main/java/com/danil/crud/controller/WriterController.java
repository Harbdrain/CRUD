package com.danil.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;
import com.danil.crud.repository.WriterRepository;
import com.danil.crud.utils.RepositoryUtils;
import com.danil.crud.utils.ViewUtils;
import com.danil.crud.view.WriterView;

public class WriterController {
    public void create(String firstName, String lastName) {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return;
        }

        Writer writer = new Writer(RepositoryUtils.writerRepository.getMaxId(), firstName, lastName);
        RepositoryUtils.writerRepository.create(writer);
        ViewUtils.writerView.statusOK();
    }

    public void update(int id, String firstName, String lastName) {
        if (id < 0 || firstName.isEmpty() || lastName.isEmpty()) {
            return;
        }

        Writer writer = RepositoryUtils.writerRepository.getById(id);
        if (writer == null || writer.isDeleted()) {
            return;
        }

        writer.setFirstName(firstName);
        writer.setLastName(lastName);
        RepositoryUtils.writerRepository.update(writer);

        ViewUtils.writerView.statusOK();
    }

    public void deleteById(int id) {
        if (id < 0) {
            return;
        }

        RepositoryUtils.writerRepository.deleteById(id);
        ViewUtils.writerView.statusOK();
    }

    public void list() {
        HashMap<Integer, Writer> writerMap = RepositoryUtils.writerRepository.getAll();
        if (writerMap == null) {
            return;
        }

        List<Writer> writerList = writerMap.values().stream()
                .filter(e -> !e.isDeleted())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        for (Writer writer : writerList) {
            cleanWriterFromDeletedFields(writer);
        }

        ViewUtils.writerView.showList(writerList);
    }

    public void getById(int id) {
        if (id < 0) {
            return;
        }

        Writer writer = RepositoryUtils.writerRepository.getById(id);
        if (writer == null || writer.isDeleted()) {
            return;
        }

        cleanWriterFromDeletedFields(writer);

        ViewUtils.writerView.show(writer);
    }

    private void removeDeletedLabelsInPost(Post post) {
        Iterator<Label> labelIterator = post.getLabels().iterator();
        while (labelIterator.hasNext()) {
            Label label = labelIterator.next();
            if (label.isDeleted()) {
                labelIterator.remove();
            }
        }
    }

    private void cleanWriterFromDeletedFields(Writer writer) {
        Iterator<Post> postIterator = writer.getPosts().iterator();
        while (postIterator.hasNext()) {
            Post post = postIterator.next();
            if (post.isDeleted()) {
                postIterator.remove();
                continue;
            }

            removeDeletedLabelsInPost(post);
        }
    }
}
