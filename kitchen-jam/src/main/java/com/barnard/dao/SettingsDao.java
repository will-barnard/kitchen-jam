package com.barnard.dao;

public interface SettingsDao {

    boolean makeProfilePublic(int userId);
    boolean makeProfilePrivate(int userId);
    boolean makeDefaultPublic(int userId);
    boolean makeDefaultPrivate(int userId);

}
