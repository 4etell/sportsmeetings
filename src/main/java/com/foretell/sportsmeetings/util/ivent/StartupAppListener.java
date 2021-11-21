package com.foretell.sportsmeetings.util.ivent;

import com.foretell.sportsmeetings.util.scheduler.CustomScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartupAppListener implements InitializingBean {

    private final CustomScheduler customScheduler;

    public StartupAppListener(CustomScheduler customScheduler) {
        this.customScheduler = customScheduler;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        customScheduler.scheduleAllMeetingsFinishedStatusTask();
    }
}
