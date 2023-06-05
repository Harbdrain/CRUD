package com.danil.crud.controller;

import java.util.List;
import java.util.ArrayList;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.model.PostStatus;
import com.danil.crud.model.Writer;
import com.danil.crud.repository.LabelRepository;
import com.danil.crud.repository.PostRepository;
import com.danil.crud.repository.WriterRepository;
import com.danil.crud.repository.gson.GsonLabelRepositoryImpl;
import com.danil.crud.repository.gson.GsonPostRepositoryImpl;
import com.danil.crud.repository.gson.GsonWriterRepositoryImpl;

public class PostController {
    private final LabelRepository labelRepository = new GsonLabelRepositoryImpl();
    private final PostRepository postRepository = new GsonPostRepositoryImpl();
    private final WriterRepository writerRepository = new GsonWriterRepositoryImpl();

    public Post create(int writerId, String content) {
        if (writerId < 0 || content.isEmpty()) {
            return null;
        }

        Writer writer = writerRepository.getById(writerId);
        if (writer == null || writer.isDeleted()) {
            return null;
        }

        Post post = new Post();
        post.setContent(content);
        Long time = System.currentTimeMillis() / 1000L;
        post.setCreated(time);
        post.setUpdated(time);
        post.setStatus(PostStatus.ACTIVE);
        post.setLabels(new ArrayList<>());
        Post result = postRepository.create(post);

        writer.addPost(post);
        writerRepository.update(writer);

        return result;
    }

    public Post update(int postId, String content) {
        if (postId < 0 || content.isEmpty()) {
            return null;
        }

        Post post = postRepository.getById(postId);
        if (post == null) {
            return null;
        }

        Long time = System.currentTimeMillis() / 1000L;
        post.setContent(content);
        post.setStatus(PostStatus.UNDER_REVIEW);
        post.setUpdated(time);
        return postRepository.update(post);
    }

    public Post addLabel(int postId, int labelId) {
        if (postId < 0 || labelId < 0) {
            return null;
        }

        Post post = postRepository.getById(postId);
        if (post == null) {
            return null;
        }

        Label label = labelRepository.getById(labelId);
        if (label == null) {
            return null;
        }

        post.addLabel(label);
        return postRepository.update(post);
    }

    public void deleteById(int id) {
        if (id < 0) {
            return;
        }
        postRepository.deleteById(id);
    }

    public Post dellabel(int postId, int labelId) {
        if (postId < 0 || labelId < 0) {
            return null;
        }

        Post post = postRepository.getById(postId);
        if (post == null) {
            return null;
        }

        post.getLabels().removeIf(e -> e.getId() == labelId);
        return postRepository.update(post);
    }

    public List<Post> list() {
        return postRepository.getAll();
    }

    public Post getById(int id) {
        if (id < 0) {
            return null;
        }

        return postRepository.getById(id);
    }
}
