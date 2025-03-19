package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Friend;
import com.barnard.model.Friendship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcFriendshipDao implements FriendshipDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Friend> getFriendRequests(int userId) {
        List<Friend> friendRequests = new ArrayList<>();
        List<Friend> result = null;
        String sql = "SELECT * FROM friendships " +
                "WHERE user_id2 = ? AND status = 'pending';";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()) {
                friendRequests.add(mapRowToFriend(rowSet, userId));
            }
            result = getFriendUsernames(friendRequests);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return result;
    }

    @Override
    public List<Friend> getPendingRequests(int userId) {
        List<Friend> pendingRequests = new ArrayList<>();
        List<Friend> result = null;
        String sql = "SELECT * FROM friendships " +
                "WHERE user_id1 = ? AND status = 'pending';";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()) {
                pendingRequests.add(mapRowToFriend(rowSet, userId));
            }
            result = getFriendUsernames(pendingRequests);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return result;
    }

    @Override
    public void createFriendRequest(int userId1, int userId2) {
        String sql = "INSERT INTO friendships (user_id1, user_id2, status, created_at) " +
                "VALUES (?, ?, 'pending', NOW())";
        try {
            jdbcTemplate.update(sql, userId1, userId2);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void acceptFriendRequest(int userId1, int userId2) {
        String sql = "UPDATE friendships " +
            "SET status = 'accepted'" +
            "WHERE user_id1 = ? AND user_id2 = ? AND status = 'pending';";
        try {
            jdbcTemplate.update(sql, userId1, userId2);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void rejectFriendRequest(int userId1, int userId2) {
        String sql = "DELETE from friendships " +
                "WHERE user_id1 = ? AND user_id2 = ? AND status = 'pending';";
        try {
            jdbcTemplate.update(sql, userId1, userId2);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void removeFriend(int userId1, int userId2) {
        String sql = "DELETE FROM friendships " +
                "WHERE (user_id1 = ? AND user_id2 = ?) OR (user_id1 = ? AND user_id2 = ?);";
        try {
            jdbcTemplate.update(sql, userId1, userId2, userId2, userId1);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void blockFriend(int userId1, int userId2) {
        String sql = "UPDATE friendships " +
                "SET status = 'blocked'" +
                "WHERE (user_id1 = ? AND user_id2 = ?) OR (user_id1 = ? AND user_id2 = ?);";
        try {
            jdbcTemplate.update(sql, userId1, userId2, userId2, userId1);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void unblockFriend(int userId1, int userId2) {
        String sql = "UPDATE friendships " +
                "SET status = 'accepted'" +
                "WHERE (user_id1 = ? AND user_id2 = ?) OR (user_id1 = ? AND user_id2 = ?);";
        try {
            jdbcTemplate.update(sql, userId1, userId2, userId2, userId1);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<Friend> getBlockedUsers(int userId) {
        List<Friend> blockedUsers = new ArrayList<>();
        List<Friend> result = null;
        String sql = "SELECT * FROM friendships " +
                "WHERE (user_id1 = ? OR user_id2 = ?) AND status = 'blocked';";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (rowSet.next()) {
                blockedUsers.add(mapRowToFriend(rowSet, userId));
            }
            result = getFriendUsernames(blockedUsers);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return result;
    }

    @Override
    public void cancelFriendRequest(int userId1, int userId2) {
        String sql = "DELETE FROM friendships " +
                "WHERE user_id1 = ? AND user_id2 = ? AND status = 'pending';";
        try {
            jdbcTemplate.update(sql, userId1, userId2);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<Friend> getFriendList(int userId) {
        List<Friend> friends = new ArrayList<>();
        List<Friend> result = null;
        String sql = "SELECT * FROM friendships " +
                "WHERE (user_id1 = ? OR user_id2 = ?) AND status = 'accepted';";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (rowSet.next()) {
                friends.add(mapRowToFriend(rowSet, userId));
            }
            result = getFriendUsernames(friends);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return result;
    }

    private List<Friend> getFriendUsernames(List<Friend> friends) {
        String sql = "SELECT username, user_id FROM users " +
                "WHERE user_id = ";
        for (Friend friend : friends) {
            sql += friend.getFriendId() + " OR user_id = ";
        }
        sql = sql.substring(0, sql.length() - 14) + ";";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                for (Friend friend : friends) {
                    if (friend.getFriendId() == rowSet.getInt("user_id")) {
                        friend.setUsername(rowSet.getString("username"));
                    }
                }
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return friends;
    }

    @Override
    public boolean isFriend(int userId1, int userId2) {
        String sql = "SELECT * FROM friendships " +
                "WHERE (user_id1 = ? AND user_id2 = ?) OR (user_id1 = ? AND user_id2 = ?) AND status = 'accepted';";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId1, userId2, userId2, userId1);
            return rowSet.next();
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Friend mapRowToFriend(SqlRowSet rowSet, int userId) {
        Friendship friendship = new Friendship();
        friendship.setFriendshipId(rowSet.getInt("friendship_id"));
        friendship.setUserId1(rowSet.getInt("user_id1"));
        friendship.setUserId2(rowSet.getInt("user_id2"));
        friendship.setStatus(rowSet.getString("status"));
        if (rowSet.getTimestamp("created_at") != null) {
            friendship.setCreatedAt(rowSet.getTimestamp("created_at").toLocalDateTime());
        }
        Friend friend = new Friend();
        if (friendship.getUserId1() == userId) {
            friend.setUserId(friendship.getUserId1());
            friend.setFriendId(friendship.getUserId2());
        } else {
            friend.setUserId(friendship.getUserId2());
            friend.setFriendId(friendship.getUserId1());
        }
        friend.setStatus(friendship.getStatus());
        friend.setFrienshipId(friendship.getFriendshipId());


        return friend;
    }
}
