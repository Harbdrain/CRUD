package com.danil.crud.repository.deserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.danil.crud.model.Label;
import com.danil.crud.model.Post;
import com.danil.crud.model.PostStatus;
import com.danil.crud.repository.GsonLabelRepositoryImpl;
import com.danil.crud.repository.LabelRepository;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PostDeserializer implements JsonDeserializer<Post> {

    @Override
    public Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Post result = new Post(jsonObject.get("id").getAsInt(), jsonObject.get("content").getAsString());
        result.setCreated(jsonObject.get("created").getAsInt());
        result.setUpdated(jsonObject.get("updated").getAsInt());
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
        Iterator<JsonElement> iterator = jsonObject.getAsJsonArray("labels").iterator();
        List<Label> labelList = new ArrayList<>();
        LabelRepository labelRepository = new GsonLabelRepositoryImpl("db/labels.json");
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
