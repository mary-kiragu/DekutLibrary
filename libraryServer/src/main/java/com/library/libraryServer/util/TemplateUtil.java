package com.library.libraryServer.util;


import com.library.libraryServer.domain.dto.*;

public class TemplateUtil {

    public static String passwordResetEmail(String name, String resetKey, String baseUrl, String appName, String email) {

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Hello " + name + "</p>\n" +
                "\n" +
                "    <p>You created an account on " + appName + ". Please click the link below to complete your account creation.</p>\n" +
                "\n" +
                "    <p>" + baseUrl + "reset?key=" + resetKey + "&email=" + email + "</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    public static String generatePasswordReset(String name, String resetKey, String baseUrl, String email) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Hello " + name + ", you requested to reset your password</p>\n" +
                "\n" +
                "    <p>Please click the link below to reset your password</p>\n" +
                "\n" +
                "    <p>" + baseUrl + "reset?key=" + resetKey + "&email=" + email + "</p>\n" +
                "</body>\n" +
                "</html>";
    }


    public static String generatePaymentReminder(short paymentReminder, UserDTO user) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Payment Reminder</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Hello " + user.getName() + ",</p>\n" +
                "\n" +
                "    <p>Your next billing date is " + paymentReminder + " days away</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String generateReturnBookReminder(short paymentReminder, BookDTO book) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Due Date Reminder</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Hello " + book.getBorrowedBy() + ",</p>\n" +
                "\n" +
                "    <p>Your due date is " + paymentReminder + " days away</p>\n" +
                "</body>\n" +
                "</html>";
    }
}
