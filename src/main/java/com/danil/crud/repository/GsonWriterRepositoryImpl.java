package com.danil.crud.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;
import com.danil.crud.model.WriterStatus;
import com.danil.crud.repository.deserializer.WriterDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class GsonWriterRepositoryImpl implements WriterRepository {
    private static int maxID = 0;
    private final File file;

    public GsonWriterRepositoryImpl(String filename) {
        this.file = new File(filename);
        setMaxId();
    }

    @Override
    public void save(Writer writer) {
        if (writer == null) {
            throw new NullPointerException("Tried to save null writer");
        }

        HashMap<Integer, Writer> collection = getAll();
        if (collection == null) {
            collection = new HashMap<>();
        }
        if (!collection.containsKey(writer.getId())) {
            collection.put(writer.getId(), writer);
            saveAll(collection);
            setMaxId();
        }
    }

    public void saveAll(HashMap<Integer, Writer> collection) {
        if (collection == null) {
            throw new NullPointerException("Tried to save null collection");
        }

        GsonBuilder builder = new GsonBuilder();
        Type listLabelType = new TypeToken<List<Post>>() {
        }.getType();
        builder.registerTypeAdapter(listLabelType, (JsonSerializer<List<Post>>) (src, typeOfSrc, context) -> {
            JsonArray jsonPosts = new JsonArray();

            for (Post post : src) {
                jsonPosts.add(post.getId());
            }
            return jsonPosts;
        });
        Gson gson = builder.create();

        String json = gson.toJson(collection.values());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Cannot write to " + file.getName() + ": " + e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, Writer> getAll() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + file.getName() + ": " + e.getMessage());
            return null;
        }

        TypeToken<ArrayList<Writer>> collectionType = new TypeToken<ArrayList<Writer>>() {
        };
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Writer.class, new WriterDeserializer());
        Gson gson = builder.create();

        ArrayList<Writer> writerList = gson.fromJson(contents, collectionType);
        HashMap<Integer, Writer> result = new HashMap<>();
        if (writerList == null) {
            return result;
        }
        for (Writer writer : writerList) {
            result.put(writer.getId(), writer);
        }
        return result;
    }

    @Override
    public HashMap<Integer, Writer> getAllExceptDeleted() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + file.getName() + ": " + e.getMessage());
            return null;
        }

        TypeToken<ArrayList<Writer>> collectionType = new TypeToken<ArrayList<Writer>>() {
        };
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Post.class, new WriterDeserializer());
        Gson gson = builder.create();

        ArrayList<Writer> writerList = gson.fromJson(contents, collectionType);
        HashMap<Integer, Writer> result = new HashMap<>();
        if (writerList == null) {
            return result;
        }
        for (Writer writer : writerList) {
            if (writer.getStatus() != WriterStatus.DELETED) {
                result.put(writer.getId(), writer);
            }
        }
        return result;
    }

    @Override
    public Writer getById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to get writer with null id");
        }
        HashMap<Integer, Writer> collection = getAll();
        if (collection == null) {
            return null;
        }
        return collection.get(id);
    }

    @Override
    public void update(Writer writer) {
        if (writer == null) {
            throw new NullPointerException("Tried to update null writer");
        }

        HashMap<Integer, Writer> collection = getAll();
        if (collection == null) {
            return;
        }
        if (collection.containsKey(writer.getId())) {
            collection.put(writer.getId(), writer);
            saveAll(collection);
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to delete writer by null id");
        }

        HashMap<Integer, Writer> collection = getAll();
        if (collection == null) {
            return;
        }
        Writer target = collection.get(id);
        if (target != null) {
            target.delete();
            collection.put(id, target);
            saveAll(collection);
        }
    }

    @Override
    public int getMaxId() {
        return maxID;
    }

    private void setMaxId() {
        HashMap<Integer, Writer> collection = getAll();
        if (collection == null) {
            return;
        }
        Collection<Writer> values = collection.values();
        for (Writer val : values) {
            if (maxID <= val.getId()) {
                maxID = val.getId() + 1;
            }
        }
    }
}
