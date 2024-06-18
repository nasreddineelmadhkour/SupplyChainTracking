package com.pgsintl.supplychaintracking;

import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.Orders;
import com.pgsintl.supplychaintracking.Entities.Reclamation;
import com.pgsintl.supplychaintracking.Entities.Role;
import com.pgsintl.supplychaintracking.Repository.AccountRepository;
import com.pgsintl.supplychaintracking.Repository.OrdersRepository;
import com.pgsintl.supplychaintracking.Repository.ReclamationRepository;
import com.pgsintl.supplychaintracking.Services.AccountService;
import com.pgsintl.supplychaintracking.Services.OrdersService;
import com.pgsintl.supplychaintracking.Services.ReclamationIService;
import com.pgsintl.supplychaintracking.Services.ReclamationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SupplyChainTrackingApplicationTests {

    @Mock
    private AccountService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private ReclamationRepository reclamationRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private OrdersService ordersService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Tests for addOrder method

    @Test
    public void testAddOrder() {
        // Mock data
        Orders orders = new Orders();
        orders.setOrdersNumber(1L);
        Long idCarrier = 1L;
        Long idDriver = 2L;
        Account mockCarrier = new Account();
        mockCarrier.setUserNumber(idCarrier);
        Account mockDriver = new Account();
        mockDriver.setUserNumber(idDriver);

        // Mock repository behavior
        when(accountRepository.findById(idCarrier)).thenReturn(Optional.of(mockCarrier));
        when(accountRepository.findById(idDriver)).thenReturn(Optional.of(mockDriver));
        when(ordersRepository.save(any(Orders.class))).thenReturn(orders);

        // Perform service method
        Orders result = ordersService.addOrder(new Orders(), idCarrier, idDriver);

        // Verify interactions and assertions
        verify(accountRepository, times(2)).findById(anyLong());
        verify(ordersRepository, times(1)).save(any(Orders.class));
        assertEquals(1L, result.getOrdersNumber());
    }

    // Tests for getAllOrders method

    @Test
    public void testGetAllOrders() {
        // Mock repository behavior
        List<Orders> mockOrdersList = new ArrayList<>();
        when(ordersRepository.findAll()).thenReturn(mockOrdersList);

        // Perform service method
        List<Orders> result = ordersService.getAllOrders();

        // Verify interactions and assertions
        verify(ordersRepository, times(1)).findAll();
        assertEquals(mockOrdersList, result);
    }

    // Additional tests can be added for other methods similarly

}
