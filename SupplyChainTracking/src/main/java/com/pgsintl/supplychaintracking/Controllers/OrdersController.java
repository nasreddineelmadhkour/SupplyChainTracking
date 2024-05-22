package com.pgsintl.supplychaintracking.Controllers;

import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Services.OrdersIService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders") @CrossOrigin("*")
public class OrdersController {

    @Autowired
    private OrdersIService ordersIService;

    @PostMapping("/addOrder/{idCarrier}/{idDriver}")
    @PreAuthorize("hasAuthority('CARRIER')")
    public Orders addOrder(@RequestBody Orders orders, @PathVariable  Long idCarrier, @PathVariable Long idDriver) {



        return ordersIService.addOrder(orders,idCarrier,idDriver);
    }

    @GetMapping("/ordersByCarrier/{idCarrier}")
    @PreAuthorize("hasAuthority('CARRIER')")
    public List<Orders> getOrdersByCarrier(@PathVariable Long idCarrier)
    {
        return ordersIService.getAllOrderByCarrier(idCarrier);
    }

    @GetMapping("/admin")
    public List<Orders> getAllOrder(){
        return ordersIService.getAllOrders();
    }


    @GetMapping("/orderTodayByCarrier/{idCarrier}")
    @PreAuthorize("hasAuthority('CARRIER')")
    public List<Orders> getOrdersTodayBycarrier(@PathVariable Long idCarrier){
        return ordersIService.getOrdersTodayBycarrier(idCarrier);
    }



    @GetMapping("/ordersByDriver/{idDriver}")
    @PreAuthorize("hasAuthority('DRIVER')")
    public List<Orders> getOrdersByDriver(@PathVariable Long idDriver)
    {
        return ordersIService.getOrdersByDriver(idDriver);
    }

    @GetMapping("/orderTodayByDriver/{idDriver}")
    @PreAuthorize("hasAuthority('DRIVER')")
    public List<Orders> getOrdersTodayBydriver(@PathVariable Long idDriver){
        return ordersIService.getOrdersTodayBydriver(idDriver);
    }



}
