package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Entities.StatusReclamation;
import com.pgsintl.supplychaintracking.Repository.OrdersRepository;
import com.pgsintl.supplychaintracking.Repository.ReclamationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReclamationService implements ReclamationIService {
    private  OrdersRepository ordersRepository;
    private  ReclamationRepository reclamationRepository;
    @Override
    public Reclamation addReclamation(Reclamation reclamation, Long idOrders) {

        Orders orders = ordersRepository.findById(idOrders).orElse(null);
            if(orders!=null) {
                reclamation.setDateReclamation(new Date());
                reclamation.setStatusReclamation(StatusReclamation.NOT_RESOLVED);
                orders.setReclamation(reclamation);
                ordersRepository.save(orders);
                return orders.getReclamation();

            }
            return null;
    }


    @Override
    public List<Reclamation> getAll(){
        return reclamationRepository.findAll();
    }

    @Override
    public boolean resolvedClaim(Long idClaim) {
        Reclamation reclamation = reclamationRepository.findById(idClaim).orElse(null);
        if(reclamation!= null){
            reclamation.setStatusReclamation(StatusReclamation.RESOLVED);
            reclamationRepository.save(reclamation);
            return true;
        }

        return false;
    }
}
