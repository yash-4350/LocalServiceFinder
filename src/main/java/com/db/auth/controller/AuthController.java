package com.db.auth.controller;

import com.db.auth.dto.ProviderOnboardingRequest;
import com.db.auth.dto.SignUpRequest;
import com.db.auth.service.IAuthService;
import com.db.database.entities.ServiceCategory;
import com.db.database.entities.User;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpRequest signUpRequest)
    {
        try
        {
            authService.registerUser(signUpRequest);
            return new ResponseEntity<>("User registered Successfully", HttpStatus.CREATED);
        }
        catch (Exception e)
        {
         return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/provider/onboard")
    public ResponseEntity<String> onboardProvider(@Valid @RequestBody ProviderOnboardingRequest request)
    {
        try
        {
            authService.onboardProvider(request);
            return new ResponseEntity<>("Service Provider onboarded successfully!", HttpStatus.CREATED);
        }
        catch (RuntimeException e)
        {
            // Catches validation errors (like invalid user ID, wrong role, or duplicate onboarding)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
