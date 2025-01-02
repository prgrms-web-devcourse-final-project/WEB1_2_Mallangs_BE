package com.mallangs.domain.article.service;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.response.ArticlePageResponse;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.ArticleType;
import com.mallangs.domain.article.entity.CaseStatus;
import com.mallangs.domain.article.entity.MapVisibility;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final MemberRepository memberRepository;
    private final ArticleFactoryManager factoryManager;
    private final ArticleRepository articleRepository;

    public ArticleResponse createArticle(ArticleCreateRequest articleCreateRequest, Long memberId) {
        //단순 회원조회
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 팩토리 매니저를 통해 적절한 팩토리 선택 -> 팩토리의 역할:
        log.info("articleCreateRequest: {}", articleCreateRequest.toString());
        log.info("articleCreateRequest 설명: {}", articleCreateRequest.getArticleType().getDescription());
        ArticleFactory factory = factoryManager.getFactory(
                articleCreateRequest.getArticleType().getDescription());

        // 팩토리에서 article 생성
        Article article = factory.createArticle(foundMember, articleCreateRequest);
        article.hideInMap(); // published 상태 아니면 map hidden

        Article savedArticle = articleRepository.save(article);

        // 각 타입에 맞는 Response 객체 반환
        return factory.createResponse(savedArticle);
    }


