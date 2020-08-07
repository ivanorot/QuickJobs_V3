package com.example.quickjobs.helper;

public class NotificationManager {

    private boolean isNonPromotedNotificationsActive;
    private boolean isTimeSensitiveActive;
    private boolean isMessagesActive;



    public void setNonPromotedNotificationsActive(boolean nonPromotedNotificationsActive) {
        isNonPromotedNotificationsActive = nonPromotedNotificationsActive;
    }

    public void setTimeSensitiveActive(boolean timeSensitiveActive) {
        isTimeSensitiveActive = timeSensitiveActive;
    }

    public void setMessagesActive(boolean messagesActive) {
        isMessagesActive = messagesActive;
    }
}
