package com.barnard.controller;

import javax.validation.Valid;

import com.barnard.exception.DaoException;
import com.barnard.model.*;
import com.barnard.service.EmailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.barnard.dao.UserDao;
import com.barnard.security.jwt.JWTFilter;
import com.barnard.security.jwt.TokenProvider;

import java.security.Principal;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;
    private EmailService emailService;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao, EmailService emailService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.emailService = emailService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

        loginDto.setUsername(userDao.getUsernameByEmail(loginDto));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        User user;
        try {
            user = userDao.getUserByUsername(loginDto.getUsername());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect.");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new LoginResponseDto(jwt, user), httpHeaders, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public void register(@Valid @RequestBody RegisterUserDto newUser) {
        newUser.setRole("user");
        try {
            User user = userDao.createUser(newUser);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed.");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed.");
        }
    }

    @GetMapping(path = "/requestPasswordUpdate")
    public void requestPasswordUpdate(Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());
        String email;
        String token;
        try {

            email = userDao.getUserEmail(user.getId());
            token = userDao.createResetPasswordLink(user.getId());

        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve email.");

        }
        EmailParams emailParams = new EmailParams(email, "Password Reset", "Click the link to reset your password: http://kitchen-jam.com/resetPassword?token=" + token);
        emailService.sendEmail(emailParams);
    }

    @PostMapping(path = "/requestPasswordReset")
    public void requestPasswordReset(@RequestBody String email) {
        String token;
        try {

            int userId = userDao.getUserIdByEmail(email);
            token = userDao.createResetPasswordLink(userId);

        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve email.");

        }
        EmailParams emailParams = new EmailParams(email, "Password Reset", "Click the link to reset your password: http://kitchen-jam.com/resetPassword?token=" + token);
        emailService.sendEmail(emailParams);
    }

    @PutMapping(path = "/updatePassword")
    public void updatePassword(@RequestBody UpdatePasswordDto passwordUpdateDto) {
        try {
            int userId = userDao.getUserIdByUuid(passwordUpdateDto.getUuid());
            userDao.updatePassword(userId, passwordUpdateDto.getPassword());
            userDao.clearPasswordResetToken(userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not update password.");
        }
    }

}