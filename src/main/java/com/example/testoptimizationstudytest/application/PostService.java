package com.example.testoptimizationstudytest.application;

import com.example.testoptimizationstudytest.domain.post.Post;
import com.example.testoptimizationstudytest.domain.post.repository.PostRepository;
import com.example.testoptimizationstudytest.presentation.dto.PostRequest;
import com.example.testoptimizationstudytest.presentation.dto.PostResponse;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse create(PostRequest postRequest) {
        String title = postRequest.getTitle();
        String content = postRequest.getContent();

        Post save = postRepository.save(new Post(null, title, content));

        return new PostResponse(
            save.getId(),
            save.getTitle(),
            save.getTitle()
        );
    }
}
