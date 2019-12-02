package com.example.simhortus_master;

public class UserGardenInfo {

    public String first_name, last_name, phone_number;
    public boolean is_approved;



    public UserGardenInfo(){}

    public UserGardenInfo(String first_name, String last_name, String phone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
    }

    public UserGardenInfo(boolean is_approved) {
        this.is_approved = is_approved;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isStatus() {
        return is_approved;
    }

    public void setStatus(boolean is_approved) {
        this.is_approved = is_approved;
    }
}