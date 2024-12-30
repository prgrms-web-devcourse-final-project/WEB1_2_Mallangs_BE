package com.mallangs.domain.ai;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.mallangs.domain.ai.dto.SightAIResponse;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.dto.response.LostResponse;
import com.mallangs.domain.article.service.ArticleService;
import com.mallangs.domain.board.dto.response.SightingListResponse;
import com.mallangs.domain.board.service.BoardService;
import com.mallangs.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIPromptController {

    //    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;
    private final ArticleService articleService;
    private final BoardService boardService;

    @Value("${spring.ai.vertex.ai.gemini.project-id}")
    private String projectId;

    @Value("${spring.ai.vertex.ai.gemini.location}")
    private String location;

    String modelName = "gemini-1.5-flash-001";

    // AI로 비슷한 목격게시물 조회
    // 관리자 전부 조회 가능
    // 회원 visible + 자신의 글 조회 가능
    // 비회원 mapVisible 만 조회 가능
    @Operation(summary = "AI로 비슷한 목격게시물을 조회", description = "AI로 실종글타래와 비슷한 목격게시물을 조회합니다.")
    @GetMapping("/{articleId}")
    public ResponseEntity<List<SightAIResponse>> getArticleByArticleIdByAI(
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

        //단전조회, 실종글타래 조회
        ArticleResponse articleResponse = articleService.getArticleById(articleId, memberRole,
                memberId);

        //질문 제작
        StringBuilder question = new StringBuilder();

        // JSON 형식에 대한 예시 설명
        question.append("실종 동물과 유사한 목격 정보를 여러 개 찾습니다. " +
                "반드시 JSON 배열 형식으로 응답해주세요. 예시는 다음과 같습니다:\n");

        question.append("[\n");
        question.append("  {\n");
        question.append("    \"sightArticleId\": 12345,\n");
        question.append("    \"percentage\": 85.52,\n");
        question.append("    \"findSpot\": \"서울특별시 강남구 역삼동\",\n");
        question.append("    \"sightedAt\": \"2024-12-24\",\n");
        question.append("    \"breed\": \"Labrador\",\n");
        question.append("    \"color\": \"Yellow\",\n");
        question.append("    \"gender\": \"Male\"\n");
        question.append("  },\n");
        question.append("  {\n");
        question.append("    \"sightArticleId\": 67890,\n");
        question.append("    \"percentage\": 93.3,\n");
        question.append("    \"findSpot\": \"서울특별시 서초구 서초동\",\n");
        question.append("    \"sightedAt\": \"2024-12-22\",\n");
        question.append("    \"breed\": \"Labrador\",\n");
        question.append("    \"color\": \"Yellow\",\n");
        question.append("    \"gender\": \"Male\"\n");
        question.append("  }\n");
        question.append("]\n\n");

        question.append("목격 정보는 반드시 `percentage` 내림차순으로 정렬해주세요. " +
                "그리고 50% 이상만 응답해 주세요.\n\n");

        // 실종 동물 정보
        LostResponse lostArticle = (LostResponse) articleResponse;
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

        question.append("\n위 정보를 바탕으로 JSON 배열을 만들어 주세요. " +
                "각 요소는 SightAIResponse 형식이며, 50% 미만은 제외하고 " +
                "percentage 내림차순으로 정렬해 주세요.\n");

        //AI에게 질문하기
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {
            // 1. Gemini에게 질문 (질문 -> 응답)
//            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);
//            GenerateContentResponse response = model.generateContent(question.toString());
            GenerateContentResponse response;
            GenerativeModel model = new GenerativeModel(modelName, vertexAi);
            // Create a chat session to be used for interactive conversation.

            String answer = ResponseHandler.getText(new ChatSession(model).sendMessage(question.toString()));
            log.info("answer {}",answer);

//            GenerateContentResponse response = model.generateContent(question.toString());
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

            // findSpot 디코딩 적용
//            for (SightAIResponse answer : answerList) {
//                String decodedSpot = decodeFindSpot(answer.getFindSpot());
//                answer.setFindSpot(decodedSpot);
//            }

            return ResponseEntity.ok(answerList);
        } catch (IllegalArgumentException e) {
            log.error("AI answer failed {}", e.getMessage());
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

                // 특수문자 UTF-8 디코딩
//                return new String(jsonData.getBytes("ISO-8859-1"), "UTF-8");
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
    // findSpot 디코딩
    public String decodeFindSpot(String encodedSpot) {
        try {
            // UTF-8 디코딩
            byte[] bytes = encodedSpot.getBytes(StandardCharsets.ISO_8859_1);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // 디코딩 실패 시 원본 반환
            return encodedSpot;
        }
    }


//    // AI로 비슷한 목격게시물 조회
//    // 관리자 전부 조회 가능
//    // 회원 visible + 자신의 글 조회 가능
//    // 비회원 mapVisible 만 조회 가능
//    @Operation(summary = "AI로 비슷한 목격게시물을 조회", description = "AI로 실종글타래와 비슷한 목격게시물을 조회합니다.")
//    @GetMapping("/{articleId}")
//    public ResponseEntity<List<SightAIResponse>> getArticleByArticleIdByAI(
//            @Parameter(description = "조회할 글타래 ID", required = true) @PathVariable Long articleId) {
//
//        String memberRole;
//        Long memberId;
//
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof Member member) {
//            memberRole = member.getMemberRole().name();
//            memberId = member.getMemberId();
//        } else {
//            memberRole = "ROLE_GUEST";
//            memberId = -1L;
//        }
//        log.info("role: {} memberId: {}", memberRole, memberId);
//
//        //단전조회, 실종글타래 조회
//        ArticleResponse articleResponse = articleService.getArticleById(articleId, memberRole,
//                memberId);
//
//        //질문 제작
//        StringBuilder question = new StringBuilder();
//
//        // JSON 형식에 대한 예시 설명
//        question.append("실종 동물과 유사한 목격 정보를 여러 개 찾습니다. " +
//                "반드시 JSON 배열 형식으로 응답해주세요. 예시는 다음과 같습니다:\n");
//
//        question.append("[\n");
//        question.append("  {\n");
//        question.append("    \"sightArticleId\": 12345,\n");
//        question.append("    \"percentage\": 85.52,\n");
//        question.append("    \"findSpot\": \"서울특별시 강남구 역삼동\",\n");
//        question.append("    \"sightedAt\": \"2024-12-24\",\n");
//        question.append("    \"breed\": \"Labrador\",\n");
//        question.append("    \"color\": \"Yellow\",\n");
//        question.append("    \"gender\": \"Male\"\n");
//        question.append("  },\n");
//        question.append("  {\n");
//        question.append("    \"sightArticleId\": 67890,\n");
//        question.append("    \"percentage\": 93.3,\n");
//        question.append("    \"findSpot\": \"서울특별시 서초구 서초동\",\n");
//        question.append("    \"sightedAt\": \"2024-12-22\",\n");
//        question.append("    \"breed\": \"Labrador\",\n");
//        question.append("    \"color\": \"Yellow\",\n");
//        question.append("    \"gender\": \"Male\"\n");
//        question.append("  }\n");
//        question.append("]\n\n");
//
//        question.append("목격 정보는 반드시 `percentage` 내림차순으로 정렬해주세요. " +
//                "그리고 50% 이상만 응답해 주세요.\n\n");
//
//        // 실종 동물 정보
//        LostResponse lostArticle = (LostResponse) articleResponse;
//        question.append("실종동물에 대한 설명 : lostArticle.getDescription()");
//        question.append("실종 위치: 경도:" + lostArticle.getLongitude() + ", 위도:" + lostArticle.getLatitude());
//        question.append("실종 동물 종: " + lostArticle.getBreed());
//        question.append("실종 동물 색상: " + lostArticle.getPetColor());
//        question.append("실종 동물 성별: " + lostArticle.getPetGender());
//        question.append("실종 동물 chipNumber: " + lostArticle.getChipNumber());
//        question.append("실종동물 실종된 위치: " + lostArticle.getLostLocation());
//        question.append("실종동물 실종일: " + lostArticle.getLostDate());
//
//        // 여러 목격 게시글 정보 추가
//        List<SightingListResponse> sightingList = boardService.getAllSightingBoardToList();
//
//        for (SightingListResponse sightingListResponse : sightingList) {
//            question.append("목격제보 ID: " + sightingListResponse.getBoardId());
//            question.append(", 목격동물 특징: " + sightingListResponse.getContent());
//            question.append(", 목격일: " + sightingListResponse.getSightedAt());
//            question.append(", 목격위치" + sightingListResponse.getAddress());
//        }
//
//        question.append("\n위 정보를 바탕으로 JSON 배열을 만들어 주세요. " +
//                "각 요소는 SightAIResponse 형식이며, 50% 미만은 제외하고 " +
//                "percentage 내림차순으로 정렬해 주세요.\n");
//
//        //AI에게 질문하기
//        try {
//            // 1. Gemini에게 질문 (질문 -> 응답)
//            String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(question.toString());
//
//            // 응답에서 백틱 및 불필요한 텍스트 제거
//            String cleanedResponse = cleanJsonResponse(vertexAiGeminiResponse);
//
//            // json 형식인지 확인
//            if (!cleanedResponse.trim().startsWith("[")) {
//                log.error("Invalid response format: {}", cleanedResponse);
//                throw new IllegalArgumentException("AI 응답이 JSON 형식이 아닙니다.");
//            }
//
//            // 2. JSON 데이터를 DTO로 매핑 <List>버전
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule()); //jackson 날짜추가
//
//            JavaType listType = objectMapper.getTypeFactory()
//                    .constructCollectionType(List.class, SightAIResponse.class);
//            List<SightAIResponse> answerList = objectMapper.readValue(cleanedResponse, listType);
//
//            return ResponseEntity.ok(answerList);
//        } catch (IllegalArgumentException e) {
//            log.error("AI answer failed {}", e.getMessage());
//            throw new RuntimeException("AI 응답이 유효하지 않습니다.");
//        } catch (Exception e) {
//            log.error("Error while processing AI response: {}", e.getMessage());
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AI 응답 처리 중 문제가 발생했습니다.", e);
//        }
//    }

}

