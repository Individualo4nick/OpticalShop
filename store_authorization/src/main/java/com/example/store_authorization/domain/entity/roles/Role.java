package com.example.store_authorization.domain.entity.roles;


public enum Role{
    ADMIN,
    USER;
    public String getAuthority() {
        return name();
    }
    public static String[] getRoles(){
        return new String[]{String.valueOf(ADMIN), String.valueOf(USER)};
    }
}
