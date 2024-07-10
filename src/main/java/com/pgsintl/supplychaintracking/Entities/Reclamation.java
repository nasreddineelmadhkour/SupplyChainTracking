package com.pgsintl.supplychaintracking.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reclamation")
@Setter
@Getter
@NoArgsConstructor
public class Reclamation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long reclamationNumber;
    String description;
    Date dateReclamation;
    @Enumerated(EnumType.STRING)
    StatusReclamation statusReclamation;
}
