package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(
        description = "PostDto Model Information"
)
public class PostDto {
    private Long id;
    @NotEmpty
    @Size(min=2,message ="Post title should have at least 2 characters")
    @Schema(description = "Blog Post Title")
    //title should not be null and should have 2 character
    private String title;

    @NotEmpty
    @Size(min=10,message ="Post title should have at least 10 characters")
    @Schema(description = "Blog Post Description")
    //description cannot be null  and should have 10chars
    private String description;

    @NotEmpty
    @Schema(description = "Blog Post Content")
    //contest should not be null or empty
    private String content;
    private Set<CommentDto> comments;
    @Schema(description = "Blog Post Category")
    private long categoryId;
}
