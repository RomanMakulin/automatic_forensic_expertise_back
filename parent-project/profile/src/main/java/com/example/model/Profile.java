package com.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "profile")
public class Profile {

    @Id
    private UUID id = UUID.randomUUID();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    private String photo;

    private String phone;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Column(name = "plan_start_date")
    private LocalDateTime planStartDate;

    @Column(name = "plan_duration_month")
    private LocalDateTime planDurationMonth;

    @JsonManagedReference
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<File> files = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Direction> directions;

}