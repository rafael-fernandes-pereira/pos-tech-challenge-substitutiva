package com.github.rafaelfernandes.notification.adapter.out.sms;

import com.github.rafaelfernandes.notification.application.port.out.NotificationPort;
import com.github.rafaelfernandes.notification.common.annotations.SmsAdapter;
import lombok.extern.slf4j.Slf4j;

@SmsAdapter
@Slf4j
public class NotificationSms implements NotificationPort {
    @Override
    public void notifyPackge(String cellphone, String message) {
        log.info("Sending SMS to cellphone: {} with message: {}", cellphone, message);
    }
}
