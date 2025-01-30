package com.example.adminservice.api.rest;

import com.example.adminservice.api.dto.profile.ProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/administration")
public class AdminController {

    @GetMapping("/get-all-profiles")
    public ResponseEntity<List<ProfileDto>> getAllProfiles() {
        // TODO logic
    }

    @GetMapping("/get-unverified-profiles")
    public ResponseEntity<List<ProfileDto>> getUnverifiedProfiles() {
        // TODO logic
    }

}
