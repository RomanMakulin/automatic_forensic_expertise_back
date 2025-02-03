package com.example.repository;

import com.example.model.Profile;
import com.example.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    List<Profile> findAllByStatus_VerificationResult(Status.VerificationResult  statusVerificationResult);

}
