package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Dto.OrdersTrackingDto;
import com.pgsintl.supplychaintracking.Entities.Orders;

import java.util.List;

public interface OrdersIService {
    Orders addOrder(Orders orders, Long idCarrier, Long idDriver) ;

    List<Orders> getAllOrders();

    List<Orders> getAllOrderByDriver(Long idDriver);

    List<Orders> getAllOrderByCarrier(Long idCarrier);


    List<Orders> getOrderByCarrier(Long idCarrier);
    List<Orders> getOrdersTodayBycarrier(Long idCarrier);

    List<Orders> getOrdersByDriver(Long idDriver);

    List<Orders> getOrdersTodayBydriver(Long idDriver);

    void updatePosition(OrdersTrackingDto ordersTrackingDto);

    void startingOrders(Long idOrders);

    boolean deleteOrders(Long idOrders);

   // Orders updateOrders(Long orderId, Orders updatedOrders,Long idDriver,int isS,int isA);

    void completedOrders(Long idOrders);
}
