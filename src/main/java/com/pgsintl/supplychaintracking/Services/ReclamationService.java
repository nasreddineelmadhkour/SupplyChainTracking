package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Repository.OrdersRepository;
import com.pgsintl.supplychaintracking.Repository.ReclamationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service @AllArgsConstructor
public class ReclamationService implements ReclamationIService {
    OrdersRepository ordersRepository;
    ReclamationRepository reclamationRepository;


    @Override
    public Reclamation addReclamation(Reclamation reclamation, Long idOrders) {

        Orders orders = ordersRepository.findById(idOrders).orElse(null);

            if(orders!=null) {
                reclamation.setDateReclamation(new Date());
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
