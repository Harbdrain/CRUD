package com.danil.crud.repository.deserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.model.PostStatus;
import com.danil.crud.repository.LabelRepository;
import com.danil.crud.repository.gson.GsonLabelRepositoryImpl;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PostDeserializer implements JsonDeserializer<Post> {
    private final LabelRepository labelRepository = new GsonLabelRepositoryImpl();

    @Override
    public Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Post result = new Post();
        result.setId(jsonObject.get("id").getAsInt());
        result.setContent(jsonObject.get("content").getAsString());
        result.setCreated(jsonObject.get("created").getAsLong());
        result.setUpdated(jsonObject.get("updated").getAsLong());
        String status = jsonObject.get("status").getAsString();
        if (status.equals("ACTIVE")) {
            result.setStatus(PostStatus.ACTIVE);
        }
        if (status.equals("DELETED")) {
            result.setStatus(PostStatus.DELETED);
        }
        if (status.equals("UNDER_REVIEW")) {
            result.setStatus(PostStatus.UNDER_REVIEW);
        }
        result.setLabels(new ArrayList<>());
        Iterator<JsonElement> iterator = jsonObject.getAsJsonArray("labels").iterator();
        List<Label> labelList = new ArrayList<>();
        while (iterator.hasNext()) {
            int labelId = iterator.next().getAsInt();
            Label label = labelRepository.getById(labelId);
            if (label != null) {
                labelList.add(label);
            }
        }
        result.setLabels(labelList);
        return result;
    }
}
