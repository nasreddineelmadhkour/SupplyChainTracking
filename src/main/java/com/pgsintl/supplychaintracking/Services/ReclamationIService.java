package com.pgsintl.supplychaintracking.services;

import com.pgsintl.supplychaintracking.entities.Reclamation;

import java.util.List;

public interface ReclamationIService {
    public Reclamation addReclamation(Reclamation reclamation , Long idOrders);
    public List<Reclamation> getAll();
}
