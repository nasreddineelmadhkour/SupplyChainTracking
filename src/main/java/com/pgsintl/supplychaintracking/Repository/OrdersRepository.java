package com.pgsintl.supplychaintracking.Repository;

import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Entities.StatusOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,Long> {

    List<Orders> findByReclamationIsNotNullAndCarrierUserNumber(Long idCarrier);

    List<Orders> findByReclamationIsNotNullAndDriver_UserNumber(Long idDriver);

    List<Orders> findByDateOrdersIsBetweenAndStatus(Date dateAfter,Date dateBefore, StatusOrders statusOrders);

}
