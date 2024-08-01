package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Entities.Reclamation;

import java.util.List;

public interface ReclamationIService {
    Reclamation addReclamation(Reclamation reclamation , Long idOrders);
    List<Reclamation> getAll();
    boolean resolvedClaim(Long idClaim);
}
