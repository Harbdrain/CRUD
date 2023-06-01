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

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.model.PostStatus;
import com.danil.crud.repository.deserializer.PostDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class GsonPostRepositoryImpl implements PostRepository {
    private static int maxID = 0;
    private final File file;

    public GsonPostRepositoryImpl(String filename) {
        this.file = new File(filename);
        setMaxId();
    }

    @Override
    public void save(Post post) {
        if (post == null) {
            throw new NullPointerException("Tried to save null post");
        }

        HashMap<Integer, Post> collection = getAll();
        if (collection == null) {
            collection = new HashMap<>();
        }
        if (!collection.containsKey(post.getId())) {
            collection.put(post.getId(), post);
            saveAll(collection);
            setMaxId();
        }
    }

    public void saveAll(HashMap<Integer, Post> collection) {
        if (collection == null) {
            throw new NullPointerException("Tried to save null collection");
        }

        GsonBuilder builder = new GsonBuilder();
        Type listLabelType = new TypeToken<List<Label>>() {
        }.getType();
        builder.registerTypeAdapter(listLabelType, (JsonSerializer<List<Label>>) (src, typeOfSrc, context) -> {
            JsonArray jsonLabels = new JsonArray();

            for (Label post : src) {
                jsonLabels.add(post.getId());
            }
            return jsonLabels;
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
    public HashMap<Integer, Post> getAll() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + file.getName() + ": " + e.getMessage());
            return null;
        }

        TypeToken<ArrayList<Post>> collectionType = new TypeToken<ArrayList<Post>>() {
        };
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Post.class, new PostDeserializer());
        Gson gson = builder.create();

        ArrayList<Post> postList = gson.fromJson(contents, collectionType);
        HashMap<Integer, Post> result = new HashMap<>();
        if (postList == null) {
            return result;
        }
        for (Post post : postList) {
            result.put(post.getId(), post);
        }
        return result;
    }

    @Override
    public HashMap<Integer, Post> getAllExceptDeleted() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + file.getName() + ": " + e.getMessage());
            return null;
        }

        TypeToken<ArrayList<Post>> collectionType = new TypeToken<ArrayList<Post>>() {
        };
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Post.class, new PostDeserializer());
        Gson gson = builder.create();

        ArrayList<Post> postList = gson.fromJson(contents, collectionType);
        HashMap<Integer, Post> result = new HashMap<>();
        if (postList == null) {
            return result;
        }
        for (Post post : postList) {
            if (post.getStatus() != PostStatus.DELETED) {
                result.put(post.getId(), post);
            }
        }
        return result;
    }

    @Override
    public Post getById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to get post with null id");
        }
        HashMap<Integer, Post> collection = getAll();
        if (collection == null) {
            return null;
        }
        return collection.get(id);
    }

    @Override
    public void update(Post post) {
        if (post == null) {
            throw new NullPointerException("Tried to update null post");
        }

        HashMap<Integer, Post> collection = getAll();
        if (collection == null) {
            return;
        }
        if (collection.containsKey(post.getId())) {
            collection.put(post.getId(), post);
            saveAll(collection);
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to delete post by null id");
        }

        HashMap<Integer, Post> collection = getAll();
        if (collection == null) {
            return;
        }
        Post target = collection.get(id);
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
        HashMap<Integer, Post> collection = getAll();
        if (collection == null) {
            return;
        }
        Collection<Post> values = collection.values();
        for (Post val : values) {
            if (maxID <= val.getId()) {
                maxID = val.getId() + 1;
            }
        }
    }
}
