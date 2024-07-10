package com.pgsintl.supplychaintracking.Controllers;

import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Services.ReclamationIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reclamation")
public class ReclamationController {
    @Autowired

    ReclamationIService reclamationIService;


    @PostMapping("/addReclamation/{idOrders}")
    public Reclamation addReclamation(@RequestBody Reclamation reclamation, @PathVariable Long idOrders) {
        return reclamationIService.addReclamation(reclamation,idOrders);
    }

}
