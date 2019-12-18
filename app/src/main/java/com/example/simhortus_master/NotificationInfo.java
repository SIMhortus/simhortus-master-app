package com.example.simhortus_master;

public class NotificationInfo {
    String date, description, full_name;
    int typeOfNotif;
    Boolean isRead;

    public NotificationInfo() {
    }

    public NotificationInfo(String date, String description, String full_name, int typeOfNotif, Boolean isRead) {
        this.date = date;
        this.description = description;
        this.full_name = full_name;
        this.typeOfNotif = typeOfNotif;
        this.isRead = isRead;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getFull_name() {
        return full_name;
    }

    public int getTypeOfNotif() {
        return typeOfNotif;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setTypeOfNotif(int typeOfNotif) {
        this.typeOfNotif = typeOfNotif;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
