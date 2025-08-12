package com.example.service;

import com.example.dto.DtoToCartEntity;
import com.example.entity.Book;
import com.example.entity.Cart;
import com.example.entity.User;
import com.example.exception.CustomiseException;
import com.example.repo.BookRepo;
import com.example.repo.CartRepo;
import com.example.repo.UserRepo;
import com.example.serviceImpl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private BookRepo bookRepo;

    @Mock
    private CartRepo cartRepo;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Book book;
    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);

        book = new Book();
        book.setBookId(10L);
        book.setBookName("Test Book");
        book.setBookPrice(100.00);
        book.setBookQuantity(5);

        cart = new Cart();
        cart.setCartId(100L);
        cart.setUser(user);
        cart.setBook(book);
        cart.setCartQuantity(1);
        cart.setTotalPrice(100.00);
    }

    @Test
    void addToCart_success() {
        DtoToCartEntity dto = new DtoToCartEntity();
        dto.setCartQuantity(2);

        when(bookRepo.findById(10L)).thenReturn(Optional.of(book));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        String result = cartService.addToCart(1L, 10L, dto);
        assertEquals("Book Added to Cart Successfully!!", result);
        verify(cartRepo, times(1)).save(any(Cart.class));
    }

    @Test
    void addToCart_bookNotFound() {
        when(bookRepo.findById(10L)).thenReturn(Optional.empty());
        DtoToCartEntity dto = new DtoToCartEntity();
        assertThrows(CustomiseException.class, () -> cartService.addToCart(1L, 10L, dto));
    }

    @Test
    void addTooCart_success() {
        when(bookRepo.findById(10L)).thenReturn(Optional.of(book));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        String result = cartService.addTooCart(1L, 10L);
        assertEquals("Book Added to Cart Successfully!!", result);
        verify(cartRepo).save(any(Cart.class));
    }

    @Test
    void removeFromCart_success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepo.findById(100L)).thenReturn(Optional.of(cart));

        String result = cartService.removeFromCart(1L, 100L);
        assertEquals("Remove From Cart Successfully", result);
        verify(cartRepo).deleteById(100L);
    }

    @Test
    void removeFromCart_userNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        String result = cartService.removeFromCart(1L, 100L);
        assertEquals("USER NOT FOUND TO DELETE CART!!", result);
    }

    @Test
    void removeByUserID_success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepo.findByUser(user)).thenReturn(List.of(cart));

        String result = cartService.removeByUserID(1L);
        assertEquals("All Cart Deleted for User ID: 1", result);
    }

    @Test
    void updateQuantityInCart_invalidQuantity() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepo.findById(100L)).thenReturn(Optional.of(cart));
        book.setBookQuantity(5);
        cart.setBook(book);

        assertThrows(CustomiseException.class, () -> cartService.updateQuantityInCart(1L, 100L, 1));
    }

    @Test
    void getAllCartItemsByUserID_success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepo.findByUser(user)).thenReturn(List.of(cart));
        when(bookRepo.findById(10L)).thenReturn(Optional.of(book));

        assertFalse(cartService.getAllCartItemsByUserID(1L).isEmpty());
    }

    @Test
    void getAllCarts_success() {
        when(cartRepo.findAll()).thenReturn(List.of(cart));
        when(bookRepo.findById(10L)).thenReturn(Optional.of(book));

        assertFalse(cartService.getAllCarts().isEmpty());
    }

    @Test
    void getAllCarts_empty_throwsException() {
        when(cartRepo.findAll()).thenReturn(List.of());
        assertThrows(CustomiseException.class, () -> cartService.getAllCarts());
    }
}
