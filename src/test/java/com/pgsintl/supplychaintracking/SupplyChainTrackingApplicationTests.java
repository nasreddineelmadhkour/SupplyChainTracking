package com.pgsintl.supplychaintracking;

import com.pgsintl.supplychaintracking.Dto.OrdersTrackingDto;
import com.pgsintl.supplychaintracking.Entities.*;
import com.pgsintl.supplychaintracking.Repository.AccountRepository;
import com.pgsintl.supplychaintracking.Repository.OrdersRepository;
import com.pgsintl.supplychaintracking.Repository.ReclamationRepository;
import com.pgsintl.supplychaintracking.Services.AccountService;
import com.pgsintl.supplychaintracking.Services.OrdersService;
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

    @InjectMocks
    private AccountService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private ReclamationRepository reclamationRepository;

    @Mock
    private AccountRepository accountRepository;

    private Account carrier;

    @InjectMocks
    private OrdersService ordersService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        reclamationRepository.deleteAll();

    }


    // Tests for Account

    @Test
    void testCreatAccountCarrier() {
        carrier = new Account();
        carrier.setUserNumber(1L);
        carrier.setPhoneNumber("11223366");
        carrier.setPassword("plainPassword");
        // Mock password encoding
        when(passwordEncoder.encode(carrier.getPassword())).thenReturn("encodedPassword");

        // Mock repository save method
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        Account createdCarrier = accountService.creatAccountCarrier(carrier);

        // Assertions
        assertEquals(Role.CARRIER, createdCarrier.getRole());
        assertEquals("encodedPassword", createdCarrier.getPassword());
        assertEquals(carrier.getPhoneNumber(), createdCarrier.getPhoneNumber());
        assertEquals(carrier.getUserNumber(), createdCarrier.getUserNumber());
        assertEquals(carrier.getDatecreation().getTime(), createdCarrier.getDatecreation().getTime(), 1000); // Allowing 1-second difference
    }



    // Tests for addOrder method

    @Test
    void testAddOrder() {
        // Mock data
        Orders orders = new Orders();
        orders.setOrdersNumber(1L);
        orders.setOrdersNowLat(25.30);
        orders.setOrdersNowLong(15.33);

        orders.setStatus(StatusOrders.PENDING);
        orders.setDateOrders(new Date());
        orders.setDateFinOrders(new Date());
        orders.setProductOrders("Gasoline");
        orders.setWeightOrders(35000);
        orders.setUnitProduct("litre");
        orders.setEstimation("35 MIN");
        orders.setDistance("152 KM");
        orders.setArrivalPoint("Ariana");
        orders.setStartingPoint("Seliena");

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
    void testGetAllOrders() {
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
    void testUpdatePosition_WithValidData() {
        Long orderId = 1L;
        Orders existingOrder = new Orders();
        existingOrder.setOrdersNumber(orderId);
        existingOrder.setOrdersNowLat(0.0);
        existingOrder.setOrdersNowLong(0.0);

        OrdersTrackingDto ordersTrackingDto = new OrdersTrackingDto();
        ordersTrackingDto.setIdOrders(orderId);
        ordersTrackingDto.setOrdersNowLat(10.0);
        ordersTrackingDto.setOrdersNowLong(20.0);

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(ordersRepository.save(any(Orders.class))).thenReturn(existingOrder);

        ordersService.updatePosition(ordersTrackingDto);

        verify(ordersRepository).save(existingOrder);
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




    @Test
    void testReclamationCreation() {
        Reclamation reclamation = Reclamation.builder()
                .reclamationNumber(1L)
                .description("Description of the reclamation")
                .dateReclamation(new Date())
                .statusReclamation(StatusReclamation.NOT_RESOLVED)
                .build();

        assertEquals(1L, reclamation.getReclamationNumber());
        assertEquals("Description of the reclamation", reclamation.getDescription());
        assertEquals(StatusReclamation.NOT_RESOLVED, reclamation.getStatusReclamation());
    }


    @Test
    void testSaveAndFindById() {
        Reclamation reclamation = Reclamation.builder()
                .reclamationNumber(1L)
                .description("Test Reclamation")
                .dateReclamation(new Date())
                .statusReclamation(StatusReclamation.NOT_RESOLVED)
                .build();

        // Mock the save method to return the reclamation object
        when(reclamationRepository.save(any(Reclamation.class))).thenReturn(reclamation);

        // Save the reclamation
        Reclamation savedReclamation = reclamationRepository.save(reclamation);

        // Mock the findById method to return the saved reclamation
        when(reclamationRepository.findById(savedReclamation.getReclamationNumber())).thenReturn(Optional.of(savedReclamation));

        // Find the reclamation by ID
        Optional<Reclamation> foundReclamation = reclamationRepository.findById(savedReclamation.getReclamationNumber());

        // Assertions to validate the result
        assertTrue(foundReclamation.isPresent());
        assertEquals(savedReclamation.getReclamationNumber(), foundReclamation.get().getReclamationNumber());
        assertEquals("Test Reclamation", foundReclamation.get().getDescription());
        assertEquals(StatusReclamation.NOT_RESOLVED, foundReclamation.get().getStatusReclamation());
    }

    @Test
    void testFindByStatusReclamation() {
        // Ensure you are using the correct status
        StatusReclamation status = StatusReclamation.NOT_RESOLVED;

        // Create a new Reclamation object
        Reclamation reclamation = Reclamation.builder()
                .description("Test Reclamation")
                .dateReclamation(new Date())
                .statusReclamation(status)
                .build();

        // Save the reclamation to the repository
        reclamationRepository.save(reclamation);

        // Fetch reclamations with the status NOT_RESOLVED
        List<Reclamation> reclamations = reclamationRepository.findByStatusReclamation(status);

        // Assertions to validate the result
        assertNotNull(reclamations, "Reclamations list should not be null");
    }


}
