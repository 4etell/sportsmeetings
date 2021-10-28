package com.foretell.sportsmeetings.util.ivent;

import com.foretell.sportsmeetings.model.Meeting;
import com.foretell.sportsmeetings.model.MeetingStatus;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.util.telegrambot.TelegramBot;
import com.foretell.sportsmeetings.util.timer.CustomTimer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimerTask;

@Component
@Slf4j
public class StartupAppListener implements InitializingBean {

    private final CustomTimer customTimer;
    private final MeetingService meetingService;
    private final TelegramBot telegramBot;

    public StartupAppListener(CustomTimer customTimer, MeetingService meetingService, TelegramBot telegramBot) {
        this.customTimer = customTimer;
        this.meetingService = meetingService;
        this.telegramBot = telegramBot;
    }


    public void scheduleMeetingFinishedStatusTasksInCustomTimer() {
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

    public void scheduleTelegramBotStartMeetingNotification() {
        List<Meeting> meetings = meetingService.findAllMeetingsWhichNotStarted();
        meetings.forEach(meeting -> {
            GregorianCalendar startDate = meeting.getStartDate();
            long timeUntilEndDate = 10800000;
            Date date = new Date();
            date.setTime(startDate.getTimeInMillis() - timeUntilEndDate);
            meeting.getParticipants().forEach(user -> {
                Long telegramBotChatId = user.getTelegramBotChatId();
                if (telegramBotChatId != null) {
                    customTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            telegramBot.sendStartMeetingNotification(
                                    telegramBotChatId,
                                    new SimpleDateFormat("dd-M-yyyy hh:mm")
                                            .format(startDate.getTime()));
                        }
                    }, date);
                }

            });
        });
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        scheduleMeetingFinishedStatusTasksInCustomTimer();
        scheduleTelegramBotStartMeetingNotification();
    }
}
