package com.example.controller;

import com.example.dto.CartEntityToDto;
import com.example.dto.DtoToCartEntity;
import com.example.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartJwtControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartJwtController cartJwtController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- addToCart ----------
    @Test
    void testAddToCart_UserRole() {
        DtoToCartEntity dto = new DtoToCartEntity();
        when(cartService.addToCart(1L, 2L, dto)).thenReturn("Added");

        ResponseEntity<String> response = cartJwtController.addToCart("USER", 1L, 2L, dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Added", response.getBody());
        verify(cartService).addToCart(1L, 2L, dto);
    }

    @Test
    void testAddToCart_NotUserRole() {
        ResponseEntity<String> response = cartJwtController.addToCart("ADMIN", 1L, 2L, new DtoToCartEntity());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Token is NOT Valid to Add To Cart Book", response.getBody());
        verifyNoInteractions(cartService);
    }

    // ---------- addTooCart ----------
    @Test
    void testAddTooCart_UserRole() {
        when(cartService.addTooCart(1L, 2L)).thenReturn("Added");

        ResponseEntity<String> response = cartJwtController.addTooCart("USER", 1L, 2L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Added", response.getBody());
        verify(cartService).addTooCart(1L, 2L);
    }

    @Test
    void testAddTooCart_NotUserRole() {
        ResponseEntity<String> response = cartJwtController.addTooCart("ADMIN", 1L, 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoInteractions(cartService);
    }

    // ---------- removeFromCart ----------
    @Test
    void testRemoveFromCart_UserRole() {
        when(cartService.removeFromCart(1L, 3L)).thenReturn("Removed");

        ResponseEntity<String> response = cartJwtController.removeFromCart("USER", 1L, 3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Removed", response.getBody());
        verify(cartService).removeFromCart(1L, 3L);
    }

    @Test
    void testRemoveFromCart_NotUserRole() {
        ResponseEntity<String> response = cartJwtController.removeFromCart("ADMIN", 1L, 3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoInteractions(cartService);
    }

    // ---------- removeByUserID ----------
    @Test
    void testRemoveByUserID_UserRole() {
        when(cartService.removeByUserID(1L)).thenReturn("Removed All");

        ResponseEntity<String> response = cartJwtController.removeByUserID("USER", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Removed All", response.getBody());
        verify(cartService).removeByUserID(1L);
    }

    @Test
    void testRemoveByUserID_NotUserRole() {
        ResponseEntity<String> response = cartJwtController.removeByUserID("ADMIN", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoInteractions(cartService);
    }

    // ---------- updateQuantityInCart ----------
    @Test
    void testUpdateQuantityInCart_UserRole() {
        when(cartService.updateQuantityInCart(1L, 3L, 5)).thenReturn("Updated");

        ResponseEntity<String> response = cartJwtController.updateQuantityInCart("USER", 1L, 3L, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated", response.getBody());
        verify(cartService).updateQuantityInCart(1L, 3L, 5);
    }

    @Test
    void testUpdateQuantityInCart_NotUserRole() {
        ResponseEntity<String> response = cartJwtController.updateQuantityInCart("ADMIN", 1L, 3L, 5);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoInteractions(cartService);
    }

    // ---------- updateQuantityInCartAdd ----------
    @Test
    void testUpdateQuantityInCartAdd_UserRole() {
        when(cartService.updateQuantityInCartAdd(1L, 3L)).thenReturn("Added Qty");

        ResponseEntity<String> response = cartJwtController.updateQuantityInCartAdd("USER", 1L, 3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Added Qty", response.getBody());
        verify(cartService).updateQuantityInCartAdd(1L, 3L);
    }

    @Test
    void testUpdateQuantityInCartAdd_NotUserRole() {
        ResponseEntity<String> response = cartJwtController.updateQuantityInCartAdd("ADMIN", 1L, 3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoInteractions(cartService);
    }

    // ---------- updateQuantityInCartRmv ----------
    @Test
    void testUpdateQuantityInCartRmv_UserRole() {
        when(cartService.updateQuantityInCartRmv(1L, 3L)).thenReturn("Removed Qty");

        ResponseEntity<String> response = cartJwtController.updateQuantityInCartRmv("USER", 1L, 3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Removed Qty", response.getBody());
        verify(cartService).updateQuantityInCartRmv(1L, 3L);
    }

    @Test
    void testUpdateQuantityInCartRmv_NotUserRole() {
        ResponseEntity<String> response = cartJwtController.updateQuantityInCartRmv("ADMIN", 1L, 3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoInteractions(cartService);
    }

    // ---------- getAllCartItemsByUserID ----------
    @Test
    void testGetAllCartItemsByUserID_UserRole() {
        List<CartEntityToDto> carts = Arrays.asList(new CartEntityToDto());
        when(cartService.getAllCartItemsByUserID(1L)).thenReturn(carts);

        ResponseEntity<?> response = cartJwtController.getAllCartItemsByUserID("USER", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carts, response.getBody());
        verify(cartService).getAllCartItemsByUserID(1L);
    }

    @Test
    void testGetAllCartItemsByUserID_NotUserRole() {
        ResponseEntity<?> response = cartJwtController.getAllCartItemsByUserID("ADMIN", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoInteractions(cartService);
    }

    // ---------- getAllCarts ----------
    @Test
    void testGetAllCarts_AdminRole() {
        List<CartEntityToDto> carts = Arrays.asList(new CartEntityToDto());
        when(cartService.getAllCarts()).thenReturn(carts);

        ResponseEntity<?> response = cartJwtController.getAllCarts("ADMIN");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carts, response.getBody());
        verify(cartService).getAllCarts();
    }

    @Test
    void testGetAllCarts_NotAdminRole() {
        ResponseEntity<?> response = cartJwtController.getAllCarts("USER");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoInteractions(cartService);
    }
}
