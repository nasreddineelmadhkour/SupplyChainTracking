package com.pgsintl.supplychaintracking.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name = "reclamation")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long reclamationNumber;
    String description;
    Date dateReclamation;
    @Enumerated(EnumType.STRING)
    StatusReclamation statusReclamation;

}
