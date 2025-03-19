package com.barnard.controller;

import com.barnard.dao.FriendshipDao;
import com.barnard.dao.UserDao;
import com.barnard.model.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/friendship")
public class FriendshipController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private FriendshipDao friendshipDao;

    @GetMapping(path = "/friendlist")
    public List<Friend> getFriendList(Principal principal) {
        return friendshipDao.getFriendList(userDao.getUserByUsername(principal.getName()).getId());
    }

    @GetMapping(path = "/friendlist/{userId}")
    public List<Friend> getFriendList(@PathVariable int userId, Principal principal) {
        if (friendshipDao.isFriend(userDao.getUserByUsername(principal.getName()).getId(), userId)) {
            return friendshipDao.getFriendList(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not friends with this user");
        }
    }

    @GetMapping(path = "/pending")
    public List<Friend> getPendingRequests(Principal principal) {
        return friendshipDao.getPendingRequests(userDao.getUserByUsername(principal.getName()).getId());
    }

    @GetMapping(path = "/blocked")
    public List<Friend> getBlockedUsers(Principal principal) {
        return friendshipDao.getBlockedUsers(userDao.getUserByUsername(principal.getName()).getId());
    }

    @PostMapping(path = "/request/{friendId}")
    public void createFriendRequest(Principal principal, @PathVariable int friendId) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            friendshipDao.createFriendRequest(userId, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @PostMapping(path = "/accept/{friendId}")
    public void acceptFriendRequest(Principal principal, @PathVariable int friendId) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            friendshipDao.acceptFriendRequest(userId, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @GetMapping(path = "/requests")
    public List<Friend> getFriendRequests(Principal principal) {
        return friendshipDao.getFriendRequests(userDao.getUserByUsername(principal.getName()).getId());
    }

    @PostMapping(path = "/reject/{friendId}")
    public void rejectFriendRequest(Principal principal, @PathVariable int friendId) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            friendshipDao.rejectFriendRequest(userId, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @PostMapping(path = "/remove/{friendId}")
    public void removeFriend(Principal principal, @PathVariable int friendId) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            friendshipDao.removeFriend(userId, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @PostMapping(path = "/block/{friendId}")
    public void blockFriend(Principal principal, @PathVariable int friendId) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            friendshipDao.blockFriend(userId, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @PostMapping(path = "/unblock/{friendId}")
    public void unblockFriend(Principal principal, @PathVariable int friendId) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            friendshipDao.unblockFriend(userId, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @PostMapping(path = "/cancel/{friendId}")
    public void cancelFriendRequest(Principal principal, @PathVariable int friendId) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            friendshipDao.cancelFriendRequest(userId, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @GetMapping(path = "/isfriend/{friendId}")
    public boolean isFriend(Principal principal, @PathVariable int friendId) {
        return friendshipDao.isFriend(userDao.getUserByUsername(principal.getName()).getId(), friendId);
    }


}
