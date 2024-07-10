package com.pgsintl.supplychaintracking.Repository;

import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Entities.StatusReclamation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {

    List<Reclamation> findByStatusReclamation(StatusReclamation statusReclamation);
}
