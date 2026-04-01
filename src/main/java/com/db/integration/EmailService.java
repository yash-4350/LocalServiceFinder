package com.db.integration;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;


import java.io.UnsupportedEncodingException;


public interface EmailService
{
    @Async
    void sendEmail(String to, String subject, String body, String senderName, String replyTo);
    public void sendProviderVerificationEmail(Long providerId, String providerName, String categories) throws MessagingException, UnsupportedEncodingException;
    public void sendProviderWelcomeEmail(String providerEmail, String providerName) throws MessagingException, UnsupportedEncodingException;

    void sendBookingNotificationToProvider(String providerEmail, String providerName,
                                           String customerName, String categoryName,
                                           String date, String time, Long bookingId);

    void sendBookingConfirmationToCustomer(String customerEmail, String customerName,
                                           String providerName, String categoryName,
                                           String date, String time);

}
