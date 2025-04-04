package com.barnard.dao;

import com.barnard.model.Notification;

import java.util.List;

public interface NotificationDao {

    Notification getNotification(int notificationId);
    List<Notification> getNotifications(int userId);
    List<Notification> getOldNotifications(int userId);
    Notification createNotification(Notification notification);
    void markNotificationAsRead(int notificationId);
    void markAllNotificationsAsRead(int userId);
    void deleteNotification(int notificationId);

}
