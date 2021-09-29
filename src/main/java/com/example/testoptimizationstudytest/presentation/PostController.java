package com.example.testoptimizationstudytest.presentation;

import com.example.testoptimizationstudytest.application.PostService;
import com.example.testoptimizationstudytest.presentation.dto.PostRequest;
import com.example.testoptimizationstudytest.presentation.dto.PostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> create(@RequestBody PostRequest postRequest) {
        PostResponse postResponse = postService.create(postRequest);

        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId) {
        PostResponse postResponse = postService.findById(postId);

        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> update(
        @PathVariable Long postId,
        @RequestBody PostRequest postRequest
    ) {
        PostResponse postResponse = postService.update(postId, postRequest);

        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId) {
        postService.delete(postId);

        return ResponseEntity.noContent().build();
    }



}
