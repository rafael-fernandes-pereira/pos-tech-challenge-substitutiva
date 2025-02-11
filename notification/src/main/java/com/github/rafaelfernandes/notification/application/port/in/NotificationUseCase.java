package com.github.rafaelfernandes.notification.application.port.in;

public interface NotificationUseCase {

    void sendPackageArrivedNotification(String deliveryId);

}
