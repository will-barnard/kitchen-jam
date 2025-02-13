package com.barnard.controller;

import com.barnard.dao.SettingsDao;
import com.barnard.dao.UserDao;
import com.barnard.model.SettingsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/settings")
public class SettingsController {

    @Autowired
    private SettingsDao settingsDao;
    @Autowired
    private UserDao userDao;

    @PutMapping(path = "/isPublic")
    public void updateIsPublic(@RequestBody SettingsDto dto, Principal principal) {
        boolean isPublicBool = dto.isProfilePublic();
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (isPublicBool) {
                settingsDao.makeProfilePublic(userId);
            } else {
                settingsDao.makeProfilePrivate(userId);
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @PutMapping(path = "/defaultPublic")
    public void updateDefaultPublic(@RequestBody SettingsDto dto, Principal principal) {
        boolean defaultPublic = dto.isDefaultPublic();
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (defaultPublic) {
                settingsDao.makeDefaultPublic(userId);
            } else {
                settingsDao.makeDefaultPrivate(userId);
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

}
