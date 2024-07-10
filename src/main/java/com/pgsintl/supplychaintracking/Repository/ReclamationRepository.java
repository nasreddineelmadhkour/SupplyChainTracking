package com.pgsintl.supplychaintracking.Repository;

import com.pgsintl.supplychaintracking.Entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {
}
