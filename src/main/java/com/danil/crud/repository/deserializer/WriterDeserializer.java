package com.danil.crud.repository.deserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.danil.crud.model.Post;
import com.danil.crud.model.Writer;
import com.danil.crud.model.WriterStatus;
import com.danil.crud.repository.GsonPostRepositoryImpl;
import com.danil.crud.repository.PostRepository;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class WriterDeserializer implements JsonDeserializer<Writer> {
    @Override
    public Writer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Writer result = new Writer(jsonObject.get("id").getAsInt(), jsonObject.get("firstName").getAsString(),
                jsonObject.get("lastName").getAsString());
        String status = jsonObject.get("status").getAsString();
        if (status.equals("ACTIVE")) {
            result.setStatus(WriterStatus.ACTIVE);
        }
        if (status.equals("DELETED")) {
            result.setStatus(WriterStatus.DELETED);
        }
        Iterator<JsonElement> iterator = jsonObject.getAsJsonArray("posts").iterator();
        List<Post> labelList = new ArrayList<>();
        PostRepository postRepository = new GsonPostRepositoryImpl("db/posts.json");
        while (iterator.hasNext()) {
            int postId = iterator.next().getAsInt();
            Post post = postRepository.getById(postId);
            if (post != null) {
                labelList.add(post);
            }
        }
        result.setPosts(labelList);
        return result;
    }
}
