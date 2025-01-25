package com.example.auth.service;

import com.example.auth.model.dto.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface RegistrationService {

    void register(RegistrationRequest request);

}
