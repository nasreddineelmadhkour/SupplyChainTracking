package com.pgsintl.supplychaintracking.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "orders")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long ordersNumber;

    @ManyToOne @JoinColumn(name = "driverNumber")
    Account driver;

    @ManyToOne @JoinColumn(name = "carrierNumber")
    Account carrier ;

    @OneToOne(cascade = CascadeType.ALL) @JoinColumn(name = "reclamationNumber")
    Reclamation reclamation;

    Date dateOrders;
    Date dateFinOrders;
    String productOrders;
    int weightOrders;
    String unitProduct;
    String startingPoint;
    String arrivalPoint;

    double startingLong;
    double startingLat;

    double arrivalLong;
    double arrivalLat;

    double ordersNowLat;
    double ordersNowLong;

    String estimation;
    String distance;

    @Enumerated(EnumType.STRING)
    StatusOrders status;



}

