package com.barnard.dao;

import com.barnard.model.Friend;

import java.util.List;

public interface FriendshipDao {

    void createFriendRequest(int userId1, int userId2);
    void acceptFriendRequest(int userId1, int userId2);
    List<Friend> getFriendList(int userId);
    List<Friend> getFriendRequests(int userId);
    List<Friend> getBlockedUsers(int userId);
    void rejectFriendRequest(int userId1, int userId2);
    void removeFriend(int userId1, int userId2);
    void blockFriend(int userId1, int userId2);
    void unblockFriend(int userId1, int userId2);
    void cancelFriendRequest(int userId1, int userId2);
    boolean isFriend(int userId1, int userId2);

}
