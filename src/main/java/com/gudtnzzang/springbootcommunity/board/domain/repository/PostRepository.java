package com.gudtnzzang.springbootcommunity.board.domain.repository;

import com.gudtnzzang.springbootcommunity.board.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository
        extends JpaRepository<Post, Long> {
    //Optional<Post> findById(Long id);
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
    List<Post> findByTitleContaining(String keyword);
}
