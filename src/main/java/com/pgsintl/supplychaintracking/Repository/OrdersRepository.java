package com.pgsintl.supplychaintracking.Repository;

import com.pgsintl.supplychaintracking.Entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
