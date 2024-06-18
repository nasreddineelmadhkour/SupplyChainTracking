package com.pgsintl.supplychaintracking;

import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Repository.ReclamationRepository;
import com.pgsintl.supplychaintracking.Services.ReclamationIService;
import com.pgsintl.supplychaintracking.Services.ReclamationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SupplyChainTrackingApplicationTests {


    @Mock
    private ReclamationRepository reclamationRepository;
    @InjectMocks
    private ReclamationService reclamationService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void retrieveAllReclamations() {
        List<Reclamation> expectedReclamations = new ArrayList<>();

        when(reclamationRepository.findAll()).thenReturn(expectedReclamations);
        List<Reclamation> actualReclamations = reclamationService.getAll();

        assertEquals(expectedReclamations, actualReclamations);
        verify(reclamationRepository, times(1)).findAll();
    }

}
