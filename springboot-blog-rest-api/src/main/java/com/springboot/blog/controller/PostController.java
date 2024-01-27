package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="CRUD REST APIs for Post Resource")
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

//create blog post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @SecurityRequirement(name="Bearer Authentication")
    @Operation(
            summary = "Create Post REST API",
            description = "Create Post REST API is used to save post into database"
    )
    @ApiResponse(responseCode = "201",
                description = "Http Status 201 CREATED")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all post rest api
    @GetMapping
    @Operation(
            summary = "Get All Post REST API",
            description = "Get All Post is used to get all the available post from the database"
    )
    @ApiResponse(responseCode = "200",
            description = "Http Status 200 SUCCESS")
    public PostResponse getAllPosts(
            @RequestParam(value="pageNo",defaultValue = AppConstants. DEFAULT_PAGE_NO,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_NO,required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue =AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
            @RequestParam(value="sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false)String sortDir)
    {
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    //get post by id
    @GetMapping("/{id}")
    @Operation(
            summary = "Get Post By Id REST API",
            description = "Get Post By Id is used to get single  post from the database"
    )
    @ApiResponse(responseCode = "200",
            description = "Http Status 200 SUCCESS")
    public  ResponseEntity<PostDto> getPostById(@PathVariable(name="id")long id){
        return  ResponseEntity.ok(postService.getPostById(id));
    }

    //update post
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @Operation(
            summary = "Update Post REST API",
            description = "Update Post is used to update particular post from the database"
    )
    @ApiResponse(responseCode = "200",
            description = "Http Status 200 SUCCESS")
    public  ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable(name="id") long id){
        PostDto postResponse=postService.updatePost(postDto,id);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @Operation(
            summary = "Delete Post REST API",
            description = "Delete Post is used to delete the particular post from the database"
    )
    @ApiResponse(responseCode = "200",
            description = "Http Status 200 SUCCESS")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id)
    {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully",HttpStatus.OK);
    }

    //build get post by category api

//http:/localhost:8080/api/posts/category/{categoryId}
    @GetMapping("/category/{id}")
    @Operation(
            summary = "Get Post By Category Id REST API",
            description = "Get Post By Category Id is used to get the particular post from the database by the category id"
    )
    @ApiResponse(responseCode = "200",
            description = "Http Status 200 SUCCESS")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable("id") Long categoryId){
      List<PostDto> postDto=  postService.getPostByCategory(categoryId);
      return ResponseEntity.ok(postDto);
    }
}
