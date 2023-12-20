package com.example.betme.services;

import com.example.betme.data.model.Notification;
import com.example.betme.dtos.request.NotificationRequest;

public interface NotificationService {

    Notification notifyPlayer(NotificationRequest notificationRequest);
}
