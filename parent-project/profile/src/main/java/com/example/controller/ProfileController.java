package com.example.controller;

import com.example.model.*;
import com.example.service.LocationService;
import com.example.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    private final LocationService locationService;

    public ProfileController(ProfileService profileService, LocationService locationService) {
        this.profileService = profileService;
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfile(@PathVariable UUID id) {
        return profileService.getProfileById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @PostMapping("/save")
    public ResponseEntity<Profile> save(@RequestBody Profile profile) {
        AppUser appUser = new AppUser();
        Location location = locationService.save(new Location());
        Status status = new Status();
        Role role = new Role();
        appUser.setRole(role);
        System.out.println(profile);
        profile.setLocation(location);
        profile.setStatus(status);
        profile.setAppUser(appUser);
        return ResponseEntity.ok(profileService.save(profile));
    }

    @PostMapping("/update")
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.update(profile));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
