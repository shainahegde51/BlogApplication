package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CommentDto {
    private long id;

    @NotEmpty(message = "Name should not be null or empty")
    //NAME SHOULD NOT BE NULL OR EMPTY
    private String name;


    @NotEmpty(message = "Email should not be null or empty")
    @Email
    //email should not be null and add email field validation
    private String email;

    @NotEmpty
    @Size(min=10,message = "Comment body must be minimum 10 characters")
    //body should not be empty and comment body at least have 10chars

    private String body;
}
