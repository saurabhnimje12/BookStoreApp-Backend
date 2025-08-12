package com.example.service;

import com.example.dto.DtoToAddressEntity;
import com.example.entity.*;
import com.example.repo.CartRepo;
import com.example.repo.OrderRepo;
import com.example.repo.UserRepo;
import com.example.serviceImpl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private CartRepo cartRepo;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User mockUser;
    private Book mockBook;
    private Cart mockCart;
    private Order mockOrder;
    private DtoToAddressEntity dtoAddress;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setUserId(1L);

        mockBook = new Book();
        mockBook.setBookPrice(100.0);

        mockCart = new Cart();
        mockCart.setBook(mockBook);
        mockCart.setCartQuantity(2);
        mockCart.setUser(mockUser);

        mockOrder = new Order();
        mockOrder.setUser(mockUser);
        mockOrder.setCancelOrder(false);

        dtoAddress = new DtoToAddressEntity();
        dtoAddress.setName("John");
        dtoAddress.setCity("City");
        dtoAddress.setAddress("123 Street");
        dtoAddress.setPhoneNumber("9999999999");
        dtoAddress.setPinCode("123456");
        dtoAddress.setLocality("Locality");
        dtoAddress.setLandmark("Near Park");
    }

    // -------- placeOrder --------
    @Test
    void testPlaceOrder_HappyPath() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(cartRepo.findByUser(mockUser)).thenReturn(List.of(mockCart));

        Order savedOrder = orderService.placeOrder(1L, dtoAddress);

        assertEquals(mockUser, savedOrder.getUser());
        assertEquals(200.0, savedOrder.getOrderPrice()); // 100 * 2
        assertEquals(1, savedOrder.getOrderQuantity());
        assertNotNull(savedOrder.getAddress());
        verify(orderRepo, times(1)).save(any(Order.class));
        verify(cartRepo, times(1)).deleteAll(anyList());
    }

    // -------- cancelOrder --------
    @Test
    void testCancelOrder_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        String result = orderService.cancelOrder(1L, 2L);

        assertEquals("Enter a Valid Token", result);
        verify(orderRepo, never()).findById(anyLong());
    }

    @Test
    void testCancelOrder_OrderNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(orderRepo.findById(2L)).thenReturn(Optional.empty());

        String result = orderService.cancelOrder(1L, 2L);

        assertEquals("Order NOT Found with ID : 2", result);
    }

    @Test
    void testCancelOrder_NotEligibleUser() {
        User otherUser = new User();
        otherUser.setUserId(99L);
        mockOrder.setUser(otherUser);

        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(orderRepo.findById(2L)).thenReturn(Optional.of(mockOrder));

        String result = orderService.cancelOrder(1L, 2L);

        assertEquals("NOT Eligible to CANCEL the Order!!", result);
    }

    @Test
    void testCancelOrder_Success() {
        mockOrder.setUser(mockUser);

        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(orderRepo.findById(2L)).thenReturn(Optional.of(mockOrder));

        String result = orderService.cancelOrder(1L, 2L);

        assertEquals("Order Cancel Successfully with ID : 2", result);
        assertTrue(mockOrder.getCancelOrder());
        verify(orderRepo, times(1)).save(mockOrder);
    }

    // -------- getAllOrders --------
    @Test
    void testGetAllOrders_NoOrders() {
        Order cancelledOrder = new Order();
        cancelledOrder.setCancelOrder(true);

        when(orderRepo.findAll()).thenReturn(List.of(cancelledOrder));

        Object result = orderService.getAllOrders(1L);

        assertEquals("No Order Is Present!!", result);
    }

    @Test
    void testGetAllOrders_WithOrders() {
        when(orderRepo.findAll()).thenReturn(List.of(mockOrder));

        Object result = orderService.getAllOrders(1L);

        assertTrue(result instanceof List);
        assertEquals(1, ((List<?>) result).size());
    }
}
