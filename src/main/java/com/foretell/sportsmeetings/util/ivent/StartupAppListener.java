package com.foretell.sportsmeetings.util.ivent;

import com.foretell.sportsmeetings.model.Meeting;
import com.foretell.sportsmeetings.model.MeetingStatus;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.util.timer.CustomTimer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TimerTask;

@Component
@Slf4j
public class StartupAppListener implements InitializingBean {

    private final CustomTimer customTimer;
    private final MeetingService meetingService;

    public StartupAppListener(CustomTimer customTimer, MeetingService meetingService) {
        this.customTimer = customTimer;
        this.meetingService = meetingService;
    }


    public void scheduleTasksInCustomTimer() {
        List<Meeting> allExpiredMeetings = meetingService.findAllExpiredMeetings();
        allExpiredMeetings.forEach(meeting -> customTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Long meetingId = meeting.getId();
                        if (meetingService.updateStatus(meetingId, MeetingStatus.FINISHED)) {
                            log.info("Status of meeting with id: " + (meetingId) + " set to FINISHED");
                        } else {
                            log.error("Cannot update status of meeting with id: " + (meetingId));
                        }
                    }
                },
                meeting.getEndDate().getTime()
        ));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        scheduleTasksInCustomTimer();
    }
}
