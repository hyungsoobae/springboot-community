package com.gudtnzzang.springbootcommunity.board.service;

import com.gudtnzzang.springbootcommunity.board.domain.entity.Comment;
import com.gudtnzzang.springbootcommunity.board.domain.repository.CommentRepository;
import com.gudtnzzang.springbootcommunity.board.domain.repository.PostRepository;
import com.gudtnzzang.springbootcommunity.board.domain.repository.UserRepository;
import com.gudtnzzang.springbootcommunity.board.dto.CommentDto;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;

    }

    private CommentDto convertEntityToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .author(comment.getUser().getEmail())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate())
                .build();
    }

    @Transactional
    public List<CommentDto> getCommentList(Long postId) {

        List<Comment> commentList = commentRepository.findByPost_Id(postId);
        if(commentList.isEmpty()) {
            throw(new ResponseStatusException(HttpStatus.BAD_REQUEST, "post id " + postId + " is not found"));
        }
        List<CommentDto> commentDtoList = new ArrayList<CommentDto>();

        for(Comment comment : commentList) {
            commentDtoList.add(this.convertEntityToDto(comment));
        }

        return commentDtoList;
    }

    @Transactional
    public Long saveComment(CommentDto commentDto) {
        Comment newComment = Comment.builder()
                .id(commentDto.getId())
                .post(postRepository.getOne(commentDto.getPostId()))
                .user(userRepository.findByEmail(commentDto.getAuthor()).get())
                .content(commentDto.getContent())
                .build();

        return commentRepository.save(newComment).getId();
    }

    @Transactional
    public void updateComment(CommentDto commentDto) {

        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "comment id " + commentDto.getId() + " is not found"));
        comment.setContent(commentDto.getContent()); //update by dirty check
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "comment id " + commentId + " is not found"));
        commentRepository.delete(comment);
    }
}
