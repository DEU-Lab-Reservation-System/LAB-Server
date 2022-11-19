package lab.reservation_server.schedule;

import lab.reservation_server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 매년 3월 1일에 member의 isAuth를 false로 초기화,
 * 매년 9월 1일에 member의 isAuth를 false로 초기화
 *
 * @Scheduled(cron = "* * * * * *")
 * 첫번째 부터 위치별 설정 값은
 * 초(0-59)
 * 분(0-59)
 * 시간(0-23)
 * 일(1-31)
 * 월(1-12)
 * 요일(0-7)
 *
 */
@RequiredArgsConstructor
@Component
public class MemberSchedule {

    private final MemberService memberService;

    // 매년 3월 1일에 member의 isAuth를 false로 초기화
    @Scheduled(cron = "0 0 0 1 3 *" , zone = "Asia/Seoul")
    public void updateMemberIsAuthFalse() {
        memberService.updateMemberIsAuthFalse();
    }

    // 매년 9월 1일에 member의 isAuth를 false로 초기화
    @Scheduled(cron = "0 0 0 1 9 *")
    public void updateMemberIsAuthFalse2() {
        memberService.updateMemberIsAuthFalse();
    }

}
