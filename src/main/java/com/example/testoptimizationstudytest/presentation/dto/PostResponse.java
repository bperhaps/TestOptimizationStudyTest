package com.example.testoptimizationstudytest.presentation.dto;

import com.example.testoptimizationstudytest.domain.post.Post;

public class PostResponse {

    private Long id;
    private String title;
    private String content;

    private PostResponse() {
    }

    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
