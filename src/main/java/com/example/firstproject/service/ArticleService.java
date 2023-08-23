package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//주방장 기능에 대한 처리 흐름과 흐름의 실패에 대한 트랜잭션 처리 담당
@Slf4j
@Service //Service 선언!(서비스 객체를 스프링 부트에 생성)
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
       Article article = dto.toEntity();
       if (article.getId() != null){
           return null;
       }
       return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
//        1. 수정용 엔티티 생성
        Article article = dto.toEntity();
        log.info("id:{}, article:{}", id, article.toString());

//        2. 대상 엔티티 찾기
        Article target = articleRepository.findById(id).orElse(null);

//        3. 잘못된 요청 처리(대상이 없거나, id가 다른경우)
        if (target == null || id != article.getId()){
            log.info("잘못된 요청! id:{}, article:{}", id, article.toString());
            return null;
        }

//        4. 업데이트 및 정상 응답(200)
        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {
        //대상 찾기
        Article target = articleRepository.findById(id).orElse(null);

        //잘못된 요청 처리
        if (target == null) {
            return null;
        }
        //대상 삭제
        articleRepository.delete(target);

        //데이터 반환
        return target;
    }

    @Transactional //해당 메소드를 트랜잭션으로 묶는다! 코드가 실행되다가 실패하게 되면 이전 상태로 롤백!
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // dto 묶음을 entity 묶음으로 변환
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());
        // entity 묶음을 db로 저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        // 강제 예외 발생
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결재 실패!")
        );
        // 결과 값 반환
        return articleList;
    }
}
