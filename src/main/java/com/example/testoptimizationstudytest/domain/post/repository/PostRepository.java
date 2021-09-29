package com.example.testoptimizationstudytest.domain.post.repository;

import com.example.testoptimizationstudytest.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
