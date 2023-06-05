package com.danil.crud.repository.gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.repository.LabelRepository;
import com.danil.crud.repository.PostRepository;
import com.danil.crud.repository.deserializer.PostDeserializer;
import com.danil.crud.utils.RepositoryUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class GsonPostRepositoryImpl implements PostRepository {
    private final File FILE;
    private final LabelRepository labelRepository = new GsonLabelRepositoryImpl();

    public GsonPostRepositoryImpl() {
        this.FILE = new File(RepositoryUtils.POST_REPOSITORY_FILENAME);
        File parent = this.FILE.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try {
            this.FILE.createNewFile();
        } catch (IOException e) {
            System.err.print("Cannot create " + RepositoryUtils.POST_REPOSITORY_FILENAME + " file! ");
            System.err.println(e);
        }
    }

    @Override
    public Post create(Post post) {
        if (post == null) {
            throw new NullPointerException("Tried to create null post");
        }

        List<Post> currentPosts = readPostsFromFile();
        Integer generatedId = generateMaxId(currentPosts);
        post.setId(generatedId);
        currentPosts.add(post);
        writePostsToFile(currentPosts);
        return post;
    }

    @Override
    public Post update(Post post) {
        if (post == null) {
            throw new NullPointerException("Tried to update null post");
        }

        List<Post> currentPosts = readPostsFromFile();

        List<Post> updatedPosts = currentPosts.stream()
                .map(e -> {
                    if (e.getId().equals(post.getId())) {
                        return post;
                    }
                    return e;
                })
                .collect(Collectors.toList());
        writePostsToFile(updatedPosts);
        return post;
    }

    @Override
    public List<Post> getAll() {
        return readPostsFromFile();
    }

    @Override
    public Post getById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to get post with null id");
        }

        return readPostsFromFile().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to delete post by null id");
        }

        List<Post> currentPosts = readPostsFromFile();
        currentPosts.forEach(e -> {
            if (e.getId().equals(id)) {
                e.delete();
            }
        });
        writePostsToFile(currentPosts);
    }

    private List<Post> readPostsFromFile() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + FILE.getName() + ": " + e.getMessage());
            return null;
        }

        TypeToken<ArrayList<Post>> collectionType = new TypeToken<ArrayList<Post>>() {
        };
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Post.class, new PostDeserializer());
        Gson gson = builder.create();

        List<Post> result = gson.fromJson(contents, collectionType);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    private void writePostsToFile(List<Post> collection) {
        if (collection == null) {
            throw new NullPointerException("Tried to create and update null collection");
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

        String json = gson.toJson(collection);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Cannot write to " + FILE.getName() + ": " + e.getMessage());
        }

        collection.forEach(post -> {
            post.getLabels().forEach(label -> {
                if (labelRepository.getById(label.getId()) != null) {
                    labelRepository.update(label);
                } else {
                    labelRepository.create(label);
                }
            });
        });
    }

    private Integer generateMaxId(List<Post> posts) {
        return posts.stream().mapToInt(Post::getId).max().orElse(-1) + 1;
    }
}
