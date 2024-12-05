package com.mallangs.global.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@Component
@RequiredArgsConstructor
public class SseEmitters {
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter create(String sseId, SseEmitter emitter) {
        emitters.put(sseId, emitter);
        return emitter;
    }

    public SseEmitter findSingleEmitter(String sseId) {
        return emitters.get(sseId);
    }

    public Map<String, SseEmitter> findEmitter(String sseId) {
        return emitters.entrySet().stream().filter(entry -> entry.getKey().startsWith(sseId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void delete(String sseId) {
        emitters.remove(sseId);
    }
}