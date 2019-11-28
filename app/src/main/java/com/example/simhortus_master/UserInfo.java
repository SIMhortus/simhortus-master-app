package com.example.simhortus_master;

public class UserInfo {

    public String first_name, last_name, phone_number;

    /****
     *
     * @param first_name: for first name
     * @param last_name: for last name
     */
    public UserInfo(String first_name, String last_name, String phone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
