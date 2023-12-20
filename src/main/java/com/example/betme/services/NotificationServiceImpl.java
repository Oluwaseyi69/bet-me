package com.example.betme.services;

import com.example.betme.data.model.Notification;
import com.example.betme.data.repository.NotificationRepository;
import com.example.betme.dtos.request.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private NotificationRepository notificationRepository;


    @Override
    public Notification notifyPlayer(NotificationRequest notificationRequest) {

        Notification notification = buildNotification(notificationRequest);
        return save(notification);
    }

    private static Notification buildNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        notification.setAmount(notificationRequest.getAmount());
        notification.setEvent(notificationRequest.getEvent());
        notification.setCaller(notificationRequest.getCaller());
        notification.setReceiver(notificationRequest.getReceivingPlayer());
        return notification;
    }

    private Notification save(Notification notification) {
       return notificationRepository.save(notification);
    }
}
