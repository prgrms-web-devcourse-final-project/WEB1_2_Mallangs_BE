package com.mallangs.global.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.util.OS;

import java.io.IOException;
import java.net.Socket;

@Log4j2
@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedisServer() {
        if (!isRedisRunning()) {
            // Redis가 실행 중이 아니면 임베디드 Redis 서버 시작
            redisServer = new RedisServer(redisPort);
            redisServer.start();
            System.out.println("임베디드 Redis 서버가 시작되었습니다.");
        } else {
            System.out.println("Redis가 이미 실행 중입니다.");
        }
    }

    @PreDestroy
    public void stopRedisServer() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
            System.out.println("임베디드 Redis 서버가 중지되었습니다.");
        }
    }

    // Redis 서버가 실행 중인지 확인하는 메서드
    private boolean isRedisRunning() {
        try (Socket socket = new Socket("localhost", redisPort)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
