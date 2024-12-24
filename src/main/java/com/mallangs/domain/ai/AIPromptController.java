package com.mallangs.domain.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallangs.domain.ai.dto.AiResponseDTO;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.service.ArticleService;
import com.mallangs.domain.board.dto.response.SightingListResponse;
import com.mallangs.domain.board.service.BoardService;
import com.mallangs.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIPromptController {
    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;
    private final ArticleService articleService;
    private final BoardService boardService;

    @GetMapping("/chat")
    public ResponseEntity<AiResponseDTO> chat() {
        try {
            //질문내용
            String message = "{ \"response\": \"너는 누구야?\", \"details\": \"json 형식으로 답변해줘\" }";

            // 1. Gemini에게 질문 (질문 -> 응답)
            String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(message);

            // 응답에서 백틱 및 불필요한 텍스트 제거
            String cleanedResponse = cleanJsonResponse(vertexAiGeminiResponse);

            // json 형식인지 확인
            if (!cleanedResponse.trim().startsWith("{")) {
                log.error("Invalid response format: {}", cleanedResponse);
                throw new IllegalArgumentException("AI 응답이 JSON 형식이 아닙니다.");
            }

            // 2. JSON 데이터를 DTO로 매핑
            ObjectMapper objectMapper = new ObjectMapper();
            AiResponseDTO responseDto = objectMapper.readValue(cleanedResponse, AiResponseDTO.class);

            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            log.error("AI answer failed {}",e.getMessage());
            throw new RuntimeException("AI 응답이 유효하지 않습니다.");
        } catch (Exception e) {
            log.error("Error while processing AI response: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AI 응답 처리 중 문제가 발생했습니다.", e);
        }
    }
    /**
     * AI 응답에서 백틱 및 불필요한 텍스트 제거
     */
    private String cleanJsonResponse(String response) {
        // 백틱과 "```json" 제거
        String cleaned = response.replaceAll("```json", "").replaceAll("```", "").trim();
        log.debug("Cleaned response: {}", cleaned);
        return cleaned;
    }

    // AI로 비슷한 목격게시물 조회
    // 관리자 전부 조회 가능
    // 회원 visible + 자신의 글 조회 가능
    // 비회원 mapVisible 만 조회 가능
    @Operation(summary = "AI로 비슷한 목격게시물을 조회", description = "AI로 실종글타래와 비슷한 목격게시물을 조회합니다.")
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> getArticleByArticleIdByAI(
            @Parameter(description = "조회할 글타래 ID", required = true) @PathVariable Long articleId) {

        String memberRole;
        Long memberId;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Member member) {
            memberRole = member.getMemberRole().name();
            memberId = member.getMemberId();
        } else {
            memberRole = "ROLE_GUEST";
            memberId = -1L;
        }
        log.info("role: {} memberId: {}", memberRole, memberId);

        //단견조회, 실종글타래 조회
        ArticleResponse articleResponse = articleService.getArticleById(articleId, memberRole,
                memberId);

        //목격제보 게시글 전체조회
        List<SightingListResponse> sightingList = boardService.getAllSightingBoardToList();

        return ResponseEntity.ok(articleResponse);
    }

}
