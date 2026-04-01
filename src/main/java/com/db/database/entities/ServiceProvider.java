package com.db.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "service_providers")
@Getter
@Setter
@NoArgsConstructor
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "provider_categories",
            joinColumns = @JoinColumn(name = "provider_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )

    @OnDelete(action = OnDeleteAction.CASCADE)

    private Set<ServiceCategory> serviceCategories = new HashSet<>();


    private Double hourlyRate; // Moved from schedule
    private Integer experienceYears; // Added for profile completeness
    private String bio;
    private String status;

    @CreationTimestamp
    private LocalDateTime createDate;


    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();
    private String email;
    private String name;


    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
        schedule.setServiceProvider(this); // Sets the foreign key
    }

}
