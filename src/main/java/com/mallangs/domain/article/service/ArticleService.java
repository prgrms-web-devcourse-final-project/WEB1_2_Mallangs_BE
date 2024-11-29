package com.mallangs.domain.article.service;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.factory.ArticleFactory;
import com.mallangs.domain.article.factory.ArticleFactoryManager;
import com.mallangs.domain.article.repository.ArticleRepository;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.MemberRole;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final MemberRepository memberRepository;
  private final ArticleFactoryManager factoryManager;
  private final ArticleRepository articleRepository;

  public ArticleResponse createArticle(ArticleCreateRequest articleCreateRequest, Long memberId) {
    Member foundMember = memberRepository.findById(memberId)
        .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

    // 팩토리 매니저를 통해 적절한 팩토리 선택
    System.out.println(articleCreateRequest.getType());
    ArticleFactory factory = factoryManager.getFactory(articleCreateRequest.getType());

    // 팩토리에서 article 생성
    Article article = factory.createArticle(foundMember, articleCreateRequest);
    article.hideInMap(); // published 상태 아니면 map hidden

    // 생성된 article 저장
    Article savedArticle = articleRepository.save(article);

    // 각 타입에 맞는 Response 객체 반환
    return factory.createResponse(savedArticle);
  }

  // 글타래 단건 조회
  public ArticleResponse getArticleById(Long articleId) {
    Article foundArticle = articleRepository.findById(articleId)
        .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));
    ArticleFactory factory = factoryManager.getFactory(foundArticle.getType());
    return factory.createResponse(foundArticle); // ArticleResponse 로 반환 통일되게 팩토리메서드 작성 필요
  }

  // 글타래 전체 조회
  public Page<ArticleResponse> findAllTypeArticles(Pageable pageable) {
    return articleRepository.findAll(pageable)
        .map(article -> {
          ArticleFactory factory = factoryManager.getFactory(article.getType());
          return factory.createResponse(article);
        });
  }

  // 글타래 타입 별 전체/실종/구조 조회 // 장소 카테고리도 설정 가능?
  public List<ArticleResponse> findArticlesByArticleType(String articleType) {
    List<Article> articles = articleRepository.findByArticleType(articleType);
    return articles.stream()
        .map(article -> {
          ArticleFactory factory = factoryManager.getFactory(article.getType());
          return factory.createResponse(article);
        })
        .collect(Collectors.toList());
  }

  // 글타래 멤버 개인 글타래 목록 조회
  public Page<ArticleResponse> findArticlesByMemberId(Pageable pageable, Long memberId) {
    Page<Article> articles = articleRepository.findByMemberId(pageable, memberId);
    return articles.map(article -> {
      ArticleFactory factory = factoryManager.getFactory(article.getType());
      return factory.createResponse(article);
    });
  }

  // 위치 기준 //////////////////

  // 글타래 자신 주변 글타래 목록 조회

  // 검색어 기준

  // 수정 자신의 글타래 (장소는 자신의 글도 수정 삭제 불가) // 관리자는 수정 가능
  public ArticleResponse updateArticle(Long articleId, ArticleCreateRequest articleUpdateRequest,
      Long memberId) {
    Member foundMember = memberRepository.findById(memberId)
        .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

    // 기존 Article 조회
    Article foundArticle = articleRepository.findById(articleId)
        .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));

    // 자신의 글인지 체크
    // 장소인지 체크
    // foundMember role 체크, foundArticle 타입 체크
    if (foundMember.getMemberRole() == MemberRole.ROLE_USER) {
      validateUserArticleAccess(foundArticle, memberId, false);
    }

    ArticleFactory factory = factoryManager.getFactory(foundArticle.getType());
    Article updatedArticle = factory.createArticle(foundArticle.getMember(),
        articleUpdateRequest);

    foundArticle.applyChanges(updatedArticle);

    // 글 상태가 발행이 아닌 경우
    // mapVisibility 상태 변경
    // 사용자 자신의 글타래에서는 볼 수 있으나 지도에는 안뜸
    foundArticle.hideInMap();

    // 저장
    Article savedArticle = articleRepository.save(foundArticle);

    // 응답 반환
    return factory.createResponse(savedArticle);
  }

  // 논리 삭제
  // 사용자 삭제 인 경우 - 데이터에는 남아있음
  // 사용자 논리 삭제 시 map invisible 로 변경, isDeleted 변경
  public void deactivateArticle(Long articleId, Long memberId) {
    Member foundMember = memberRepository.findById(memberId)
        .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

    Article foundArticle = articleRepository.findById(articleId)
        .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));

    if (foundMember.getMemberRole() == MemberRole.ROLE_USER) {
      validateUserArticleAccess(foundArticle, memberId, true);
    }

    // 논리 삭제 처리 상태 변경
    foundArticle.deactivate(); // 사용자 못봄
    articleRepository.save(foundArticle);

  }


  // 물리 삭제 // 관리자
  public void deleteArticle(Long articleId) {
    Article foundArticle = articleRepository.findById(articleId)
        .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));

    articleRepository.deleteById(foundArticle.getId());
  }


  private void validateUserArticleAccess(Article foundArticle, Long memberId, Boolean forDelete) {
    validateArticleAccess(foundArticle, memberId, forDelete);
    validateUserAccessByArticleType(foundArticle, forDelete);
  }

  // 자신의 글인지 체크
  private void validateArticleAccess(Article foundArticle, Long memberId, Boolean forAnyDelete) {
    if (forAnyDelete) {
      if (Objects.equals(foundArticle.getMember().getMemberId(), memberId)) {
        throw new MallangsCustomException(ErrorCode.UNAUTHORIZED_DELETE);
      }
    } else {
      if (Objects.equals(foundArticle.getMember().getMemberId(), memberId)) {
        throw new MallangsCustomException(ErrorCode.UNAUTHORIZED_MODIFY);
      }
    }
  }

  // 장소 접근 체크
  private void validateUserAccessByArticleType(Article foundArticle, Boolean forAnyDelete) {
    String articleType = foundArticle.getType();
    if (forAnyDelete) {
      if (Objects.equals(articleType, "place")) {
        throw new MallangsCustomException(ErrorCode.RESOURCE_NOT_DELETABLE);
      }
    } else {
      if (Objects.equals(articleType, "place")) {
        throw new MallangsCustomException(ErrorCode.RESOURCE_NOT_MODIFIABLE);
      }
    }

  }


}
