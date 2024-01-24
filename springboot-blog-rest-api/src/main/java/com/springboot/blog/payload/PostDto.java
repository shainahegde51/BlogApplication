package com.springboot.blog.payload;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private Long id;
    @NotEmpty
    @Size(min=2,message ="Post title should have at least 2 characters")
    //title should not be null and should have 2 character
    private String title;

    @NotEmpty
    @Size(min=10,message ="Post title should have at least 10 characters")
    //description cannot be null  and should have 10chars
    private String description;

    @NotEmpty
    //contest should not be null or empty
    private String content;
    private Set<CommentDto> comments;
}
