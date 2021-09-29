package com.example.testoptimizationstudytest.presentation;

import com.example.testoptimizationstudytest.application.PostService;
import com.example.testoptimizationstudytest.presentation.dto.PostRequest;
import com.example.testoptimizationstudytest.presentation.dto.PostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    public ResponseEntity<PostResponse> create(@RequestBody PostRequest postRequest) {
        PostResponse postResponse = postService.create(postRequest);

        return ResponseEntity.ok(postResponse);
    }

}
