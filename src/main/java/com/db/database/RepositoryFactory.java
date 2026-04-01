package com.db.database;

import com.db.database.repository.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RepositoryFactory {

    @Autowired private UserRepository userRepository;

    @Autowired private RolesRepository rolesRepository;

    @Autowired private PermissionRepository permissionRepository;

    @Autowired private AddressRepository addressRepository;

    @Autowired private BookingRepository bookingRepository;

    @Autowired private PaymentRepository paymentRepository;

    @Autowired private ReviewsRepository reviewsRepository;

    @Autowired private ScheduleRepository scheduleRepository;

    @Autowired private ServiceCategoryRepository serviceCategoryRepository;

    @Autowired private ServiceProviderRepository serviceProviderRepository;

}
