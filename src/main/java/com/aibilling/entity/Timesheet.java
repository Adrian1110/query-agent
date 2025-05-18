package com.aibilling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "timesheet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "work_date")
    private LocalDate workDate;

    @Column(name = "hours_worked")
    private int hoursWorked;

    @Column(name = "hour_rate")
    private double hourRate;
}
