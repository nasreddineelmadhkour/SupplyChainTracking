package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Entities.Orders;

import java.util.List;

public interface OrdersIService {
    public Orders addOrder(Orders orders, Long idCarrier, Long idDriver) ;

    List<Orders> getAllOrders();

    List<Orders> getAllOrderByCarrier(Long idCarrier);

    List<Orders> getOrdersTodayBycarrier(Long idCarrier);

    List<Orders> getOrdersByDriver(Long idDriver);

    List<Orders> getOrdersTodayBydriver(Long idDriver);
}
