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


    // Test Account
    @Test
    void testCreatAccountCarrier() {
        Account carrier = new Account();
        carrier.setPassword("plainPassword");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(carrier);

        Account result = accountService.CreatAccountCarrier(carrier);

        assertEquals(Role.CARRIER, result.getRole());
        assertEquals("encodedPassword", result.getPassword());
        assertNotNull(result.getDatecreation());
        verify(accountRepository, times(1)).save(carrier);
    }

    @Test
    void testCreatAccountDriver() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("photo.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Account carrier = new Account();
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(carrier));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());

        Account result = accountService.CreatAccountDriver("driverName", "password", "email", "cardNumber", "serialNumber", "phoneNumber", file, 1L);

        assertEquals(Role.DRIVER, result.getRole());
        assertEquals("encodedPassword", result.getPassword());
        assertNotNull(result.getDatecreation());
        verify(accountRepository, times(1)).save(result);
    }

    @Test
    void testGetAllUser() {
        Account account = new Account();
        account.setPhoto(new byte[]{1, 2, 3});
        List<Account> accountList = Collections.singletonList(account);
        when(accountRepository.findAll()).thenReturn(accountList);

        List<AccountLoginDto> result = accountService.getAllUser();

        assertEquals(1, result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testSetAllNoPDP() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("photo.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Account account = new Account();
        List<Account> accountList = Collections.singletonList(account);
        when(accountRepository.findAll()).thenReturn(accountList);

        boolean result = accountService.SetAllNoPDP(file);

        assertTrue(result);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testSendCodeReset() {
        Account account = new Account();
        when(accountRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(account));

        boolean result = accountService.SendCodeReset("phoneNumber");

        assertTrue(result);
        assertNotNull(account.getCodeTel());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testVerifyCode() {
        Account account = new Account();
        account.setCodeTel("123456");
        when(accountRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(account));

        boolean result = accountService.verifyCode("123456", "phoneNumber");

        assertTrue(result);
        assertNull(account.getCodeTel());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testChangePasswordAfterVerification() {
        Account account = new Account();
        when(accountRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(account));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        boolean result = accountService.ChangePasswordAfterVerification("newPassword", "phoneNumber");

        assertTrue(result);
        assertEquals("encodedPassword", account.getPassword());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testDeleteDriver() {
        doNothing().when(accountRepository).deleteById(any(Long.class));

        boolean result = accountService.deleteDriver(1L);

        assertTrue(result);
        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateProfile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("photo.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Account account = new Account();
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(account));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        Account result = accountService.updateProfile(1L, file, "name", "phoneNumber", "email", "password", "true", "true", "true", "true", "true");

        assertNotNull(result);
        verify(accountRepository, times(2)).save(account);
    }

}
