package com.example.auth.util;

public class ApiPaths {

    public static class NotificationService {
        public static final String BASE = "http://localhost:8087";
        public static final String SEND_MAIL = "http://localhost:8087/api/notification/send";
        public static final String PUBLIC_SEND_MAIL = "http://localhost:8087/api/public-notification/send";
    }

    public static class AuthService {
        public static final String BASE = "http://localhost:8080";
        public static final String RESET_REQUEST = AuthService.BASE + "/auth/reset-password";
    }

    public static class Frontend {
        public static final String BASE = "http://localhost:8082";
        public static final String RESET_REQUEST = Frontend.BASE + "/reset-password";
    }

}
