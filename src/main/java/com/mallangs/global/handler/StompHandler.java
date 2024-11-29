package com.mallangs.global.handler;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import com.mallangs.global.jwt.util.JWTUtil;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        //websocket 연결시
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            if (!jwtUtil.isExpired(jwtToken)){
                Map<String, Object> payload = jwtUtil.validateToken(jwtToken);
                String userId = (String)payload.get("userId");
                Member foundMember = memberRepository.findByUserId(new UserId(userId))
                        .orElseThrow(()->new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
                CustomMemberDetails customUserDetails = new CustomMemberDetails(foundMember);

                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                        customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                Object payload1 = message.getPayload();
                log.info(" {}",payload1.toString());
            }
        }
        return message;
    }
}

