package com.mallangs.domain.article.controller;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.request.MapBoundsRequest;
import com.mallangs.domain.article.dto.response.ArticlePageResponse;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.dto.response.MapBoundsResponse;
import com.mallangs.domain.article.entity.CaseStatus;
import com.mallangs.domain.article.service.ArticleService;
import com.mallangs.domain.article.service.LocationService;
import com.mallangs.domain.article.validation.ValidationGroups;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Tag(name = "Article Controller", description = "글타래 API")
public class ArticleController {

  private final ArticleService articleService;
  private final LocationService locationService;

  @Operation(summary = "글타래 등록", description = "새로운 글타래를 등록합니다.")
  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @PostMapping
  public ResponseEntity<ArticleResponse> createArticle(
      @Validated(ValidationGroups.CreateGroup.class) @RequestBody ArticleCreateRequest articleCreateRequest,
      @Parameter(description = "현재 인증된 사용자 정보", required = true)
      @AuthenticationPrincipal CustomMemberDetails principal) {
    Long memberId = principal.getMemberId();
    ArticleResponse articleResponse = articleService.createArticle(articleCreateRequest,
        memberId);

    return ResponseEntity.ok(articleResponse);
  }


  // 조회
  // 관리자 전부 조회 가능
  // 회원 visible + 자신의 글 조회 가능
  // 비회원 mapVisible 만 조회 가능
  @Operation(summary = "글타래 단건 조회", description = "글타래를 단건 조회합니다.")
  @GetMapping("/{articleId}")
  public ResponseEntity<ArticleResponse> getArticleByArticleId(
      @Parameter(description = "조회할 글타래 ID", required = true) @PathVariable Long articleId,
      @AuthenticationPrincipal CustomMemberDetails principal) {

    String userRole = (principal == null) ? "GUEST" : principal.getRole();
    Long memberId = (principal == null) ? -1L : principal.getMemberId();

    ArticleResponse articleResponse = articleService.getArticleById(articleId, userRole, memberId);

    return ResponseEntity.ok(articleResponse);
  }

  // 관리자 페이지
  // articleType : lost rescue place user
  // placeCategory : place 하위
  @Operation(summary = "관리자 글타래 전체 조회", description = "관리자가 글타래를 조회합니다.")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @GetMapping("/admin")
  public ResponseEntity<ArticlePageResponse> getArticles(
      @PageableDefault(page = 0, size = 10)
      @Parameter(description = "페이징 요청 정보", required = true) Pageable pageable,
      @RequestParam(value = "articleType", required = false) String articleType, // 대분류
      @RequestParam(value = "placeCategory", required = false) String placeCategory) { // 소분류

    ArticlePageResponse articles;

    if (articleType == null || articleType.isEmpty()) {
      articles = articleService.findAllTypeArticles(pageable); // 전체
    } else {
      if (placeCategory == null || placeCategory.isEmpty()) { // 대분류
        articles = articleService.findArticlesByArticleType(pageable, articleType);
      } else {                                               // 장소, 사용자 등록 위치 소분류
        articles = articleService.findPlaceArticlesByCategory(pageable, articleType, placeCategory);
      }
    }

    return ResponseEntity.ok(articles);
  }

  // 실종 페이지 // list
  // map visible 만 보임
  @Operation(summary = "실종 글타래 전체 조회", description = "실종 글타래를 조회합니다.")
  @GetMapping("/public/lost")
  public ResponseEntity<List<ArticleResponse>> getLostArticles(
      @RequestParam(value = "lostStatus", required = false) CaseStatus lostStatus) {

    List<ArticleResponse> articles = articleService.findLostArticles(lostStatus);

    return ResponseEntity.ok(articles);
  }


