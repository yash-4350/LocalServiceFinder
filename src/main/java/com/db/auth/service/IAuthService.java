package com.db.auth.service;

import com.db.auth.dto.ProviderOnboardingRequest;
import com.db.auth.dto.SignUpRequest;
import com.db.database.entities.ServiceCategory;
import com.db.database.entities.ServiceProvider;
import com.db.database.entities.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IAuthService {
    public User registerUser(SignUpRequest request);


    public ServiceProvider onboardProvider(ProviderOnboardingRequest request) throws UnsupportedEncodingException,MessagingException;

    List<ServiceCategory> getAllServiceCategories();
}
