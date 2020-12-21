package ink.verge.checkin.run;

import ink.verge.checkin.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResetCheckinStatus {
    private UserServiceImpl userService;

    @Autowired
    public ResetCheckinStatus(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * 重置签到状态为false
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void resetCheckinStatusToFalse(){
        log.info("开始重置签到状态");

        userService.resetCheckinStatus();
    }
}
