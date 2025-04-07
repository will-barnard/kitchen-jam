package com.barnard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.barnard.dao.UserDao;

@Service
public class PasswordResetCleanupService {

    @Autowired
    private UserDao userDao;

    @Scheduled(cron = "0 0 0 * * ?") // Runs once a day at midnight
    public void cleanUpOldPasswordResets() {
        userDao.deleteOldPasswordResets();
    }
}
