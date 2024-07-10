package com.pgsintl.supplychaintracking.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrdersTrackingDto {

    Long idOrders;
    double ordersNowLat;
    double ordersNowLong;
}
