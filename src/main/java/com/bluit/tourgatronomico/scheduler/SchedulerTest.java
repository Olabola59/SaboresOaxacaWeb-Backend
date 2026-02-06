package com.bluit.tourgatronomico.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulerTest {

    @Scheduled(fixedRate = 5000) // cada 5 segundos
    public void test() {
        System.out.println("✅ SCHEDULER OK - " + LocalDateTime.now());
    }
}
