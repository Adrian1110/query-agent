package com.example.aibilling.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "query_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "generated_sql")
    private String generatedSql;

    @Column(name = "sandbox")
    private boolean sandbox = true;
}
