package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Repository.AccountRepository;
import com.pgsintl.supplychaintracking.Repository.OrdersRepository;
import com.pgsintl.supplychaintracking.Repository.ReclamationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        List<Orders> orders = new ArrayList<>();
        Account account = accountRepository.findById(idCarrier).orElse(null);
        if(account!=null){
            orders.addAll(account.getOrdersCarrier());
        }

        return orders;
       // return accountRepository.findById(idCarrier).map(account -> account.getOrdersDriver().stream().toList()).orElse(null);
    }

    @Override
    public List<Orders> getOrdersTodayBycarrier(Long idCarrier) {
        List<Orders> ordersList = new ArrayList<>();


       for (Orders orders1 : accountRepository.findById(idCarrier).get().getOrdersCarrier()) {
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
    public List<Orders> getOrdersByDriver(Long idDriver) {
        return ordersRepository.findAll().stream().filter(orders -> orders.getDriver().getUserNumber()==idDriver).toList();
    }

    @Override
    public List<Orders> getOrdersTodayBydriver(Long idDriver) {
        List<Orders> ordersList = new ArrayList<>();


        for (Orders orders1 : ordersRepository.findAll().stream().filter(orders -> orders.getDriver().getUserNumber()==idDriver).toList()) {
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


}
