package com.pgsintl.supplychaintracking.controllers;

import com.pgsintl.supplychaintracking.entities.Reclamation;
import com.pgsintl.supplychaintracking.services.ReclamationIService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reclamation") @CrossOrigin("*") @AllArgsConstructor
public class ReclamationController {

    ReclamationIService reclamationIService;


    @PostMapping("/addReclamation/{idOrders}")
    public Reclamation addReclamation(@RequestBody Reclamation reclamation, @PathVariable Long idOrders) {
        return reclamationIService.addReclamation(reclamation,idOrders);
    }

}
