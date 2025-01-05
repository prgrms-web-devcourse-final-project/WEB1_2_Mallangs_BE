package com.mallangs.domain.ai;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mallangs.domain.ai.dto.SightAIResponse;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.dto.response.LostResponse;
import com.mallangs.domain.article.service.ArticleService;
import com.mallangs.domain.board.dto.response.SightingListResponse;
import com.mallangs.domain.board.service.BoardService;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/ai")
public class OpenAiPromptController {

    private final ChatClient chatClient;
    private final ArticleService articleService;
    private final BoardService boardService;

    OpenAiPromptController(ChatClient.Builder builder, ArticleService articleService, BoardService boardService) {
        this.chatClient = builder.build();
        this.articleService = articleService;
        this.boardService = boardService;
    }

    // AI로 비슷한 목격게시물 조회
    // 관리자 전부 조회 가능
    // 회원 visible + 자신의 글 조회 가능
    // 비회원 mapVisible 만 조회 가능
    @Operation(summary = "AI로 비슷한 목격게시물을 조회", description = "AI로 실종글타래와 비슷한 목격게시물을 조회합니다.")
    @GetMapping("/{articleId}")
    public ResponseEntity<List<SightAIResponse>> getArticleByArticleIdByAI(
            @Parameter(description = "조회할 글타래 ID", required = true) @PathVariable Long articleId) {

        //단전조회, 실종글타래 조회
        ArticleResponse articleResponse = articleService.getLostArticleById(articleId);
        log.info("articleResponse는 {}", articleResponse);

        //lostArticle 확인
        LostResponse lostArticle;
        if (articleResponse instanceof LostResponse) {
            // LostResponse 처리 로직
            lostArticle = (LostResponse) articleResponse;
        } else {
            log.error("lostArticle이 아닙니다. {}", articleResponse);
            throw new MallangsCustomException(ErrorCode.WRONG_ARTICLE);
        }

        //질문제작
        StringBuilder question = new StringBuilder();

        // JSON 형식에 대한 예시 설명
        question.append("실종 동물과 유사한 목격 정보를 여러 개 찾습니다. " +
                "반드시 JSON 배열 형식으로 응답해주세요. 예시는 다음과 같습니다:\n");

        question.append("다음은 단순히 JSON 응답 형식의 예제입니다. 반드시 실제 데이터를 사용하여 응답을 생성해 주세요:\n");
        question.append("[예제 시작]\n");
        question.append("[\n");
        question.append("  {\n");
        question.append("    \"sightArticleId\": 12345,\n");
        question.append("    \"percentage\": 85.52,\n");
        question.append("    \"findSpot\": \"서울특별시 강남구 역삼동\",\n");
        question.append("    \"sightedAt\": \"2024-12-24\",\n");
        question.append("    \"breed\": \"Labrador\",\n");
        question.append("    \"color\": \"Yellow\",\n");
        question.append("    \"gender\": \"Male\"\n");
        question.append("  }\n");
        question.append("]\n");
        question.append("[예제 끝]\n");

        question.append("목격 정보는 반드시 `percentage` 내림차순으로 정렬해주세요. " +
                "그리고 50% 이상만 응답해 주세요.\n\n");

        // 실종 동물 정보
        question.append("실종동물에 대한 설명 : lostArticle.getDescription()");
        question.append("실종 위치: 경도:" + lostArticle.getLongitude() + ", 위도:" + lostArticle.getLatitude());
        question.append("실종 동물 종: " + lostArticle.getBreed());
        question.append("실종 동물 색상: " + lostArticle.getPetColor());
        question.append("실종 동물 성별: " + lostArticle.getPetGender());
        question.append("실종 동물 chipNumber: " + lostArticle.getChipNumber());
        question.append("실종동물 실종된 위치: " + lostArticle.getLostLocation());
        question.append("실종동물 실종일: " + lostArticle.getLostDate());

        // 여러 목격 게시글 정보 추가
        List<SightingListResponse> sightingList = boardService.getAllSightingBoardToList();

        for (SightingListResponse sightingListResponse : sightingList) {
            question.append("목격제보 ID: " + sightingListResponse.getBoardId());
            question.append(", 목격동물 특징: " + sightingListResponse.getContent());
            question.append(", 목격일: " + sightingListResponse.getSightedAt());
            question.append(", 목격위치" + sightingListResponse.getAddress());
        }

        question.append("\n위 정보를 바탕으로 JSON 배열을 만들어 주세요. 그리고 50% 이상만 응답해 주세요 " +
                "각 요소는 SightAIResponse 형식이며, 상위 5개만 찾아주세요. " +
                "percentage 내림차순으로 정렬해 주세요. 완전히 동일한 내용의 목격제보가 있다면 percentage는 동일하게 해주세요.\n");

        //AI에게 질문하기
        try {
            String answer = chatClient.prompt()
                    .user(question.toString())
                    .call()
                    .content();

            // 응답에서 백틱 및 불필요한 텍스트 제거
            String cleanedResponse = cleanJsonResponse(answer);
            log.info("cleanedResponse {}",cleanedResponse);

            // json 형식인지 확인
            if (!cleanedResponse.trim().startsWith("[")) {
                log.error("Invalid response format: {}", cleanedResponse);
                throw new IllegalArgumentException("AI 응답이 JSON 형식이 아닙니다.");
            }

            // 2. JSON 데이터를 DTO로 매핑 <List>버전
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); //jackson 날짜추가

            JavaType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, SightAIResponse.class);
            List<SightAIResponse> answerList = objectMapper.readValue(cleanedResponse, listType);

            return ResponseEntity.ok(answerList);
        } catch (
                IllegalArgumentException e) {
            log.error("AI answer failed {}", e.getMessage());
            throw new RuntimeException("AI 응답이 유효하지 않습니다.");
        } catch (
                Exception e) {
            log.error("Error while processing AI response: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AI 응답 처리 중 문제가 발생했습니다.", e);
        }
    }

    /**
     * AI 응답에서 백틱 및 불필요한 텍스트 제거
     */
    private String cleanJsonResponse(String response) {
        try {
            String cleaned = response.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .replaceAll("\\\\n", "") // 개행 문자 제거
                    .replaceAll("\\\\", "") // 특수문자 제거
                    .replaceAll("\\s+", " ") // 불필요한 공백 제거
                    .trim();

            int startIndex = cleaned.indexOf('[');
            int endIndex = cleaned.lastIndexOf(']');
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                String jsonData = cleaned.substring(startIndex, endIndex + 1).trim();

                return jsonData;
            } else {
                log.error("JSON 데이터가 포함되어 있지 않습니다: {}", cleaned);
                throw new IllegalArgumentException("올바른 JSON 데이터를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("JSON 추출 및 정리 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("AI 응답 처리 중 문제가 발생했습니다.");
        }
    }

}
