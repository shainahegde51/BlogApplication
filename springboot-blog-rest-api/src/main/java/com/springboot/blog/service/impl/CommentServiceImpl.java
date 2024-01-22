package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
//    @Autowired
    private CommentRepository commentRepository;
    //no need to use @Autowired bcz CommentServiceImpl class we configured as spring bean
    //when ever it has one constructor we can vomit adding @Autowired annotation
    //spring will automatically detect  and inject all the necessary dependencies

//    @Autowired
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper=mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment=mapToEntity(commentDto);
        //Retrive post entity by id
        Post post=postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));

        //set post to comment entity
        comment.setPost(post);

        //save comment entity to db
        Comment newComment=commentRepository.save(comment);
        return mapToDto(newComment);

    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        //get comments by post id
        List<Comment> comments=commentRepository.findByPostId(postId);

        //convert list of comment entities to list comment dto
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        //retrivr post by id
        Post post=postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        //retrive comment by id
        Comment comment=commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        Post post=postRepository.findById(postId).
                orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());
        Comment updatedComment=commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post=postRepository.findById(postId).
                orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        commentRepository.delete(comment);
    }


    private CommentDto mapToDto(Comment comment){
//        CommentDto commentDto=new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        CommentDto commentDto=mapper.map(comment,CommentDto.class);
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
//        Comment comment=new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());

        Comment comment=mapper.map(commentDto,Comment.class);
        return comment;
    }
}
