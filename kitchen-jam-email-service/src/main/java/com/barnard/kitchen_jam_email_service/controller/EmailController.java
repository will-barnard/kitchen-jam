package com.barnard.kitchen_jam_email_service.controller;

import com.barnard.kitchen_jam_email_service.model.EmailParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.barnard.kitchen_jam_email_service.service.EmailService;

@CrossOrigin
@RestController
@RequestMapping(path = "/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping(path = "/send")
    public void sendEmail(@RequestBody EmailParams email) {
        emailService.sendEmail(email.getTo(), email.getSubject(), email.getBody());
    }

}
