package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Dto.OrdersTrackingDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Entities.StatusOrders;
import com.pgsintl.supplychaintracking.Repository.AccountRepository;
import com.pgsintl.supplychaintracking.Repository.OrdersRepository;
import com.pgsintl.supplychaintracking.Repository.ReclamationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrdersService implements OrdersIService {
    private  OrdersRepository ordersRepository;
    private  ReclamationRepository reclamationRepository;
    private  AccountRepository accountRepository;
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
    public List<Orders> getAllOrderByDriver(Long idDriver) {
        return ordersRepository.findByReclamationIsNotNullAndDriver_UserNumber(idDriver);
    }

    @Override
    public List<Orders> getAllOrderByCarrier(Long idCarrier) {

        return ordersRepository.findByReclamationIsNotNullAndCarrierUserNumber(idCarrier);
    }

    @Override
    public List<Orders> getOrderByCarrier(Long idCarrier) {

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
/*
    @Override
    public Orders updateOrders(Long orderId, Orders updatedOrders ,Long idDriver,int isS,int isA) {
        Optional<Orders> existingOrderOptional = ordersRepository.findById(orderId);
        Optional<Account> existingDriverOptional = accountRepository.findById(idDriver);
        log.info("Avant present");

        if (existingOrderOptional.isPresent() && existingDriverOptional.isPresent()) {
            log.info("isPresent");
            Orders existingOrder = existingOrderOptional.get();
            Account existDriver = existingDriverOptional.get();
            // Update the fields of the existing order
            existingOrder.setArrivalLat(updatedOrders.getArrivalLat());
            existingOrder.setArrivalLong(updatedOrders.getArrivalLong());
            if(isS==1) {
                existingOrder.setStartingPoint(updatedOrders.getStartingPoint());
            }
            if(isA==1){
            existingOrder.setArrivalPoint(updatedOrders.getArrivalPoint());
            }
            existingOrder.setDistance(updatedOrders.getDistance());
            existingOrder.setEstimation(updatedOrders.getEstimation());
            existingOrder.setWeightOrders(updatedOrders.getWeightOrders());
            existingOrder.setUnitProduct(updatedOrders.getUnitProduct());
            existingOrder.setProductOrders(updatedOrders.getProductOrders());
            existingOrder.setStatus(updatedOrders.getStatus());
            existingOrder.setDateOrders(updatedOrders.getDateOrders());
            existingOrder.setDriver(existDriver);
            existingOrder.setStatus(updatedOrders.getStatus());
            return ordersRepository.save(existingOrder);
        } else {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
    }
*/
    @Override
    public void completedOrders(Long idOrders) {
            Orders orders = ordersRepository.findById(idOrders).orElse(null);
            if(orders!=null){
                orders.setStatus(StatusOrders.COMPLETED);
                orders.setDateFinOrders(new Date());
                ordersRepository.save(orders);
            }
    }


    @Scheduled(fixedRate = 10000)
    public void changeStatusOrders(){
        Date Yesterday = getYesterday();
        Date Tomorrow = getTomorrow();
        for (Orders orders :ordersRepository.findByDateOrdersIsBetweenAndStatus(Yesterday,Tomorrow,StatusOrders.DELAYED))
        {

            orders.setStatus(StatusOrders.PENDING);
            ordersRepository.save(orders);
            log.info("Orderproduct : "+orders.getProductOrders()+" | Date:"+orders.getDateOrders().toString());
        }

    }

    public Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }
    public Date getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
