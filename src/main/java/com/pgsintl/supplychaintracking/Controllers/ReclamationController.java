package com.pgsintl.supplychaintracking.Controllers;

import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Services.OrdersIService;
import com.pgsintl.supplychaintracking.Services.ReclamationIService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reclamation")
@AllArgsConstructor
public class ReclamationController {
    private  ReclamationIService reclamationIService;
    private OrdersIService ordersIService;

    @PostMapping("/addReclamation/{idOrders}")
    public Reclamation addReclamation(@RequestBody Reclamation reclamation, @PathVariable Long idOrders) {
        return reclamationIService.addReclamation(reclamation,idOrders);
    }

    @GetMapping("/getClaimsByCarrier/{idCarrier}")
    public List<Orders> getClaimsByCarrier(@PathVariable Long idCarrier ){
        return ordersIService.getAllOrderByCarrier(idCarrier);
    }
    @GetMapping("/getClaimsByDriver/{idDriver}")
    public List<Orders> getClaimsByDriver(@PathVariable Long idDriver ){
        return ordersIService.getAllOrderByDriver(idDriver);
    }

    @PostMapping("/resolvedClaim/{idClaim}")
    public boolean resolvedClaim(@PathVariable Long idClaim){
        return reclamationIService.resolvedClaim(idClaim);
    }
}
