package com.barnard.controller;

import com.barnard.dao.NotificationDao;
import com.barnard.dao.UserDao;
import com.barnard.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/notification")
public class NotificationController {

    @Autowired
    private NotificationDao notificationDao;
    @Autowired
    private UserDao userDao;

    @GetMapping
    public List<Notification> getNotifications(Principal principal) {
        return notificationDao.getNotifications(userDao.getUserByUsername(principal.getName()).getId());
    }

    @GetMapping(path = "/old")
    public List<Notification> getOldNotifications(Principal principal) {
        return notificationDao.getOldNotifications(userDao.getUserByUsername(principal.getName()).getId());
    }

    @PutMapping(path = "/{notificationId}")
    public void markNotificationAsRead(@PathVariable int notificationId, Principal principal) {
        notificationDao.markNotificationAsRead(notificationId);
    }

    @DeleteMapping(path = "/{notificationId}")
    public void deleteNotification(@PathVariable int notificationId, Principal principal) {
        try {
            Notification notification = notificationDao.getNotification(notificationId);
            if (notification.getUserId() != userDao.getUserByUsername(principal.getName()).getId()) {
                throw new RuntimeException("You do not have permission to delete this notification");
            } else {
                notificationDao.deleteNotification(notificationId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete notification", e);
        }
    }

}
