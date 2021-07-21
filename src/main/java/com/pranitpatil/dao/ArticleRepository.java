package com.pranitpatil.dao;

import com.pranitpatil.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Override
    Optional<Article> findById(String id);
}
