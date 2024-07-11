package com.pgsintl.supplychaintracking.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reclamation")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reclamation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reclamationNumber;

    @Column(nullable = false)
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReclamation = new Date();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReclamation statusReclamation;
}
