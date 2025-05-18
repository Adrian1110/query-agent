package com.aibilling.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "billing_department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "billing_code")
    private String billingCode;

    @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
