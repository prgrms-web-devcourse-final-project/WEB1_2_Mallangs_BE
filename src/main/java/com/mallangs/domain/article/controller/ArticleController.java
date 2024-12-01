package com.mallangs.domain.article.controller;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.request.MapBoundsRequest;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.dto.response.MapBoundsResponse;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.service.ArticleService;
import com.mallangs.domain.article.service.LocationService;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Article Controller", description = "글타래 API")
public class ArticleController {

  private final ArticleService articleService;
  private final LocationService locationService;

  @Operation(summary = "글타래 등록", description = "새로운 글타래를 등록합니다.")
  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @PostMapping
  public ResponseEntity<ArticleResponse> createArticle(
      @RequestBody ArticleCreateRequest articleCreateRequest,
      @Parameter(description = "현재 인증된 사용자 정보", required = true)
      @AuthenticationPrincipal CustomMemberDetails principal) {
    Long memberId = principal.getMemberId();
    ArticleResponse articleResponse = articleService.createArticle(articleCreateRequest,
        memberId);

    return ResponseEntity.ok(articleResponse);
  }


  @Operation(summary = "글타래 단건 조회", description = "글타래를 단건 조회합니다.")
  @GetMapping("/public/{articleId}")
  public ResponseEntity<ArticleResponse> getArticleByArticleId(
      @Parameter(description = "조회할 글타래 ID", required = true) @PathVariable Long articleId) {
    ArticleResponse articleResponse = articleService.getArticleById(articleId);

    return ResponseEntity.ok(articleResponse);
  }

  @Operation(summary = "글타래 전체 조회", description = "모든 글타래를 페이지별로 조회합니다.")
  @GetMapping("/public")
  public ResponseEntity<Page<ArticleResponse>> getArticles(
      @Parameter(description = "페이징 요청 정보", required = true) Pageable pageable) {
    Page<ArticleResponse> articles = articleService.findAllTypeArticles(pageable);

    return ResponseEntity.ok(articles);
  }

  // 글타래 타입 별 조회
  @Operation(summary = "글타래 타입별 전체 조회", description = "타입의 글타래를 페이지별로 조회합니다.")
  @GetMapping("/public/type/{articleType}")
  public ResponseEntity<List<ArticleResponse>> getArticlesByType(
      @Parameter(description = "조회할 글타래 Type", required = true) @PathVariable String articleType) {
    List<ArticleResponse> articles = articleService.findArticlesByArticleType(articleType);

    return ResponseEntity.ok(articles);
  }

  // 위치 기준 지도 조회
  @Operation(summary = "지도에서 글타래 조회", description = "지도에서 글타래를 조회합니다.")
  @PostMapping("/public/articlesMarkers")
  public ResponseEntity<List<MapBoundsResponse>> getMarkersInBounds(
      @RequestBody MapBoundsRequest bounds) {

    double northEastLat = bounds.getNorthEastLat();
    double northEastLon = bounds.getNorthEastLon();
    double southWestLat = bounds.getSouthWestLat();
    double southWestLon = bounds.getSouthWestLon();

    List<Article> articles = locationService.findArticlesInBounds(northEastLat, northEastLon,
        southWestLat, southWestLon);
    List<MapBoundsResponse> articlesInBounds = articles.stream()
        .map(MapBoundsResponse::new)
        .collect(Collectors.toList());

    return ResponseEntity.ok(articlesInBounds);
  }

  // 회원 자신이 작성한 글타래 목록 조회
  @Operation(summary = "사용자가 작성한 전체 글타래 조회", description = "사용자가 자신이 작성한 글타래 목록을 조회합니다.")
  @PreAuthorize("hasAuthority('ROLE_USER')")
  @GetMapping("/myArticles")
  public ResponseEntity<Page<ArticleResponse>> getArticlesByMemberId(
      @Parameter(description = "페이지 요청 정보", required = true) Pageable pageable,
      @Parameter(description = "현재 인증된 사용자 정보", required = true)
      @AuthenticationPrincipal CustomMemberDetails principal) {
    Page<ArticleResponse> articles = articleService.findArticlesByMemberId(pageable,
        principal.getMemberId());

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
      @AuthenticationPrincipal CustomMemberDetails principal
  ) {
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
