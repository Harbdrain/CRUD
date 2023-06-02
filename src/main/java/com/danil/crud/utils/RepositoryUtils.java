package com.danil.crud.utils;

import com.danil.crud.repository.GsonLabelRepositoryImpl;
import com.danil.crud.repository.GsonPostRepositoryImpl;
import com.danil.crud.repository.GsonWriterRepositoryImpl;
import com.danil.crud.repository.LabelRepository;
import com.danil.crud.repository.PostRepository;
import com.danil.crud.repository.WriterRepository;

public class RepositoryUtils {
    public static final String labelRepositoryFilename = "db/labels.json";
    public static final String postRepositoryFilename = "db/posts.json";
    public static final String writerRepositoryFilename = "db/writers.json";

    public static final LabelRepository labelRepository = new GsonLabelRepositoryImpl(labelRepositoryFilename);
    public static final PostRepository postRepository = new GsonPostRepositoryImpl(postRepositoryFilename);
    public static final WriterRepository writerRepository = new GsonWriterRepositoryImpl(writerRepositoryFilename);
}
