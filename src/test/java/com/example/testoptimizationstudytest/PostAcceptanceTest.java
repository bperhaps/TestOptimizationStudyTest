package com.example.testoptimizationstudytest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.registerCustomDateFormat;

import com.example.testoptimizationstudytest.config.db_seperate.DataSourceSelector;
import com.example.testoptimizationstudytest.presentation.dto.PostRequest;
import com.example.testoptimizationstudytest.presentation.dto.PostResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class PostAcceptanceTest extends AcceptanceTest {

    @Autowired
    DataSourceSelector dataSourceSelector;

    private static boolean flag = false;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        if(!flag) {
            dataSourceSelector.toRead();
            bulkCreatePost(100);
            flag = true;
        }
    }

    @Test
    void createPost100() {
        dataSourceSelector.toWrite();
        List<PostResponse> postResponses = bulkCreatePost(100);

        assertThat(postResponses).hasSize(100);
    }

    @Test
    void findPostById_1() {
        dataSourceSelector.toRead();

        PostResponse postResponse = findPostById(1L);
        assertThat(postResponse.getId()).isEqualTo(1);
    }

    @Test
    void findPostById_2() {
        dataSourceSelector.toRead();

        PostResponse postResponse = findPostById(2L);
        assertThat(postResponse.getId()).isEqualTo(2);
    }

    @Test
    void findPostById_3() {
        dataSourceSelector.toRead();

        PostResponse postResponse = findPostById(3L);
        assertThat(postResponse.getId()).isEqualTo(3);
    }

    @Test
    void updateById_1() {
        dataSourceSelector.toWrite();
        bulkCreatePost(100);
        PostRequest update = new PostRequest("updatedTitle", "updatedContent");

        PostResponse postResponse = updatePostById(1L, update);
        assertThat(update)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(postResponse);
    }

    @Test
    void deleteById_1() {
        dataSourceSelector.toWrite();
        bulkCreatePost(100);
        PostRequest update = new PostRequest("updatedTitle", "updatedContent");

        deleteById(1L);
    }

    private List<PostResponse> bulkCreatePost(int size) {
        List<PostResponse> responses = new ArrayList<>();

        for(int i=0; i<size; i++) {
            ExtractableResponse<Response> response = createPost();
            responses.add(response.as(PostResponse.class));
        }

        return responses;
    }

    private ExtractableResponse<Response> createPost() {
        return RestAssured.given().log().all()
            .contentType(ContentType.APPLICATION_JSON.getMimeType())
            .body(new PostRequest("title", "content"))
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    private PostResponse findPostById(Long id) {
        return RestAssured.given().log().all()
            .contentType(ContentType.APPLICATION_JSON.getMimeType())
            .when()
            .get("/posts/{postId}", id)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(PostResponse.class);
    }

    private PostResponse updatePostById(Long id, PostRequest postRequest) {
        return RestAssured.given().log().all()
            .contentType(ContentType.APPLICATION_JSON.getMimeType())
            .body(postRequest)
            .when()
            .put("/posts/{postId}", id)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(PostResponse.class);
    }

    private void deleteById(Long id) {
        RestAssured.given().log().all()
            .contentType(ContentType.APPLICATION_JSON.getMimeType())
            .when()
            .delete("/posts/{postId}", id)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }
}

