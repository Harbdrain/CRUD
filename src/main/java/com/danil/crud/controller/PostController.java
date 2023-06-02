package com.danil.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;
import com.danil.crud.utils.RepositoryUtils;
import com.danil.crud.utils.ViewUtils;

public class PostController {
    public void create(int writerId, String content) {
        if (writerId < 0 || content.isEmpty()) {
            return;
        }

        Writer writer = RepositoryUtils.writerRepository.getById(writerId);
        if (writer == null || writer.isDeleted()) {
            return;
        }
        Post post = new Post(RepositoryUtils.postRepository.getMaxId(), content);
        RepositoryUtils.postRepository.create(post);

        writer.addPost(post);
        RepositoryUtils.writerRepository.update(writer);
        ViewUtils.postView.statusOK();
    }

    public void update(int postId, String content) {
        if (postId < 0 || content.isEmpty()) {
            return;
        }

        Post post = RepositoryUtils.postRepository.getById(postId);
        if (post == null || post.isDeleted()) {
            return;
        }

        post.setContent(content);
        RepositoryUtils.postRepository.update(post);

        ViewUtils.postView.statusOK();
    }

    public void addLabel(int postId, int labelId) {
        if (postId < 0 || labelId < 0) {
            return;
        }

        Post post = RepositoryUtils.postRepository.getById(postId);
        if (post == null || post.isDeleted()) {
            return;
        }

        Label label = RepositoryUtils.labelRepository.getById(labelId);
        if (label == null || label.isDeleted()) {
            return;
        }

        post.addLabel(label);
        RepositoryUtils.postRepository.update(post);
        ViewUtils.postView.statusOK();
    }

    public void deleteById(int id) {
        if (id < 0) {
            return;
        }
        RepositoryUtils.postRepository.deleteById(id);
        ViewUtils.postView.statusOK();
    }

    public void dellabel(int postId, int labelId) {
        if (postId < 0 || labelId < 0) {
            return;
        }

        Post post = RepositoryUtils.postRepository.getById(postId);
        if (post == null || post.isDeleted()) {
            return;
        }

        Iterator<Label> labelIterator = post.getLabels().iterator();
        while (labelIterator.hasNext()) {
            Label label = labelIterator.next();
            if (label.getId() == labelId) {
                labelIterator.remove();
                RepositoryUtils.postRepository.update(post);
                ViewUtils.postView.statusOK();
                break;
            }
        }
    }

    public void list() {
        HashMap<Integer, Post> postMap = RepositoryUtils.postRepository.getAll();
        if (postMap == null) {
            return;
        }

        List<Post> postList = postMap.values().stream()
                .filter(e -> !e.isDeleted())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        for (Post post : postList) {
            cleanPostFromDeletedFields(post);
        }

        ViewUtils.postView.showList(postList);
    }

    public void getById(int id) {
        if (id < 0) {
            return;
        }

        Post post = RepositoryUtils.postRepository.getById(id);
        if (post == null) {
            return;
        }

        cleanPostFromDeletedFields(post);

        ViewUtils.postView.show(post);
    }

    private void cleanPostFromDeletedFields(Post post) {
        Iterator<Label> labelIterator = post.getLabels().iterator();
        while (labelIterator.hasNext()) {
            Label label = labelIterator.next();
            if (label.isDeleted()) {
                labelIterator.remove();
            }
        }
    }
}
