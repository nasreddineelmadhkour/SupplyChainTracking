package com.pgsintl.supplychaintracking.services;

import com.pgsintl.supplychaintracking.dto.OrdersTrackingDto;
import com.pgsintl.supplychaintracking.entities.Orders;

import java.util.List;

public interface OrdersIService {
    public Orders addOrder(Orders orders, Long idCarrier, Long idDriver) ;

    List<Orders> getAllOrders();

    List<Orders> getAllOrderByCarrier(Long idCarrier);

    List<Orders> getOrdersTodayBycarrier(Long idCarrier);

    List<Orders> getOrdersByDriver(Long idDriver);

    List<Orders> getOrdersTodayBydriver(Long idDriver);

    public void updatePosition(OrdersTrackingDto ordersTrackingDto);

    public void startingOrders(Long idOrders);

    public boolean deleteOrders(Long idOrders);

    Orders updateOrders(Long orderId, Orders updatedOrders);
}