//  // 글타래 단건 조회
//  // 사용자는 map visiblie 인 경우
//  public ArticleResponse getArticleById(Long articleId, String userRole, Long memberId) {
//    Article foundArticle = articleRepository.findById(articleId)
//        .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));
//
//    // ArticleResponse 로 반환 통일되게 팩토리메서드 작성 필요
//    ArticleFactory factory = factoryManager.getFactory(
//        foundArticle.getArticleType().getDescription());
//    log.info("factory : {}",factory);
//
//    if (MemberRole.ROLE_ADMIN.name().equals(userRole)) {
//      log.info("단건 조회 ADMIN{}", userRole);
//      ArticleResponse response = factory.createResponse(foundArticle);
//
//      log.info("단건조회결과1 : {}",response);
//      return response;
//    }
//
//    if (MemberRole.ROLE_USER.name().equals(userRole)) { // 논리 삭제하지 않은 자신의 글이거나 mapVisible
//      if (Objects.equals(foundArticle.getMember().getMemberId(), memberId)
//          && !foundArticle.getIsDeleted()
//          || foundArticle.getMapVisibility() == MapVisibility.VISIBLE) {
//        ArticleResponse response = factory.createResponse(foundArticle);
//        log.info("단건조회결과2 : {}",response);
//
//        return response;
//      }
//      log.info("단건 조회 USER ACCESS DENIED{}", userRole);
//    }
//
//    // 비회원
//    if ("ROLE_GUEST".equals(userRole) && foundArticle.getMapVisibility() == MapVisibility.VISIBLE) {
//      log.info("단건 조회 NOT MEMBER{}", userRole);
//      ArticleResponse response = factory.createResponse(foundArticle);
//      log.info("단건조회결과3 : {}",response);
//
//      return response;
//    } else {
//      log.error("비회원 조회 불가");
//    }
//
//    log.info("단건 조회 OTHER" + userRole);
//    throw new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND);
//  }

    // 글타래 단건 조회 (수정본)
    // 사용자는 map visiblie 인 경우
    public ArticleResponse getArticleById(Long articleId, String userRole, Long memberId) {
        Article foundArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));

        // ArticleResponse 로 반환 통일되게 팩토리메서드 작성 필요
        ArticleFactory factory = factoryManager.getFactory(
                foundArticle.getArticleType().getDescription());
        log.info("factory : {}", factory);

        if (MemberRole.ROLE_ADMIN.name().equals(userRole)) {
            log.info("단건 조회 ADMIN{}", userRole);
            ArticleResponse response = factory.createResponse(foundArticle);

            log.info("단건조회결과1 : {}", response);
            return response;
        }

        if (MemberRole.ROLE_USER.name().equals(userRole)) { // 논리 삭제하지 않은 자신의 글이거나 mapVisible
            if (Objects.equals(foundArticle.getMember().getMemberId(), memberId)
                    && !foundArticle.getIsDeleted()
                    || foundArticle.getMapVisibility() == MapVisibility.VISIBLE) {
                ArticleResponse response = factory.createResponse(foundArticle);
                log.info("단건조회결과2 : {}", response);

                return response;
            }
            log.info("단건 조회 USER ACCESS DENIED{}", userRole);
        }

        // 비회원
        if ("ROLE_GUEST".equals(userRole) && foundArticle.getMapVisibility() == MapVisibility.VISIBLE) {
            log.info("단건 조회 NOT MEMBER{}", userRole);
            ArticleResponse response = factory.createResponse(foundArticle);
            log.info("단건조회결과3 : {}", response);

            return response;
        }

        log.info("단건 조회 OTHER" + userRole);
        throw new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND);
    }

    // 실종 글타래 단건 조회 (수정본)
    // 사용자는 map visiblie 인 경우
    public ArticleResponse getLostArticleById(Long articleId) {
            Article foundArticle = articleRepository.findById(articleId)
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));

        try {
            // ArticleResponse 로 반환 통일되게 팩토리메서드 작성 필요
            ArticleFactory factory = factoryManager.getFactory(
                    foundArticle.getArticleType().getDescription());
            log.info("factory. : {}", factory);

            return factory.createResponse(foundArticle);
        } catch (Exception e) {
            throw new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND);
        }
    }

    // 목격제보 글타래 전체조회
    public List<ArticleResponse> getSightArticleByLostId(Long LostArticleId) {
        List<Article> articles = articleRepository.findSightingArticles(LostArticleId);

        // ArticleResponse 로 반환 통일되게 팩토리메서드 작성 필요
        try {
            return articles.stream()
                    .map(article -> {
                        ArticleFactory factory = factoryManager.getFactory(
                                article.getArticleType().getDescription());
                        return factory.createResponse(article);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("목격제보 글타래 전체조회 실패 {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.INVALID_REQUEST);
        }
    }


    // 관리자만
    // 글타래 전체 조회 // 지도가 아닌 경우
    public ArticlePageResponse findAllTypeArticles(Pageable pageable) {
        Page<Article> articles = articleRepository.findAll(pageable);

        List<ArticleResponse> articleResponses = articles.map(article -> {
            ArticleFactory factory = factoryManager.getFactory(article.getArticleType().getDescription());
            return factory.createResponse(article);
        }).getContent();

        return new ArticlePageResponse(
                articleResponses,
                articles.getTotalElements(),
                articles.getTotalPages(),
                articles.getNumber(),
                articles.getSize()
        );
    }

    // 관리자 목록조회
    // 글타래 타입 별 전체/실종/구조 조회 // 장소 카테고리도 설정 가능? // 대분류
    public ArticlePageResponse findArticlesByArticleType(Pageable pageable,
                                                         String articleType) {

        if (Objects.equals(articleType, "place") || Objects.equals(articleType, "user")) {
            // 장소, 사용자 등록 위치
            boolean isPublicData;
            isPublicData = Objects.equals("place", articleType); // place 면 isPublicData = true

            Page<Article> articles = articleRepository.findPlaceArticlesByType(pageable, isPublicData);

            List<ArticleResponse> articleResponses = articles.map(article -> {
                ArticleFactory factory = factoryManager.getFactory("place");
                return factory.createResponse(article);
            }).getContent();

            return new ArticlePageResponse(
                    articleResponses,
                    articles.getTotalElements(),
                    articles.getTotalPages(),
                    articles.getNumber(),
                    articles.getSize()
            );
        }

        // 실종, 구조
        ArticleType type = ArticleType.valueOf(articleType.toUpperCase());

        Page<Article> articles = articleRepository.findByArticleType(pageable, type);

        List<ArticleResponse> articleResponses = articles.map(article -> {
            ArticleFactory factory = factoryManager.getFactory(article.getArticleType().getDescription());
            return factory.createResponse(article);
        }).getContent();

        return new ArticlePageResponse(
                articleResponses,
                articles.getTotalElements(),
                articles.getTotalPages(),
                articles.getNumber(),
                articles.getSize()
        );
    }


    // 실종 글타래
    // map visibility 기준
    public List<ArticleResponse> findLostArticles(CaseStatus lostStatus) {
        List<Article> articles = articleRepository.findLostArticles(lostStatus);

        return articles.stream()
                .map(article -> {
                    ArticleFactory factory = factoryManager.getFactory(
                            article.getArticleType().getDescription());
                    return factory.createResponse(article);
                })
                .collect(Collectors.toList());
    }

    // 관리자
    // 장소 세부 카테고리 있는 것
    public ArticlePageResponse findPlaceArticlesByCategory(Pageable pageable,
                                                           String articleType, String placeCategory) {
        boolean isPublicData;
        isPublicData = Objects.equals("place", articleType);  // place 인 경우
        Page<Article> articles = articleRepository.findPlaceArticlesByCategory(pageable, isPublicData,
                placeCategory);

        List<ArticleResponse> articleResponses = articles.map(article -> {
            ArticleFactory factory = factoryManager.getFactory("place");
            return factory.createResponse(article);
        }).getContent();

        return new ArticlePageResponse(
                articleResponses,
                articles.getTotalElements(),
                articles.getTotalPages(),
                articles.getNumber(),
                articles.getSize()
        );

    }


    // 글타래 멤버 개인 글타래 목록 조회
    // 논리 삭제 안된 것 조회
    public List<ArticleResponse> findArticlesByMemberId(Long memberId) {
        List<Article> articles = articleRepository.findByMemberId(memberId);

        return articles.stream()
                .map(article -> {
                    ArticleFactory factory = factoryManager.getFactory(
                            article.getArticleType().getDescription());
                    return factory.createResponse(article);
                })
                .collect(Collectors.toList());
    }

    // 검색어 기준
    // 지도 표시 여부 체크 // 사용자, 관리자 모두 지도 표시 여부로 확인
    public List<ArticleResponse> findArticlesByKeyword(String keyword) {
        List<Article> articles = articleRepository.findByTitleContainingOrDescriptionContainingAndMapVisibility(
                keyword,
                keyword);

        return articles.stream()
                .map(article -> {
                    ArticleFactory factory = factoryManager.getFactory(
                            article.getArticleType().getDescription());
                    return factory.createResponse(article);
                })
                .collect(Collectors.toList());
    }


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

        // 글타래 타입 불변인지 체크
        if (!Objects.equals(foundArticle.getArticleType().getDescription(),
                articleUpdateRequest.getArticleType().getDescription())) {
            throw new MallangsCustomException(ErrorCode.INVALID_TYPE_CHANGE);
        }

        ArticleFactory factory = factoryManager.getFactory(
                foundArticle.getArticleType().getDescription());
        Article updatedArticle = factory.createArticle(foundArticle.getMember(),
                articleUpdateRequest);

        foundArticle.applyChanges(updatedArticle);

        // 글 상태가 발행이 아닌 경우
        // mapVisibility 상태 변경
        // 사용자 자신의 글타래에서는 볼 수 있으나 지도에는 안뜸
        foundArticle.hideInMap();

        Article savedArticle = articleRepository.save(foundArticle);

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
            if (!Objects.equals(foundArticle.getMember().getMemberId(), memberId)) {
                throw new MallangsCustomException(ErrorCode.UNAUTHORIZED_DELETE);
            }
        } else {
            if (!Objects.equals(foundArticle.getMember().getMemberId(), memberId)) {
                throw new MallangsCustomException(ErrorCode.UNAUTHORIZED_MODIFY);
            }
        }
    }

    // 장소 접근 체크
    private void validateUserAccessByArticleType(Article foundArticle, Boolean forAnyDelete) {
        String articleType = foundArticle.getArticleType().getDescription();
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
