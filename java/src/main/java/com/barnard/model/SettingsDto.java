package com.barnard.model;

public class SettingsDto {

    private boolean profilePublic;
    private boolean defaultPublic;

    public boolean isProfilePublic() {
        return profilePublic;
    }

    public void setProfilePublic(boolean profilePublic) {
        this.profilePublic = profilePublic;
    }

    public boolean isDefaultPublic() {
        return defaultPublic;
    }

    public void setDefaultPublic(boolean defaultPublic) {
        this.defaultPublic = defaultPublic;
    }

}
