package com.db.auth.service;

import com.db.auth.dto.ProviderOnboardingRequest;
import com.db.auth.dto.ScheduleRequest;
import com.db.auth.dto.SignUpRequest;
import com.db.auth.service.IAuthService;
import com.db.database.RepositoryFactory;
import com.db.database.entities.*;
import com.db.integration.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {
    @Autowired
    RepositoryFactory repositoryFactory;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public User registerUser(SignUpRequest request) {

        if (repositoryFactory.getUserRepository().existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email is already in use !!!");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCellPhone(request.getCellphone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //     Role userRole = repositoryFactory.getRolesRepository().findByName("USER);
        Role userRole = repositoryFactory.getRolesRepository().findByName(request.getRole());
        if (userRole == null) {
            throw new RuntimeException("Role not found");
        }
        user.setRole(userRole);

        if (request.getRole().equalsIgnoreCase("PROVIDER")) {
            user.setApproved(false);

        } else {
            user.setApproved(true);
        }
        if (request.getAddress() != null) {
            Address newaddress = new Address();
            newaddress.setAddressLine1(request.getAddress().getAddressLine1());
            newaddress.setAddressLine2(request.getAddress().getAddressLine2());
            newaddress.setCity(request.getAddress().getCity());
            newaddress.setState(request.getAddress().getState());
            newaddress.setZipCode(request.getAddress().getZipCode());
            newaddress.setAddressType(request.getAddress().getAddressType());
            newaddress.setVersion(0L);

            user.addAddress(newaddress);
        }

        return repositoryFactory.getUserRepository().save(user);
    }

    @Override
    public ServiceProvider onboardProvider(ProviderOnboardingRequest request) throws MessagingException, UnsupportedEncodingException
    {
        User user = repositoryFactory.getUserRepository().findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getRole() == null || !user.getRole().getName().equalsIgnoreCase("PROVIDER")) {
            throw new RuntimeException("Error: Only users with ROLE_PROVIDER can onboard.");
        }



        if (repositoryFactory.getServiceProviderRepository().existsByUserId(user.getId())) {
            throw new RuntimeException("Error: User has already onboarded.");
        }


        // --- UPDATED: Fetch multiple categories ---
        List<ServiceCategory> categories = repositoryFactory.getServiceCategoryRepository().findAllById(request.getCategoryIds());


        // Ensure all provided IDs were valid and found in the DB
        if (categories.isEmpty() || categories.size() != request.getCategoryIds().size()) {
            throw new RuntimeException("Error: One or more Service Categories are invalid or do not exist.");
        }


        ServiceProvider providerProfile = new ServiceProvider();
        providerProfile.setUser(user);


        // Set the collection of categories
        providerProfile.setServiceCategories(new HashSet<>(categories));


        providerProfile.setExperienceYears(request.getExperienceYears());
        providerProfile.setHourlyRate(request.getHourlyRate());
        providerProfile.setBio(request.getBio());
        providerProfile.setStatus("VERIFICATION PENDING");
        // Map and add schedules
        for (ScheduleRequest scheduleReq : request.getSchedules()) {
            if (scheduleReq.getEndTime().isBefore(scheduleReq.getStartTime())) {
                throw new RuntimeException("Error: End time cannot be before Start time on " + scheduleReq.getDayOfWeek());
            }


            Schedule newSchedule = new Schedule();
            newSchedule.setDayOfWeek(scheduleReq.getDayOfWeek());
            newSchedule.setStartTime(scheduleReq.getStartTime());
            newSchedule.setEndTime(scheduleReq.getEndTime());


            providerProfile.addSchedule(newSchedule);
        }


        ServiceProvider savedProvider = repositoryFactory.getServiceProviderRepository().save(providerProfile);
        // Extract category names for the email
        String categoryNames = savedProvider.getServiceCategories().stream()
                .map(ServiceCategory::getName)
                .reduce((a, b) -> a + ", " + b).orElse("Unknown");


        // Trigger the email to the Admin (run asynchronously in production, but fine synchronously for now)
        emailService.sendProviderVerificationEmail(
                savedProvider.getId(),
                savedProvider.getUser().getFirstName() + " " + savedProvider.getUser().getLastName(),
                categoryNames
        );


        return savedProvider;
    }

    @Override
    public List<ServiceCategory> getAllServiceCategories() {
        List<ServiceCategory> all = repositoryFactory.getServiceCategoryRepository().findAll();
        return all;
    }
}



