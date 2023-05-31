package com.danil.crud.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.danil.crud.model.Label;
import com.danil.crud.model.LabelStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonLabelRepositoryImpl implements LabelRepository {
    private final Gson gson = new Gson();
    private final File file;

    public GsonLabelRepositoryImpl(String filename) {
        this.file = new File(filename);
    }

    public void save(Label label) {
        if (label == null) {
            throw new NullPointerException("Tried to save null label");
        }

        HashMap<Integer, Label> collection = getAll();
        if (!collection.containsKey(label.getId())) {
            collection.put(label.getId(), label);
            saveAll(collection);
        }
    }

    public void update(Label label) {
        if (label == null) {
            throw new NullPointerException("Tried to update null label");
        }

        HashMap<Integer, Label> collection = getAll();
        Label labelToUpdate = collection.get(label.getId());
        if (labelToUpdate != null && labelToUpdate.getStatus() != LabelStatus.DELETED) {
            collection.put(label.getId(), label);
            saveAll(collection);
        }
    }

    public HashMap<Integer, Label> getAll() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + file.getName() + ": " + e.getMessage());
            return new HashMap<>();
        }

        Type token = new TypeToken<ArrayList<Label>>() {
        }.getType();
        ArrayList<Label> labelList = gson.fromJson(contents, token);
        HashMap<Integer, Label> result = new HashMap<>();
        if (labelList == null) {
            return result;
        }
        for (Label label : labelList) {
            result.put(label.getId(), label);
        }
        return result;
    }

    private void saveAll(HashMap<Integer, Label> collection) {
        if (collection == null) {
            throw new NullPointerException("Tried to save null collection");
        }

        String json = gson.toJson(collection.values());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Cannot write to " + file.getName() + ": " + e.getMessage());
        }
    }

    public void deleteById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to delete element by null id");
        }

        HashMap<Integer, Label> collection = getAll();
        Label target = collection.get(id);
        if (target != null) {
            target.delete();
            collection.put(id, target);
            saveAll(collection);
        }
    }
}
