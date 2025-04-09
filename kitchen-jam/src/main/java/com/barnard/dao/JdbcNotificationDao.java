package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcNotificationDao implements NotificationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Notification getNotification(int notificationId) {
        Notification notification = null;
        String sql = "SELECT * FROM notifications WHERE notification_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, notificationId);
            if (rowSet.next()) {
                notification = mapRowToNotification(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return notification;
    }

    @Override
    public List<Notification> getNotifications(int userId) {
        List<Notification> notifications;
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            notifications = mapRowSetToNotifications(rowSet);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return notifications;
    }

    @Override
    public List<Notification> getOldNotifications(int userId) {
        List<Notification> notifications;
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = TRUE ORDER BY created_at DESC";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            notifications = mapRowSetToNotifications(rowSet);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return notifications;
    }

    @Override
    public Notification createNotification(Notification notification) {
        Notification createdNotification = null;
        String sql = "INSERT INTO notifications (user_id, actor_id, type, target_id, target_type, target_url, message, is_read, comment_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING notification_id";
        try {
            int notificationId = jdbcTemplate.queryForObject(sql, int.class,
                notification.getUserId(),
                notification.getActorId(),
                notification.getType(),
                notification.getTargetId(),
                notification.getTargetType(),
                notification.getTargetUrl(),
                notification.getMessage(),
                false,
                notification.getCommentId()
            );
            createdNotification = getNotification(notificationId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return createdNotification;
    }

    @Override
    public void markNotificationAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
        try {
            jdbcTemplate.update(sql, notificationId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void markAllNotificationsAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteNotification(int notificationId) {
        String sql = "DELETE FROM notifications WHERE notification_id = ?";
        try {
            jdbcTemplate.update(sql, notificationId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private List<Notification> mapRowSetToNotifications(SqlRowSet rowSet) {
        List<Notification> notifications = new ArrayList<>();
        while (rowSet.next()) {
            notifications.add(mapRowToNotification(rowSet));
        }
        return notifications;
    }

    private Notification mapRowToNotification(SqlRowSet rs) {
        Notification notification = new Notification();

        notification.setNotificationId(rs.getInt("notification_id"));
        notification.setUserId(rs.getInt("user_id"));
        notification.setActorId(rs.getInt("actor_id"));
        notification.setType(rs.getString("type"));
        try {
            notification.setTargetId(rs.getInt("target_id"));
        } catch (Exception e) {
            // do nothing
        }
        notification.setTargetType(rs.getString("target_type"));
        notification.setTargetUrl(rs.getString("target_url"));
        notification.setMessage(rs.getString("message"));
        notification.setRead(rs.getBoolean("is_read"));
        if (rs.getTimestamp("created_at") != null) {
            notification.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        } else {
            notification.setCreatedAt(null);
        }

        return notification;
    }
}
