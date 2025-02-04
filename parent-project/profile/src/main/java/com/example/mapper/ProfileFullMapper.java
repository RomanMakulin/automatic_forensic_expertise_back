package com.example.mapper;

import com.example.model.Profile;
import com.example.model.Status;
import com.example.model.dto.ProfileFullDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { FileMapper.class, DirectionMapper.class })
public interface ProfileFullMapper {

    @Mapping(source = "appUser.id", target = "userId")
    @Mapping(source = "appUser.fullName", target = "fullName")
    @Mapping(source = "appUser.email", target = "email")
    @Mapping(source = "appUser.registrationDate", target = "registrationDate")
    @Mapping(source = "appUser.role.id", target = "roleId")
    @Mapping(source = "appUser.role.name", target = "roleName")

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.country", target = "country")
    @Mapping(source = "location.region", target = "region")
    @Mapping(source = "location.city", target = "city")
    @Mapping(source = "location.address", target = "address")

    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "status.name", target = "statusName")
    @Mapping(source = "status.verificationResult", target = "verificationResult", qualifiedByName = "verificationToString")
    @Mapping(source = "status.activityStatus", target = "activityStatus", qualifiedByName = "activityToString")

    ProfileFullDTO toDto(Profile profile);

    @InheritInverseConfiguration
    @Mapping(source = "verificationResult", target = "status.verificationResult", qualifiedByName = "stringToVerificationResult")
    @Mapping(source = "activityStatus", target = "status.activityStatus", qualifiedByName = "stringToActivityStatus")
    Profile toEntity(ProfileFullDTO dto);

    // Конвертация enum -> String
    @Named("verificationToString")
    static String verificationToString(Status.VerificationResult verificationResult) {
        return verificationResult != null ? verificationResult.name() : null;
    }

    @Named("activityToString")
    static String activityToString(Status.ActivityStatus activityStatus) {
        return activityStatus != null ? activityStatus.name() : null;
    }

    // Конвертация String -> enum
    @Named("stringToVerificationResult")
    static Status.VerificationResult stringToVerificationResult(String value) {
        return value != null ? Status.VerificationResult.valueOf(value) : null;
    }

    @Named("stringToActivityStatus")
    static Status.ActivityStatus stringToActivityStatus(String value) {
        return value != null ? Status.ActivityStatus.valueOf(value) : null;
    }
}
