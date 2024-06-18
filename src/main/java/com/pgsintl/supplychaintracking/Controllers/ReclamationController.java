package com.pgsintl.supplychaintracking.Controllers;

import com.pgsintl.supplychaintracking.Entities.Photo;
import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Repository.PhotoRepository;
import com.pgsintl.supplychaintracking.Services.PhotoIService;
import com.pgsintl.supplychaintracking.Services.ReclamationIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/reclamation") @CrossOrigin("*")
public class ReclamationController {

    @Autowired
    ReclamationIService reclamationIService;

    @Autowired
    PhotoIService photoIService;

    @Autowired
    private PhotoRepository photoRepository;

    @PostMapping("/addReclamation/{idOrders}")
    public Reclamation addReclamation(@RequestBody Reclamation reclamation, @PathVariable Long idOrders) {
        return reclamationIService.addReclamation(reclamation,idOrders);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = photoIService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData=photoIService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

}
