package com.pgsintl.supplychaintracking.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class OrdersTrackingDto {

    Long idOrders;
    double ordersNowLat;
    double ordersNowLong;
}
