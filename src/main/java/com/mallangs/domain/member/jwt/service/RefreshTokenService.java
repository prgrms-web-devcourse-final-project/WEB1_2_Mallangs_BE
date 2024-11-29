package com.mallangs.domain.member.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    protected static final String REFRESH_KEY = "RefreshToken";

    //레디스 RefreshToken 저장
    public void insertInRedis(Map<String, Object> payloadMap, String refreshToken) {

        try {
            if (refreshToken != null) {
                deleteRefreshTokenInRedis(payloadMap);
            }
            if (refreshToken == null) {
                log.error("refreshToken is null");
                throw new NoSuchElementException("refreshToken is null");
            }
            deleteRefreshTokenInRedis(payloadMap);
            redisTemplate.opsForHash().put(REFRESH_KEY, makeHashKey(payloadMap), refreshToken);
        } catch (Exception e) {
            log.error("redis failed to creat refreshToken :{}", e.getMessage());
        }
    }

    //레디스 RefreshToken 조회
    public String readRefreshTokenInRedis(Map<String, Object> payloadMap) {
        try {
            String refreshToken = (String) redisTemplate.opsForHash().get(REFRESH_KEY, makeHashKey(payloadMap));
            if (refreshToken == null) {
                log.warn("No refreshToken found for");
                throw new NoSuchElementException("No refresh token found");
            }
            return refreshToken;
        } catch (Exception e) {
            log.error("redis failed to read refreshToken :{}", e.getMessage());
        }
        return null;
    }

    //레디스 RefreshToken 삭제
    public void deleteRefreshTokenInRedis(Map<String, Object> payloadMap) {
        try {
            redisTemplate.opsForHash().delete(REFRESH_KEY, makeHashKey(payloadMap));
        } catch (Exception e) {
            log.error("redis failed to delete refreshToken :{}", e.getMessage());
        }
    }

    // 회원 데이터 마다 다른 키값 만들기
    public String makeHashKey(Map<String, Object> payloadMap) {
        Object userId = payloadMap.get("userId");
        Object randomUUID = payloadMap.get("randomUUID");
        return userId + ":" + randomUUID;
    }
}
