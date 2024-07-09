package com.pgsintl.supplychaintracking.services;

import com.pgsintl.supplychaintracking.dto.OrdersTrackingDto;
import com.pgsintl.supplychaintracking.entities.Account;
import com.pgsintl.supplychaintracking.entities.Orders;
import com.pgsintl.supplychaintracking.entities.StatusOrders;
import com.pgsintl.supplychaintracking.repository.AccountRepository;
import com.pgsintl.supplychaintracking.repository.OrdersRepository;
import com.pgsintl.supplychaintracking.repository.ReclamationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class OrdersService implements OrdersIService {

    OrdersRepository ordersRepository;
    ReclamationRepository reclamationRepository;
    AccountRepository accountRepository;
    @Override
    public Orders addOrder(Orders orders, Long idCarrier, Long idDriver) {
        Account carrier = accountRepository.findById(idCarrier).orElse(null);
        Account driver = accountRepository.findById(idDriver).orElse(null);

        orders.setCarrier(carrier);
        orders.setDriver(driver);

        return ordersRepository.save(orders);

    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public List<Orders> getAllOrderByCarrier(Long idCarrier) {
        List<Orders> ordersList = new ArrayList<>();
        Optional<Account> optionalAccount = accountRepository.findById(idCarrier);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            for (Orders orders1 : account.getOrdersCarrier()) {
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(orders1.getDateOrders());
                cal1.set(Calendar.HOUR_OF_DAY, 0);
                cal1.set(Calendar.MINUTE, 0);
                cal1.set(Calendar.SECOND, 0);
                cal1.set(Calendar.MILLISECOND, 0);

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(new Date());
                cal2.set(Calendar.HOUR_OF_DAY, 0);
                cal2.set(Calendar.MINUTE, 0);
                cal2.set(Calendar.SECOND, 0);
                cal2.set(Calendar.MILLISECOND, 0);

                if (!cal1.getTime().equals(cal2.getTime())){
                    ordersList.add(orders1);

                }

            }
        }

        return ordersList;
    }

    @Override
    public List<Orders> getOrdersTodayBycarrier(Long idCarrier) {
        List<Orders> ordersList = new ArrayList<>();
        Optional<Account> optionalAccount = accountRepository.findById(idCarrier);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            for (Orders orders1 : account.getOrdersCarrier()) {
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(orders1.getDateOrders());
                cal1.set(Calendar.HOUR_OF_DAY, 0);
                cal1.set(Calendar.MINUTE, 0);
                cal1.set(Calendar.SECOND, 0);
                cal1.set(Calendar.MILLISECOND, 0);

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(new Date());
                cal2.set(Calendar.HOUR_OF_DAY, 0);
                cal2.set(Calendar.MINUTE, 0);
                cal2.set(Calendar.SECOND, 0);
                cal2.set(Calendar.MILLISECOND, 0);

                if (cal1.getTime().equals(cal2.getTime())){
                    ordersList.add(orders1);

                }

            }
        }

        return ordersList ;
    }

    @Override
    public List<Orders> getOrdersByDriver(Long idDriver) {
        List<Orders> ordersList = new ArrayList<>();


        for (Orders orders1 : ordersRepository.findAll().stream().filter(orders -> orders.getDriver().getUserNumber().equals(idDriver)).toList()) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(orders1.getDateOrders());
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new Date());
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.MILLISECOND, 0);

            if (!cal1.getTime().equals(cal2.getTime())){
                ordersList.add(orders1);

            }


        }
        return ordersList;
    }

    @Override
    public List<Orders> getOrdersTodayBydriver(Long idDriver) {
        List<Orders> ordersList = new ArrayList<>();


        for (Orders orders1 : ordersRepository.findAll().stream().filter(orders -> orders.getDriver().getUserNumber().equals(idDriver)).toList()) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(orders1.getDateOrders());
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new Date());
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.MILLISECOND, 0);

            if (cal1.getTime().equals(cal2.getTime())){
                ordersList.add(orders1);

            }


        }


        return ordersList ;
    }

    @Override
    public void updatePosition( OrdersTrackingDto ordersTrackingDto) {
        Orders orders = ordersRepository.findById(ordersTrackingDto.getIdOrders()).orElse(null);
        if(orders!=null){
            orders.setOrdersNowLat(ordersTrackingDto.getOrdersNowLat());
            orders.setOrdersNowLong(ordersTrackingDto.getOrdersNowLong());
            ordersRepository.save(orders);


        }
    }

    @Override
    public void startingOrders(Long idOrders) {
        Orders orders = ordersRepository.findById(idOrders).orElse(null);
        if(orders!=null){
            orders.setStatus(StatusOrders.IN_PROGRESS);
            ordersRepository.save(orders);
        }
    }

    @Override
    public boolean deleteOrders(Long idOrders) {
        Orders orders = ordersRepository.findById(idOrders).orElse(null);

        if(orders!=null){
        ordersRepository.deleteById(idOrders);
        return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Orders updateOrders(Long orderId, Orders updatedOrders) {
        Optional<Orders> existingOrderOptional = ordersRepository.findById(orderId);
        if (existingOrderOptional.isPresent()) {
            Orders existingOrder = existingOrderOptional.get();
            // Update the fields of the existing order
            existingOrder.setArrivalLat(updatedOrders.getArrivalLat());
            existingOrder.setArrivalLong(updatedOrders.getArrivalLong());
            existingOrder.setStartingPoint(updatedOrders.getStartingPoint());
            existingOrder.setArrivalPoint(updatedOrders.getArrivalPoint());
            existingOrder.setDistance(updatedOrders.getDistance());
            existingOrder.setEstimation(updatedOrders.getEstimation());
            existingOrder.setWeightOrders(updatedOrders.getWeightOrders());
            existingOrder.setUnitProduct(updatedOrders.getUnitProduct());
            existingOrder.setProductOrders(updatedOrders.getProductOrders());
            existingOrder.setStatus(updatedOrders.getStatus());
            existingOrder.setDateOrders(updatedOrders.getDateOrders());
            existingOrder.setDateFinOrders(updatedOrders.getDateFinOrders());
            existingOrder.setCarrier(updatedOrders.getCarrier());
            existingOrder.setDriver(updatedOrders.getDriver());

            return ordersRepository.save(existingOrder);
        } else {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
    }



}
