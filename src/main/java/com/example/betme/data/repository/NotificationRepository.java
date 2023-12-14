package com.example.betme.data.repository;

import com.example.betme.data.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository <Notification, String> {

}
