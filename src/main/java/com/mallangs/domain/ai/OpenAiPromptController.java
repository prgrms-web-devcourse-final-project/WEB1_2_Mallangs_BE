package com.mallangs.domain.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
public class OpenAiPromptController {

    private final ChatClient chatClient;;

    OpenAiPromptController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping()
    public String call (@RequestBody String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}
