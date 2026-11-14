package com.fsilva.roomreservation.infra.scheduler;

import com.fsilva.roomreservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationReminderScheduler {

    private final ReservationService reservationService;

    @Scheduled(fixedDelayString = "${app.reminder.fixed-delay-ms:60000}")
    public void sendDueReminders() {
        reservationService.sendDueReminders();
    }
}
