package com.danil.crud.controller;

import java.util.List;
import java.util.ArrayList;

import com.danil.crud.model.Writer;
import com.danil.crud.model.WriterStatus;
import com.danil.crud.repository.WriterRepository;
import com.danil.crud.repository.gson.GsonWriterRepositoryImpl;

public class WriterController {
    private final WriterRepository writerRepository = new GsonWriterRepositoryImpl();

    public Writer create(String firstName, String lastName) {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return null;
        }

        Writer writer = new Writer();
        writer.setFirstName(firstName);
        writer.setLastName(lastName);
        writer.setStatus(WriterStatus.ACTIVE);
        writer.setPosts(new ArrayList<>());
        return writerRepository.create(writer);
    }

    public Writer update(int id, String firstName, String lastName) {
        if (id < 0 || firstName.isEmpty() || lastName.isEmpty()) {
            return null;
        }

        Writer writer = writerRepository.getById(id);
        if (writer == null) {
            return null;
        }

        writer.setFirstName(firstName);
        writer.setLastName(lastName);
        return writerRepository.update(writer);
    }

    public void deleteById(int id) {
        if (id < 0) {
            return;
        }

        writerRepository.deleteById(id);
    }

    public List<Writer> list() {
        return writerRepository.getAll();
    }

    public Writer getById(int id) {
        if (id < 0) {
            return null;
        }

        return writerRepository.getById(id);
    }
}
