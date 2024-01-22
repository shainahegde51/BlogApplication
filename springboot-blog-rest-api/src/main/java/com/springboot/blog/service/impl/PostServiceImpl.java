package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
//import java.awt.print.Pageable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper) {

        this.postRepository = postRepository;
        this.mapper=mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
       //convert dto to entity
        Post post=mapToEntity(postDto);
        Post newPost=postRepository.save(post);

        //convert entity to dto
        PostDto postResponse= mapToDto(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {  //added pagination and sorting

        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts=postRepository.findAll(pageable);
        //get content for page obj
        List<Post>listOfPost=posts.getContent();

        //we need to convert list of post into postdto, we can use stream method(java 8 stream api)
        List<PostDto> content = listOfPost.stream()
                .map(post -> mapToDto(post))
                .collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }

    @Override
    public PostDto getPostById(long id) {
        Post post=postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        //get post by id  from db and if post is not exist throw error
        Post post=postRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Post", "id",id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost=postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post=postRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Post", "id",id));
        postRepository.delete(post);
    }


    //converted entity into dto
    private PostDto mapToDto(Post post){

//        PostDto postDto=new PostDto();
//        postDto.setId((post.getId()));
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());

//instead of writing abv code we can use this lib to the chore
//        PostDto postDto=mapper.map(post,PostDto.class);
//        return postDto;
        PostDto postDto = mapper.map(post, PostDto.class);

        // Map comments if available
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            Set<CommentDto> commentDtoSet = post.getComments().stream()
                    .map(comment -> mapper.map(comment, CommentDto.class))
                    .collect(Collectors.toSet());

            postDto.setComments(commentDtoSet);
        }

        return postDto;
    }

    //Convert Dto to entity
    private Post mapToEntity(PostDto postDto){
//        Post post=new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        Post post=mapper.map(postDto,Post.class);
        return post;
    }
}
