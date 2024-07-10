package com.pgsintl.supplychaintracking.controllers;

import com.pgsintl.supplychaintracking.entities.Reclamation;
import com.pgsintl.supplychaintracking.services.ReclamationIService;
import lombok.AllArgsConstructor;
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
