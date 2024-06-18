package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Entities.Reclamation;

import java.util.List;

public interface ReclamationIService {
    public Reclamation addReclamation(Reclamation reclamation , Long idOrders);
    public List<Reclamation> getAll();
}
