package com.mallangs.domain.ai;

import com.mallangs.domain.ai.dto.SightAIResponse;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
public class OpenAiPromptController {

    @Value("${spring.ai.open.ai.secret.key}")
    private String SECRET_KEY;

    @GetMapping()
    @Operation(summary = "AI로 비슷한 목격게시물을 조회", description = "AI로 실종글타래와 비슷한 목격게시물을 조회합니다.")
    public ResponseEntity<String> getArticleByArticleIdByAI(){

        //ai client를 api key를 이용해 등록
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(SECRET_KEY)
                .build();

        //ai 대화형응답에 대한 param을 만들기
        String question = "Say this is a test";

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .messages(List.of(ChatCompletionMessageParam.ofChatCompletionUserMessageParam(ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.USER)
                        .content(ChatCompletionUserMessageParam.Content.ofTextContent(question))
                        .build())))
                .model(ChatModel.O1)
                .build();

        //응답 생성 - validate()는 자바 역직렬화 타입 확인 OpenAIInvalidDataException 방어코드
        ChatCompletion chatCompletion = client.chat().completions().create(params).validate();

        return ResponseEntity.ok(chatCompletion.toString());
    }
}
