package com.mallangs.global.schedule;

import com.mallangs.domain.member.service.MemberAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomTaskScheduler {

    private final MemberAdminService memberAdminService;

    //매일 24시 - 회원 차단기간 지날 시, 회원 활성화
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleTaskActiveMembers() {
        memberAdminService.activeMembers();
    }
}
