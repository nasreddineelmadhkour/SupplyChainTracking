package com.pgsintl.supplychaintracking.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
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
    Long reclamationNumber;
    String description;
    Date dateReclamation;
    @Enumerated(EnumType.STRING)
    StatusReclamation statusReclamation;

}
