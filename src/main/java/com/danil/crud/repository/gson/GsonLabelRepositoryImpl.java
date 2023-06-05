package com.danil.crud.repository.gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.danil.crud.model.Label;
import com.danil.crud.repository.LabelRepository;
import com.danil.crud.utils.RepositoryUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonLabelRepositoryImpl implements LabelRepository {
    private final File FILE;
    private final Gson GSON = new Gson();

    public GsonLabelRepositoryImpl() {
        this.FILE = new File(RepositoryUtils.LABEL_REPOSITORY_FILENAME);
        File parent = this.FILE.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try {
            this.FILE.createNewFile();
        } catch (IOException e) {
            System.err.print("Cannot create " + RepositoryUtils.LABEL_REPOSITORY_FILENAME + " file! ");
            System.err.println(e);
        }
    }

    @Override
    public Label create(Label label) {
        if (label == null) {
            throw new NullPointerException("Tried to create null label");
        }

        List<Label> currentLabels = readLabelsFromFile();
        Integer generatedId = generateMaxId(currentLabels);
        label.setId(generatedId);
        currentLabels.add(label);
        writeLabelsToFile(currentLabels);
        return label;
    }

    @Override
    public Label update(Label label) {
        if (label == null) {
            throw new NullPointerException("Tried to update null label");
        }

        List<Label> currentLabels = readLabelsFromFile();

        List<Label> updatedList = currentLabels.stream()
                .map(e -> {
                    if (e.getId().equals(label.getId())) {
                        return label;
                    }
                    return e;
                })
                .collect(Collectors.toList());
        writeLabelsToFile(updatedList);
        return label;
    }

    @Override
    public List<Label> getAll() {
        return readLabelsFromFile();
    }

    @Override
    public Label getById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to get label with null id");
        }

        return readLabelsFromFile().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to delete label by null id");
        }

        List<Label> currentLabels = readLabelsFromFile();
        currentLabels.forEach(e -> {
            if (e.getId().equals(id)) {
                e.delete();
            }
        });
        writeLabelsToFile(currentLabels);
    }

    private List<Label> readLabelsFromFile() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + FILE.getName() + ": " + e.getMessage());
            return null;
        }
        TypeToken<ArrayList<Label>> collectionType = new TypeToken<ArrayList<Label>>() {
        };
        List<Label> result = GSON.fromJson(contents, collectionType);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    private void writeLabelsToFile(List<Label> collection) {
        if (collection == null) {
            throw new NullPointerException("Tried to create null collection");
        }

        String json = GSON.toJson(collection);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Cannot write to " + FILE.getName() + ": " + e.getMessage());
        }
    }

    private Integer generateMaxId(List<Label> labels) {
        return labels.stream().mapToInt(Label::getId).max().orElse(-1) + 1;
    }
}
