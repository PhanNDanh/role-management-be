package com.example.keycloakdemo.constant;

public class UserStatus {

    private UserStatus() {
    }

    /**
     * mới tạo
     */
    public static final int DRAFT = 0;

    /**
     * hoạt động
     */
    public static final int ACTIVE = 1;

    /**
     * khoá
     */
    public static final int LOCKED = 2;

    /**
     * vô hiệu
     */
    public static final int INACTIVE = 3;
}