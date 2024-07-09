package com.pgsintl.supplychaintracking.repository;

import com.pgsintl.supplychaintracking.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
