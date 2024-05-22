package com.pgsintl.supplychaintracking.Entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "photo")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String namePhoto;
    @Lob
    byte[] photo;
    String type;

}
