package com.example.firstproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity //DB가 해당 객체를 인식 가능! (해당 클래스로 테이블을 만든다!)
@AllArgsConstructor //롬복으로 생성자 생성
@NoArgsConstructor //Default 생성자 생성
@ToString //롬복으로 toString 생성
@Getter //return id
public class Article {

    @Id //대표값
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 id를 알아서 1,2,3,...자동증가
    private Long id;

    @Column
    private String title;
    @Column
    private String content;

    public void patch(Article article) {
        if (article.title != null)
            this.title = article.title;
        if(article.content != null)
            this.content = article.content;
    }
}
