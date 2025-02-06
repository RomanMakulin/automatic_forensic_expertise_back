package com.example.service;

import com.example.model.Profile;
import com.example.model.newDto.profile.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MinioService {

    private final MinIOFileService minIOFileService;

    public MinioService(MinIOFileService minIOFileService) {
        this.minIOFileService = minIOFileService;
    }

    /**
     * Генерация списка DTO для профилей
     */
    public List<ProfileDto> generateProfileDtoList(List<Profile> profiles) {
        List<ProfileDto> profileDtoList = new ArrayList<>();

        for (Profile profile : profiles) {
            ProfileDto profileDto = new ProfileDto();
            UserDto userDto = new UserDto();
            LocationDto locationDto = new LocationDto();
            ProfileStatusDto profileStatusDto = new ProfileStatusDto();
            Set<DirectionDto> directionDtoList = new HashSet<>();
            Set<FilesDto> filesDtoList = new HashSet<>();

            // Обрабатываем направления (directions), если они есть
            if (profile.getDirections() != null) {
                profile.getDirections().forEach(direction -> {
                    DirectionDto directionDto = new DirectionDto();
                    directionDto.setId(direction.getId().toString());
                    directionDto.setName(direction.getName());
                    directionDtoList.add(directionDto);
                });
            }

            // Обрабатываем файлы, если они есть
            if (profile.getFiles() != null) {
                profile.getFiles().forEach(file -> {
                    String fileUrl = minIOFileService.getFile(file.getPath());

                    FilesDto filesDto = new FilesDto();
                    filesDto.setId(file.getId().toString());
                    filesDto.setCreatedAt(file.getUploadDate() != null ? file.getUploadDate().toString() : null);
                    filesDto.setPath(fileUrl);
                    filesDtoList.add(filesDto);
                });
            }

            // Получаем URL фото профиля
            String photoUrl = minIOFileService.getPhotoUrl(profile.getId());

            // Заполняем данные профиля
            if (profile.getStatus() != null) {
                profileStatusDto.setActivityStatus(
                        profile.getStatus().getActivityStatus() != null ? profile.getStatus().getActivityStatus().toString() : null);
                profileStatusDto.setVerificationResult(
                        profile.getStatus().getVerificationResult() != null ? profile.getStatus().getVerificationResult().toString() : null);
            }

            if (profile.getLocation() != null) {
                locationDto.setAddress(profile.getLocation().getAddress());
                locationDto.setCity(profile.getLocation().getCity());
                locationDto.setCountry(profile.getLocation().getCountry());
                locationDto.setRegion(profile.getLocation().getRegion());
            }

            if (profile.getAppUser() != null) {
                userDto.setEmail(profile.getAppUser().getEmail());
                userDto.setFullName(profile.getAppUser().getFullName());
                userDto.setRegistrationDate(profile.getAppUser().getRegistrationDate());
                userDto.setRole(profile.getAppUser().getRole() != null ? profile.getAppUser().getRole().toString() : null);
            }

            profileDto.setId(profile.getId());
            profileDto.setUser(userDto);
            profileDto.setPhoto(photoUrl);
            profileDto.setLocation(locationDto);
            profileDto.setProfileStatus(profileStatusDto);
            profileDto.setDirections(directionDtoList);
            profileDto.setFiles(filesDtoList);

            // Добавляем профиль в список
            profileDtoList.add(profileDto);
        }

        return profileDtoList;
    }
}
