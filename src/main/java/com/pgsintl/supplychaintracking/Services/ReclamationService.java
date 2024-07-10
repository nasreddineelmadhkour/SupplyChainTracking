package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Repository.OrdersRepository;
import com.pgsintl.supplychaintracking.Repository.ReclamationRepository;
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
