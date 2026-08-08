package com.db.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingResponse {
    private Long bookingId;
    private String customerName;    // Added for Provider Dashboard
    private String customerAddress; // Added for Provider Dashboard
    private String customerPhone;   // Added for Provider Dashboard
    private String providerName;    // Used for User Dashboard
    private String categoryName;
    private String appointmentDate;
    private String appointmentTime;
    private String status;          // PENDING, CONFIRMED, COMPLETED, CANCELLED
    private Integer reviewStars;
    private String reviewComments;
}