package com.danil.crud.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.danil.crud.model.Label;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonLabelRepositoryImpl implements LabelRepository {
    private static int maxID = 0;
    private final File file;

    public GsonLabelRepositoryImpl(String filename) {
        this.file = new File(filename);
        this.file.getParentFile().mkdirs();
        setMaxId();
    }

    @Override
    public void create(Label label) {
        if (label == null) {
            throw new NullPointerException("Tried to create null label");
        }

        HashMap<Integer, Label> collection = getAll();
        if (collection == null) {
            collection = new HashMap<>();
        }
        if (!collection.containsKey(label.getId())) {
            collection.put(label.getId(), label);
            createAndUpdateAll(collection);
            setMaxId();
        }
    }

    @Override
    public void update(Label label) {
        if (label == null) {
            throw new NullPointerException("Tried to update null label");
        }

        HashMap<Integer, Label> collection = getAll();
        if (collection == null) {
            return;
        }

        Label target = collection.get(label.getId());
        if (target == null) {
            return;
        }
        collection.put(label.getId(), label);
        createAndUpdateAll(collection);
    }

    @Override
    public HashMap<Integer, Label> getAll() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + file.getName() + ": " + e.getMessage());
            return null;
        }

        TypeToken<ArrayList<Label>> collectionType = new TypeToken<ArrayList<Label>>() {
        };
        Gson gson = new Gson();
        ArrayList<Label> labelList = gson.fromJson(contents, collectionType);
        HashMap<Integer, Label> result = new HashMap<>();
        if (labelList == null) {
            return result;
        }

        for (Label label : labelList) {
            result.put(label.getId(), label);
        }
        return result;
    }

    @Override
    public Label getById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to get label with null id");
        }
        HashMap<Integer, Label> collection = getAll();
        if (collection == null) {
            return null;
        }
        return collection.get(id);
    }

    @Override
    public void createAndUpdateAll(HashMap<Integer, Label> collection) {
        if (collection == null) {
            throw new NullPointerException("Tried to create null collection");
        }

        HashMap<Integer, Label> fullCollection = getAll();
        if (fullCollection == null) {
            fullCollection = new HashMap<>();
        }
        for (Map.Entry<Integer, Label> entry : collection.entrySet()) {
            fullCollection.put(entry.getKey(), entry.getValue());
        }

        Gson gson = new Gson();
        String json = gson.toJson(fullCollection.values());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Cannot write to " + file.getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to delete label by null id");
        }

        HashMap<Integer, Label> collection = getAll();
        if (collection == null) {
            return;
        }
        Label target = collection.get(id);
        if (target != null) {
            target.delete();
            collection.put(id, target);
            createAndUpdateAll(collection);
        }
    }

    @Override
    public int getMaxId() {
        return maxID;
    }

    private void setMaxId() {
        HashMap<Integer, Label> collection = getAll();
        if (collection == null) {
            maxID = 0;
            return;
        }
        Collection<Label> values = collection.values();
        for (Label val : values) {
            if (maxID <= val.getId()) {
                maxID = val.getId() + 1;
            }
        }
    }
}
