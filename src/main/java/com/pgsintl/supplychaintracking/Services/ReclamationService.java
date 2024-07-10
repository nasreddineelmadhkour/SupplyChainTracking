package com.pgsintl.supplychaintracking.services;

import com.pgsintl.supplychaintracking.entities.Orders;
import com.pgsintl.supplychaintracking.entities.Reclamation;
import com.pgsintl.supplychaintracking.repository.OrdersRepository;
import com.pgsintl.supplychaintracking.repository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReclamationService implements ReclamationIService {
    @Autowired

    OrdersRepository ordersRepository;
    @Autowired

    ReclamationRepository reclamationRepository;


    @Override
    public Reclamation addReclamation(Reclamation reclamation, Long idOrders) {

        Orders orders = ordersRepository.findById(idOrders).orElse(null);

            if(orders!=null) {
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
}
