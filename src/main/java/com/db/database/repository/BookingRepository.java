package com.db.database.repository;

import com.db.database.entities.Booking;
import com.db.database.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    boolean existsByServiceProviderIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
            Long providerId, LocalDate date, LocalTime time, BookingStatus status);
}
