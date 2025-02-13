package com.example.adminservice.mapper;

import com.example.adminservice.api.dto.profile.*;
import com.example.adminservice.api.dto.profile.original.OriginalProfileDto;
import com.example.adminservice.integration.minio.MinioIntegration;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс для преобразования объектов OriginalProfileDto в объекты ProfileDto.
 */
@Component
public class ProfileMapper {

    /**
     * Интеграция с Minio.
     */
    private final MinioIntegration minioIntegration;

    /**
     * Конструктор класса ProfileMapper.
     *
     * @param minioIntegration интеграция с Minio
     */
    public ProfileMapper(MinioIntegration minioIntegration) {
        this.minioIntegration = minioIntegration;
    }

    /**
     * Преобразует список OriginalProfileDto в список ProfileDto.
     *
     * @param originalProfileDtoList список OriginalProfileDto
     * @return список ProfileDto
     */
    public List<ProfileDto> toProfileDtoList(List<OriginalProfileDto> originalProfileDtoList) {
        return originalProfileDtoList.stream()
                .map(this::toProfileDto)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует OriginalProfileDto в ProfileDto.
     *
     * @param originalProfile OriginalProfileDto
     * @return ProfileDto
     */
    public ProfileDto toProfileDto(OriginalProfileDto originalProfile) {
        ProfileDto profileDto = new ProfileDto();

        profileDto.setId(originalProfile.getId());
        profileDto.setUser(toUserDto(originalProfile));
        profileDto.setPhone(originalProfile.getPhone());
        profileDto.setPhoto(getPhotoUrl(originalProfile.getId()));
        profileDto.setPassport(getPassportUrl(originalProfile.getId()));
        profileDto.setDiplom(getDiplomUrl(originalProfile.getId()));
        profileDto.setLocation(toLocationDto(originalProfile));
        profileDto.setProfileStatus(toStatusDto(originalProfile));
        profileDto.setDirections(toDirectionDtoSet(originalProfile));
        profileDto.setFiles(toFileDtoSet(originalProfile));

        return profileDto;
    }

    /**
     * Преобразует OriginalProfileDto в UserDto.
     *
     * @param originalProfile OriginalProfileDto
     * @return UserDto
     */
    private UserDto toUserDto(OriginalProfileDto originalProfile) {
        if (originalProfile.getAppUser() == null) return null;

        UserDto userDto = new UserDto();
        userDto.setEmail(originalProfile.getAppUser().getEmail());
        userDto.setFullName(originalProfile.getAppUser().getFullName());
        userDto.setRegistrationDate(originalProfile.getAppUser().getRegistrationDate());
        userDto.setRole(originalProfile.getAppUser().getRole() != null ? originalProfile.getAppUser().getRole().getName() : null);
        return userDto;
    }

    /**
     * Преобразует OriginalProfileDto в LocationDto.
     *
     * @param originalProfile OriginalProfileDto
     * @return LocationDto
     */
    private LocationDto toLocationDto(OriginalProfileDto originalProfile) {
        if (originalProfile.getLocation() == null) return null;

        LocationDto locationDto = new LocationDto();
        locationDto.setAddress(originalProfile.getLocation().getAddress());
        locationDto.setCity(originalProfile.getLocation().getCity());
        locationDto.setCountry(originalProfile.getLocation().getCountry());
        locationDto.setRegion(originalProfile.getLocation().getRegion());
        return locationDto;
    }

    /**
     * Преобразует OriginalProfileDto в StatusDto.
     *
     * @param originalProfile OriginalProfileDto
     * @return StatusDto
     */
    private StatusDto toStatusDto(OriginalProfileDto originalProfile) {
        if (originalProfile.getStatus() == null) return null;

        StatusDto statusDto = new StatusDto();
        statusDto.setActivityStatus(originalProfile.getStatus().getActivityStatus());
        statusDto.setVerificationResult(originalProfile.getStatus().getVerificationResult());
        return statusDto;
    }

    /**
     * Преобразует OriginalProfileDto в множество DirectionDto.
     *
     * @param originalProfile OriginalProfileDto
     * @return множество DirectionDto
     */
    private Set<DirectionDto> toDirectionDtoSet(OriginalProfileDto originalProfile) {
        if (originalProfile.getDirections() == null) return new HashSet<>();

        return originalProfile.getDirections().stream()
                .map(direction -> new DirectionDto(direction.getId(), direction.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Преобразует OriginalProfileDto в множество FilesDto.
     *
     * @param originalProfile OriginalProfileDto
     * @return множество FilesDto
     */
    private Set<FilesDto> toFileDtoSet(OriginalProfileDto originalProfile) {
        if (originalProfile.getFiles() == null) return new HashSet<>();

        return originalProfile.getFiles().stream()
                .map(file -> new FilesDto(file.getId(), getFileUrl(file.getDownloadUrl()), file.getCreatedAt(), file.getDownloadUrl()))
                .collect(Collectors.toSet());
    }

    /**
     * Получает URL фотографии профиля.
     *
     * @param profileId идентификатор профиля
     * @return URL фотографии профиля
     */
    private String getPhotoUrl(UUID profileId) {
        try {
            return minioIntegration.getFile("get-photo", Map.of("profileId", profileId.toString()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Получает URL диплома профиля.
     *
     * @param profileId идентификатор профиля
     * @return URL диплома профиля
     */
    private String getDiplomUrl(UUID profileId) {
        try {
            return minioIntegration.getFile("get-diplom", Map.of("profileId", profileId.toString()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Получает URL паспорта профиля.
     *
     * @param profileId идентификатор профиля
     * @return URL паспорта профиля
     */
    private String getPassportUrl(UUID profileId) {
        try {
            return minioIntegration.getFile("get-passport", Map.of("profileId", profileId.toString()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Получает URL файла.
     *
     * @param path путь к файлу
     * @return URL файла
     */
    private String getFileUrl(String path) {
        try {
            return minioIntegration.getFile("get-file", Map.of("path", path));
        } catch (Exception e) {
            return null;
        }
    }
}