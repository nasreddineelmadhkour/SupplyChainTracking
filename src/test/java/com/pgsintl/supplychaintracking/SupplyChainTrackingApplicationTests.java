package com.pgsintl.supplychaintracking;

import com.pgsintl.supplychaintracking.dto.OrdersTrackingDto;
import com.pgsintl.supplychaintracking.entities.*;
import com.pgsintl.supplychaintracking.repository.AccountRepository;
import com.pgsintl.supplychaintracking.repository.OrdersRepository;
import com.pgsintl.supplychaintracking.repository.ReclamationRepository;
import com.pgsintl.supplychaintracking.services.AccountService;
import com.pgsintl.supplychaintracking.services.OrdersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

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

        @Test
    void testUpdatePosition() {
        // Mock data
        Long orderId = 1L;
        Orders existingOrder = new Orders();
        existingOrder.setOrdersNumber(orderId);
        existingOrder.setOrdersNowLat(0.0);
        existingOrder.setOrdersNowLong(0.0);

        OrdersTrackingDto ordersTrackingDto = new OrdersTrackingDto();
        ordersTrackingDto.setIdOrders(orderId);
        ordersTrackingDto.setOrdersNowLat(10.0);
        ordersTrackingDto.setOrdersNowLong(20.0);

        // Mock repository behavior
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(ordersRepository.save(any(Orders.class))).thenReturn(existingOrder);

        // Perform service method
        ordersService.updatePosition(ordersTrackingDto);

        // Verify interactions and assertions
        verify(ordersRepository, times(1)).findById(orderId);
        verify(ordersRepository, times(1)).save(existingOrder);
        assertEquals(10.0, existingOrder.getOrdersNowLat());
        assertEquals(20.0, existingOrder.getOrdersNowLong());
    }


    @Test
    void testUpdateOrders() {
        // Mock data
        Long orderId = 1L;
        Orders existingOrder = new Orders();
        existingOrder.setOrdersNumber(orderId);
        existingOrder.setArrivalLat(0.0);
        existingOrder.setArrivalLong(0.0);

        Orders updatedOrder = new Orders();
        updatedOrder.setOrdersNumber(orderId);
        updatedOrder.setArrivalLat(30.445);
        updatedOrder.setArrivalLong(650.22);
        updatedOrder.setStartingPoint("Makther");
        updatedOrder.setArrivalPoint("Manouba");
        updatedOrder.setDistance("169 KM");
        updatedOrder.setEstimation("2 H 30 MIN");
        updatedOrder.setWeightOrders(40000);
        updatedOrder.setUnitProduct("kg");
        updatedOrder.setProductOrders("Gasoline");
        updatedOrder.setStatus(StatusOrders.IN_PROGRESS);
        updatedOrder.setDateOrders(new Date());
        updatedOrder.setDateFinOrders(new Date());

        Long idCarrier = 1L;
        Long idDriver = 2L;
        Account mockCarrier = new Account();
        mockCarrier.setUserNumber(idCarrier);
        Account mockDriver = new Account();
        mockDriver.setUserNumber(idDriver);

        // Mock repository behavior
        when(accountRepository.findById(idCarrier)).thenReturn(Optional.of(mockCarrier));
        when(accountRepository.findById(idDriver)).thenReturn(Optional.of(mockDriver));
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(ordersRepository.save(any(Orders.class))).thenReturn(updatedOrder);

        // Set carrier and driver in the updated order
        updatedOrder.setDriver(mockDriver);
        updatedOrder.setCarrier(mockCarrier);

        // Perform service method
        Orders result = ordersService.updateOrders(orderId, updatedOrder);

        // Verify interactions and assertions
        verify(ordersRepository, times(1)).findById(orderId);
        verify(ordersRepository, times(1)).save(any(Orders.class));
        assertEquals(updatedOrder.getArrivalLat(), result.getArrivalLat());
        assertEquals(updatedOrder.getArrivalLong(), result.getArrivalLong());
        assertEquals(updatedOrder.getStartingPoint(), result.getStartingPoint());
        assertEquals(updatedOrder.getArrivalPoint(), result.getArrivalPoint());
        assertEquals(updatedOrder.getDistance(), result.getDistance());
        assertEquals(updatedOrder.getEstimation(), result.getEstimation());
        assertEquals(updatedOrder.getWeightOrders(), result.getWeightOrders());
        assertEquals(updatedOrder.getUnitProduct(), result.getUnitProduct());
        assertEquals(updatedOrder.getProductOrders(), result.getProductOrders());
        assertEquals(updatedOrder.getStatus(), result.getStatus());
        assertEquals(updatedOrder.getDateOrders(), result.getDateOrders());
        assertEquals(updatedOrder.getDateFinOrders(), result.getDateFinOrders());
    }

    @Test
    void testDeleteOrder() {
        // Mock data
        Long orderId = 1L;
        Orders mockOrder = new Orders();
        mockOrder.setOrdersNumber(orderId);

        // Mock repository behavior
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        doNothing().when(ordersRepository).deleteById(orderId);

        // Perform service method
        boolean result = ordersService.deleteOrders(orderId);

        // Verify interactions and assertions
        verify(ordersRepository, times(1)).findById(orderId);
        verify(ordersRepository, times(1)).deleteById(orderId);
        assertTrue(result);
    }






}
