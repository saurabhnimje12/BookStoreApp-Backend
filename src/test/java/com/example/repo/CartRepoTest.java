package com.example.repo;

import com.example.entity.Cart;
import com.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartRepoTest {

    private CartRepo cartRepo;

    @BeforeEach
    void setUp() {
        cartRepo = Mockito.mock(CartRepo.class);
    }

    @Test
    void testFindByUser_Success() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");

        Cart cart1 = new Cart();
        cart1.setCartId(101L);
        cart1.setUser(user);

        Cart cart2 = new Cart();
        cart2.setCartId(102L);
        cart2.setUser(user);

        when(cartRepo.findByUser(user)).thenReturn(Arrays.asList(cart1, cart2));

        List<Cart> result = cartRepo.findByUser(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user, result.get(0).getUser());
    }

    @Test
    void testFindByUser_Failure() {
        User anotherUser = new User();
        anotherUser.setUserId(2L);
        anotherUser.setEmail("other@example.com");

        when(cartRepo.findByUser(anotherUser)).thenReturn(Collections.emptyList());

        List<Cart> result = cartRepo.findByUser(anotherUser);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
