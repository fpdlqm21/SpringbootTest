package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Override
    // ArticleController에서 findAll을 썼을 때 iteratable형태로 반환되므로
    //뭐든지 받을 수 있는 ArrayList로 반환되도록 수정
    ArrayList<Article> findAll();
}