  // 지도에 마커 표시 위한 경로
  // 위치 기준 지도 전체 글타래 조회 // 타입별 조회
  // map visible 만 보임
  @Operation(summary = "지도에서 글타래 조회", description = "지도에서 글타래를 조회합니다.")
  @PostMapping("/public/articlesMarkers")
  public ResponseEntity<List<MapBoundsResponse>> getMarkersInBounds(
      @RequestParam(value = "articleType", required = false) String articleType,
      @RequestParam(value = "placeCategory", required = false) String placeCategory,
      @RequestBody MapBoundsRequest bounds) {

    double southWestLat = bounds.getSouthWestLat();
    double southWestLon = bounds.getSouthWestLon();
    double northEastLat = bounds.getNorthEastLat();
    double northEastLon = bounds.getNorthEastLon();

    List<MapBoundsResponse> articlesInBounds;

    // 대분류 소분류
    // 대분류 null 인경우 전체 조회
    // 대분류 존재하는 경우 해당 값 조회 // 시설/위치 ---구조 ---목격 ---사용자 등록 정보
    // 시설/위치는 소분류도 존재
    // 사용자 등록 정보는?
    if (articleType == null || articleType.isEmpty()) { // 대분류 null 전체 조회
      articlesInBounds = locationService.findArticlesInBounds(
          southWestLat, southWestLon,
          northEastLat, northEastLon);
    } else { // 대준류 null 아님
      if (placeCategory == null || placeCategory.isEmpty()) { // 소분류 없는 경우 // 글타래 타입 기준 조회
        articlesInBounds = locationService.findArticlesInBoundsByType(
            southWestLat, southWestLon,
            northEastLat, northEastLon, articleType); // 실종 목격 구조 장소 사용자등록장소
      } else { // 시설 업체, 사용자 등록 => 장소 소분류 존재
        articlesInBounds = locationService.findPlaceArticlesInBoundsByCategory(
            southWestLat, southWestLon,
            northEastLat, northEastLon, articleType, placeCategory);
      }

    }
    return ResponseEntity.ok(articlesInBounds);
  }

  // 검색 조회
  // map visible 만 보임
  @Operation(summary = "글타래 검색", description = "글타래에서 검색합니다.")
  @GetMapping("/public/search")
  public ResponseEntity<List<ArticleResponse>> searchSightingPosts(
      @RequestParam String keyword) {

    List<ArticleResponse> articles = articleService.findArticlesByKeyword(keyword);
    return ResponseEntity.ok(articles);
  }


  // 회원 자신이 작성한 글타래 목록 조회 // list
  // is deleted false 안 보임
  @Operation(summary = "사용자가 작성한 전체 글타래 조회", description = "사용자가 자신이 작성한 글타래 목록을 조회합니다.")
  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @GetMapping("/myArticles")
  public ResponseEntity<List<ArticleResponse>> getArticlesByMemberId(
      @Parameter(description = "현재 인증된 사용자 정보", required = true)
      @AuthenticationPrincipal CustomMemberDetails principal) {

    List<ArticleResponse> articles = articleService.findArticlesByMemberId(principal.getMemberId());
    return ResponseEntity.ok(articles);
  }


  // 글타래 수정
  // 멤버 - 장소는 수정 불가
  // 관리자 전체 가능
  @Operation(summary = "글타래 수정", description = "글타래 Id 로 글타래를 수정합니다. 사용자는 자신이 작성한 글타래 장소가 아닌 글타래만 수정 가능합니다. 관리자는 전부 수정 가능합니다.")
  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @PutMapping("/{articleId}")
  public ResponseEntity<ArticleResponse> updateArticle(
      @PathVariable("articleId") Long articleId,
      @RequestBody ArticleCreateRequest articleCreateRequest,
      @Parameter(description = "현재 인증된 사용자 정보", required = true)
      @AuthenticationPrincipal CustomMemberDetails principal) {

    ArticleResponse articleResponse = articleService.updateArticle(
        articleId,
        articleCreateRequest,
        principal.getMemberId());
    return ResponseEntity.ok(articleResponse);
  }

  // 글타래 삭제

  // 논리 삭제
  // 멤버 - 장소는 삭제 불가
  // 관리자 전체 가능
  @Operation(summary = "글타래 논리 삭제", description = "글타래를 논리적으로 삭제합니다. 사용자는 자신이 작성한 글만 삭제할 수 있고, 관리자는 모든 글을 삭제할 수 있습니다.")
  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/{articleId}/deactivate")
  public ResponseEntity<Void> deactivateArticle(
      @PathVariable("articleId") Long articleId,
      @AuthenticationPrincipal CustomMemberDetails principal) {

    Long memberId = principal.getMemberId();
    articleService.deactivateArticle(articleId, memberId);
    return ResponseEntity.noContent().build();
  }

  // 물리 삭제
  // 관리자만 가능
  @Operation(summary = "글타래 물리 삭제", description = "관리자만 글타래를 물리적으로 삭제할 수 있습니다.")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @DeleteMapping("/{articleId}")
  public ResponseEntity<Void> deleteArticle(
      @PathVariable("articleId") Long articleId) {

    articleService.deleteArticle(articleId);
    return ResponseEntity.noContent().build();
  }


}
