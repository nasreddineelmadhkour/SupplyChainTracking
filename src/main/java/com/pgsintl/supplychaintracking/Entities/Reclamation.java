package com.pgsintl.supplychaintracking.Entities;

import jakarta.persistence.*;
import lombok.*;

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


}
