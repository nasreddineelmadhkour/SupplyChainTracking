package com.pgsintl.supplychaintracking;

import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private Account driver;
    @Mock
    private MultipartFile file;
    @Mock
    private MultipartFile multipartFile;
    @InjectMocks
    private OrdersService ordersService;
    private Account account;
    private Orders order1;
    private Orders order2;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        reclamationRepository.deleteAll();
        account = new Account();
        account.setUserNumber(1L);
        account.setName("John Doe");
        account.setPassword("password");
        account.setRole(Role.CARRIER);
        account = new Account();
        order1 = new Orders();
        order2 = new Orders();
        account.setOrdersCarrier(List.of(order1, order2));
    }


    // Tests for Account
    @Test
    void testCreateAccountCarrier() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.creatAccountCarrier(account);

        assertEquals(Role.CARRIER, savedAccount.getRole());
        assertNotNull(savedAccount.getDatecreation());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testCreateAccountDriver() throws IOException {
        Account driver = new Account();
        driver.setUserNumber(1L);
        driver.setRole(Role.DRIVER);  // Ensure the role is set to DRIVER
        driver.setDatecreation(new Date());
        driver.setNamePhoto("photo.jpg");
        driver.setTypePhoto("image/jpeg");
        driver.setPassword(passwordEncoder.encode("password"));
        Account carrier = new Account();
        carrier.setUserNumber(2L);
        carrier.setDrivers(new ArrayList<>());

        when(accountRepository.findById(2L)).thenReturn(Optional.of(carrier));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(multipartFile.getOriginalFilename()).thenReturn("photo.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getBytes()).thenReturn(new byte[10]);
        when(accountRepository.save(any(Account.class))).thenReturn(driver);

        Account savedAccount = accountService.creatAccountDriver("Driver", "password", "driver@example.com", "1234", "5678", "1234567890", multipartFile, 2L);

        assertEquals(Role.DRIVER, savedAccount.getRole());
        assertNotNull(savedAccount.getDatecreation());
        assertEquals("photo.jpg", savedAccount.getNamePhoto());
        assertEquals("image/jpeg", savedAccount.getTypePhoto());
        assertEquals(1, carrier.getDrivers().size());  // Ensure the driver is added to the carrier's drivers list
    }

    @Test
    void testGetAllUser() {
        Account account = new Account();
        account.setPhoto(new byte[10]);
        List<Account> accounts = Collections.singletonList(account);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountLoginDto> accountLoginDtos = accountService.getAllUser();

        assertEquals(1, accountLoginDtos.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testGetAllDriverByCarrier() {
        Account carrier = new Account();
        carrier.setDrivers(new ArrayList<>());
        Account driver = new Account();
        driver.setPhoto(new byte[10]);
        carrier.getDrivers().add(driver);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(carrier));

        List<Account> drivers = accountService.getAllDriverByCarrier(1L);

        assertEquals(1, drivers.size());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testSetAllNoPDP() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("photo.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getBytes()).thenReturn(new byte[10]);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountRepository.findAll()).thenReturn(accounts);

        boolean result = accountService.setAllNoPDP(multipartFile);

        assertTrue(result);
        verify(accountRepository, times(1)).findAll();
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testSendCodeReset() {
        when(accountRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(account));
        doAnswer(invocation -> {
            account.setCodeTel("123456");
            return null;
        }).when(accountRepository).save(any(Account.class));

        boolean result = accountService.sendCodeReset("1234567890");

        assertTrue(result);
        assertEquals("123456", account.getCodeTel());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testVerifyCode() {
        account.setCodeTel("123456");
        when(accountRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(account));

        boolean result = accountService.verifyCode("123456", "1234567890");

        assertTrue(result);
        assertNull(account.getCodeTel());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testChangePasswordAfterVerification() {
        when(accountRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(account));
        when(passwordEncoder.encode(any(String.class))).thenReturn("newEncodedPassword");

        boolean result = accountService.changePasswordAfterVerification("newPassword", "1234567890");

        assertTrue(result);
        assertEquals("newEncodedPassword", account.getPassword());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testDeleteDriver() {
        doNothing().when(accountRepository).deleteById(1L);

        boolean result = accountService.deleteDriver(1L);

        assertTrue(result);
        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateProfile() throws IOException {
        account.setPhoto(new byte[10]);
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(account));
        when(multipartFile.getOriginalFilename()).thenReturn("photo.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getBytes()).thenReturn(new byte[10]);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.updateProfile(1L, multipartFile, "New Name", "1234567890", "new@example.com", "newPassword", "true", "true", "true", "true", "true");

        assertEquals("New Name", updatedAccount.getName());
        assertEquals("1234567890", updatedAccount.getPhoneNumber());
        assertEquals("new@example.com", updatedAccount.getEmail());
        assertEquals("encodedPassword", updatedAccount.getPassword());
        assertEquals("photo.jpg", updatedAccount.getNamePhoto());
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



    @Test
    void testGetAllOrderByCarrier() {
        order1.setDateOrders(getDateMinusDays(1));
        order2.setDateOrders(new Date());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        List<Orders> result = ordersService.getAllOrderByCarrier(1L);

        assertEquals(1, result.size());
        assertEquals(order1, result.get(0));
    }

    @Test
    void testGetOrdersTodayByCarrier() {
        order1.setDateOrders(getDateMinusDays(1));
        order2.setDateOrders(new Date());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        List<Orders> result = ordersService.getOrdersTodayBycarrier(1L);

        assertEquals(1, result.size());
        assertEquals(order2, result.get(0));
    }

    @Test
    void testGetOrdersByDriver() {
        Account driver = new Account();
        driver.setUserNumber(1L);
        order1.setDriver(driver);
        order2.setDriver(driver);
        order1.setDateOrders(getDateMinusDays(1));
        order2.setDateOrders(new Date());

        when(ordersRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Orders> result = ordersService.getOrdersByDriver(1L);

        assertEquals(1, result.size());
        assertEquals(order1, result.get(0));
    }

    @Test
    void testGetOrdersTodayByDriver() {
        Account driver = new Account();
        driver.setUserNumber(1L);
        order1.setDriver(driver);
        order2.setDriver(driver);
        order1.setDateOrders(getDateMinusDays(1));
        order2.setDateOrders(new Date());

        when(ordersRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Orders> result = ordersService.getOrdersTodayBydriver(1L);

        assertEquals(1, result.size());
        assertEquals(order2, result.get(0));
    }

    private Date getDateMinusDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return cal.getTime();
    }

}
