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

import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;
import com.danil.crud.repository.PostRepository;
import com.danil.crud.repository.WriterRepository;
import com.danil.crud.repository.deserializer.WriterDeserializer;
import com.danil.crud.utils.RepositoryUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class GsonWriterRepositoryImpl implements WriterRepository {
    private final File FILE;
    private final PostRepository postRepository = new GsonPostRepositoryImpl();

    public GsonWriterRepositoryImpl() {
        this.FILE = new File(RepositoryUtils.WRITER_REPOSITORY_FILENAME);
        File parent = this.FILE.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try {
            this.FILE.createNewFile();
        } catch (IOException e) {
            System.err.print("Cannot create " + RepositoryUtils.WRITER_REPOSITORY_FILENAME + " file! ");
            System.err.println(e);
        }
    }

    @Override
    public Writer create(Writer writer) {
        if (writer == null) {
            throw new NullPointerException("Tried to create null writer");
        }

        List<Writer> currentWriters = readWritersFromFile();
        writer.setId(generateMaxId(currentWriters));
        currentWriters.add(writer);
        writeWritersToFile(currentWriters);
        return writer;
    }

    @Override
    public Writer update(Writer writer) {
        if (writer == null) {
            throw new NullPointerException("Tried to update null writer");
        }

        List<Writer> currentWriters = readWritersFromFile();

        List<Writer> updatedWriters = currentWriters.stream()
                .map(e -> {
                    if (e.getId().equals(writer.getId())) {
                        return writer;
                    }
                    return e;
                })
                .collect(Collectors.toList());
        writeWritersToFile(updatedWriters);
        return writer;
    }

    @Override
    public List<Writer> getAll() {
        return readWritersFromFile();
    }

    @Override
    public Writer getById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to get writer with null id");
        }
        return readWritersFromFile().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new NullPointerException("Tried to delete writer by null id");
        }

        List<Writer> currentWriters = readWritersFromFile();
        currentWriters.forEach(e -> {
            if (e.getId().equals(id)) {
                e.delete();
            }
        });
        writeWritersToFile(currentWriters);
    }

    private List<Writer> readWritersFromFile() {
        String contents = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            contents = reader.readLine();
        } catch (IOException e) {
            System.err.println("Cannot read " + FILE.getName() + ": " + e.getMessage());
            return null;
        }

        TypeToken<ArrayList<Writer>> collectionType = new TypeToken<ArrayList<Writer>>() {
        };
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Writer.class, new WriterDeserializer());
        Gson gson = builder.create();

        List<Writer> result = gson.fromJson(contents, collectionType);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    private void writeWritersToFile(List<Writer> collection) {
        if (collection == null) {
            throw new NullPointerException("Tried to create and update null collection");
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

        String json = gson.toJson(collection);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Cannot write to " + FILE.getName() + ": " + e.getMessage());
        }

        collection.forEach(writer -> {
            writer.getPosts().forEach(post -> {
                if (postRepository.getById(post.getId()) != null) {
                    postRepository.update(post);
                } else {
                    postRepository.create(post);
                }
            });
        });
    }

    private Integer generateMaxId(List<Writer> writers) {
        return writers.stream().mapToInt(Writer::getId).max().orElse(-1) + 1;
    }
}
