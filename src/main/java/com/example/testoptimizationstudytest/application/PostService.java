package com.example.testoptimizationstudytest.application;

import com.example.testoptimizationstudytest.domain.post.Post;
import com.example.testoptimizationstudytest.domain.post.repository.PostRepository;
import com.example.testoptimizationstudytest.presentation.dto.PostRequest;
import com.example.testoptimizationstudytest.presentation.dto.PostResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse create(PostRequest postRequest) {
        String title = postRequest.getTitle();
        String content = postRequest.getContent();

        Post post = postRepository.save(new Post(null, title, content));

        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow();

        return PostResponse.from(post);
    }

    public PostResponse update(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id).orElseThrow();

        post.updateTitle(postRequest.getTitle());
        post.updateContent(postRequest.getContent());

        return PostResponse.from(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
