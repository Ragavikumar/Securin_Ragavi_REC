package com.nvd.cve;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvd.cve.post.PostRepository;
import com.nvd.cve.post.post;
import org.springframework.asm.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.MarshalledObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
public class DataLoader<Post> implements CommandLineRunner {
    private final ObjectMapper objectMapper;

    private final PostRepository postRepository;

    public DataLoader(ObjectMapper objectMapper, PostRepository postRepository){
        this.objectMapper = objectMapper;
        this.postRepository=postRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        List<Post> posts =new ArrayList<>();
        JsonNode json;
        try (InputStream inputStream =TypeReference.class.getResourceAsStream("/data/cve.json")) {
           json = objectMapper.readValue(inputStream, JsonNode.class);
        }
        catch (IOException e){
            throw new RuntimeException("Failed to read JSON data", e);
        }

        JsonNode edges =getEdges(json);
        for (JsonNode edge :edges){
            posts.add(createPostFromNode(edge));
        }
        postRepository.saveAll(posts);
    }

    private Post createPostFromNode(JsonNode edge) {
        JsonNode node = edge.get("cve");


        String id = node.get("id").asText();
        String sourceIdentifier = node.get("sourceIdentifier").asText();
        String published = node.get("published").asText();
        String lastModified = node.get("lastModified").asText();
        String vulnStatus = node.get("vulnStatus").asText();


        return (Post) new post(id,sourceIdentifier,published ,lastModified, vulnStatus);
    }

    private JsonNode getEdges(JsonNode json) {
        return Optional.ofNullable(json)
                .map(j -> j.get("data"))
                .map(j -> j.get("allPost"))
                .map(j -> j.get("edges"))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }


}
