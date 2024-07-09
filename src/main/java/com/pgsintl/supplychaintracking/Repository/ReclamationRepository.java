package com.pgsintl.supplychaintracking.repository;

import com.pgsintl.supplychaintracking.entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {
}
