package com.danil.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;
import com.danil.crud.repository.GsonLabelRepositoryImpl;
import com.danil.crud.repository.GsonPostRepositoryImpl;
import com.danil.crud.repository.GsonWriterRepositoryImpl;
import com.danil.crud.repository.LabelRepository;
import com.danil.crud.repository.PostRepository;
import com.danil.crud.repository.WriterRepository;
import com.danil.crud.view.PostView;

public class PostController {
    private final PostRepository postRepository = new GsonPostRepositoryImpl("db/posts.json");
    private final WriterRepository writerRepository = new GsonWriterRepositoryImpl("db/writers.json");
    private final LabelRepository labelRepository = new GsonLabelRepositoryImpl("db/labels.json");
    private final PostView postView = new PostView();

    public void create(int writerId, String content) {
        if (writerId < 0 || content.isEmpty()) {
            return;
        }

        Writer writer = writerRepository.getById(writerId);
        if (writer == null || writer.isDeleted()) {
            return;
        }
        Post post = new Post(postRepository.getMaxId(), content);
        postRepository.save(post);

        writer.addPost(post);
        writerRepository.update(writer);
        postView.statusOK();
    }

    public void update(int postId, String content) {
        if (postId < 0 || content.isEmpty()) {
            return;
        }

        Post post = postRepository.getById(postId);
        if (post == null || post.isDeleted()) {
            return;
        }

        post.setContent(content);
        postRepository.update(post);

        postView.statusOK();
    }

    public void addLabel(int postId, int labelId) {
        if (postId < 0 || labelId < 0) {
            return;
        }

        Post post = postRepository.getById(postId);
        if (post == null || post.isDeleted()) {
            return;
        }

        Label label = labelRepository.getById(labelId);
        if (label == null || label.isDeleted()) {
            return;
        }

        post.addLabel(label);
        postRepository.update(post);
        postView.statusOK();
    }

    public void delete(int id) {
        if (id < 0) {
            return;
        }
        postRepository.deleteById(id);
        postView.statusOK();
    }

    public void dellabel(int postId, int labelId) {
        if (postId < 0 || labelId < 0) {
            return;
        }

        Post post = postRepository.getById(postId);
        if (post == null || post.isDeleted()) {
            return;
        }

        Iterator<Label> labelIterator = post.getLabels().iterator();
        while (labelIterator.hasNext()) {
            Label label = labelIterator.next();
            if (label.getId() == labelId) {
                // TODO: check if pointers works as i think they works
                labelIterator.remove();
                postRepository.update(post);
                postView.statusOK();
                break;
            }
        }
    }

    public void list() {
        HashMap<Integer, Post> postMap = postRepository.getAllExceptDeleted();
        if (postMap == null) {
            return;
        }

        List<Post> postList = new ArrayList<>(postMap.values());

        for (Post post : postList) {
            Iterator<Label> labelIterator = post.getLabels().iterator();
            while (labelIterator.hasNext()) {
                Label label = labelIterator.next();
                if (label.isDeleted()) {
                    labelIterator.remove();
                }
            }
        }
        postView.showList(postList);
    }

    public void get(int id) {
        if (id < 0) {
            return;
        }

        Post post = postRepository.getById(id);
        if (post == null) {
            return;
        }

        Iterator<Label> labelIterator = post.getLabels().iterator();
        while (labelIterator.hasNext()) {
            Label label = labelIterator.next();
            if (label.isDeleted()) {
                labelIterator.remove();
            }
        }

        postView.show(post);
    }
}
