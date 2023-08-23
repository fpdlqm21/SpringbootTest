package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j //로깅을 위한 어노테이션(블랙박스역할)
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){
//        System.out.println(form.toString()); 로깅기능(블랙박스 역할)으로 대체!
        log.info(form.toString());

        //Dto를 Entity객체로 변환
        Article article = form.toEntity();
//        System.out.println(article);
        log.info(article.toString());
        //Repository에게 Entity를 전달하여 DB에 저장하게 함
        Article saved = articleRepository.save(article);
//        System.out.println(saved.toString());
        log.info(saved.toString());

        return "redirect:/articles/" + saved.getId(); //Redirect설정
    }

    @GetMapping("/articles/{id}") //url주소를 통해 컨트롤러로 받아오겠다는 어노테이션
    public String show(@PathVariable Long id, Model model){ //위의 url에서 id를 받아오겠다는 어노테이션
        log.info("id = " + id);

        //1. id로 data를 가져옴!
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //2. 가져온 데이터를 모델에 등록!
        model.addAttribute("article", articleEntity);

        //3. 보여줄 페이지를 설정!
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model){
        //1. 모든 Article을 가져온다!
        // articleRepository의 findAll()이 iteratable형태라 ArrayList로 반환되도록 인터페이스 수정
        List<Article> articleEntityList = articleRepository.findAll();

        //2. 가져온 article 묶음을 뷰로 전달!
        model.addAttribute("articleList", articleEntityList);

        //3. 뷰페이지 설정!
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit") //여기서는 중괄호 하나로 해야 인식함
    public String edit(@PathVariable Long id, Model model){ //url에 있는 변수를 파라메터로 가져온다(PathVariable 어노테이션 이용)
        //수정할 데이터를 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 모델에 데이터를 등록 (뷰 페이지에서 사용하기 위해서)
        model.addAttribute("article", articleEntity);

        //뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());

        // 1. dto를 entity로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());

        // 2. entity를 db로 저장
        // 2-1. db에 기존 데이터를 가져온다.
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        //data가 있으면 entity연결, 없으면 null

        // 2-2. 기존 데이터가 있다면 값을 갱신
        if (target != null){
            articleRepository.save(articleEntity); //엔티티가 db로 갱신
        }

        // 3. 수정결과를 페이지로 리다이렉트 한다
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete") //html에서 post get밖에 요청 안되니 get으로 받음
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다!");

        //1. 삭제 대상을 가져온다!
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        //2. 대상을 삭제 한다.
        if (target != null){
            //repository가 해당 객체를 삭제한다.
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다!");
//            쓰고 바로 사라지는 휘발성(일회성) 데이터 등록
        }

        //3. 결과 페이지로 리다이렉트 한다!, 휘발성 데이터도 보낸다
        return "redirect:/articles";
    }
}
