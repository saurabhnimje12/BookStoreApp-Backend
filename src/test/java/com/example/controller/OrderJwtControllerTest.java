package com.example.controller;

import com.example.dto.DtoToAddressEntity;
import com.example.entity.Order;
import com.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderJwtControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderJwtController orderJwtController;

    private DtoToAddressEntity addressEntity;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressEntity = new DtoToAddressEntity();
        mockOrder = new Order();
    }

    // ---------------- placeOrder ----------------
    @Test
    void testPlaceOrder_WithUserRole_ReturnsCreatedOrder() {
        Long userId = 1L;
        when(orderService.placeOrder(userId, addressEntity)).thenReturn(mockOrder);

        ResponseEntity<?> response = orderJwtController.placeOrder("USER", userId, addressEntity);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).placeOrder(userId, addressEntity);
    }

    @Test
    void testPlaceOrder_WithNonUserRole_ReturnsNotFound() {
        ResponseEntity<?> response = orderJwtController.placeOrder("ADMIN", 1L, addressEntity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Token is NOT Valid to Add To Cart Book!!", response.getBody());
        verify(orderService, never()).placeOrder(anyLong(), any());
    }

    // ---------------- cancelOrder ----------------
    @Test
    void testCancelOrder_WithUserRole_ReturnsCreatedMessage() {
        Long userId = 1L;
        Long orderId = 2L;
        when(orderService.cancelOrder(userId, orderId)).thenReturn("Order Cancelled");

        ResponseEntity<String> response = orderJwtController.cancelOrder("USER", userId, orderId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order Cancelled", response.getBody());
        verify(orderService, times(1)).cancelOrder(userId, orderId);
    }

    @Test
    void testCancelOrder_WithNonUserRole_ReturnsNotFound() {
        ResponseEntity<String> response = orderJwtController.cancelOrder("ADMIN", 1L, 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Token is NOT Valid to Cancel Order from Cart!!", response.getBody());
        verify(orderService, never()).cancelOrder(anyLong(), anyLong());
    }

    // ---------------- getAllOrders ----------------
    @Test
    void testGetAllOrders_WithAdminRole_ReturnsCreatedList() {
        Long userId = 1L;
        when(orderService.getAllOrders(userId)).thenReturn("All Orders Data");

        ResponseEntity<?> response = orderJwtController.getAllOrders("ADMIN", userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("All Orders Data", response.getBody());
        verify(orderService, times(1)).getAllOrders(userId);
    }

    @Test
    void testGetAllOrders_WithNonAdminRole_ReturnsNotFound() {
        ResponseEntity<?> response = orderJwtController.getAllOrders("USER", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Token is NOT Valid to Get All Cart!!", response.getBody());
        verify(orderService, never()).getAllOrders(anyLong());
    }
}
