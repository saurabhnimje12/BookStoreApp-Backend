package com.example.controller;
/**
 * CartJwtController.java
 * This controller handles cart-related operations with JWT authentication.
 * It allows adding, removing, updating, and retrieving cart items based on user roles.
 */

import com.example.dto.CartEntityToDto;
import com.example.dto.DtoToCartEntity;
import com.example.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartApi")
//@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class CartJwtController {
    private CartService cartService;

    public CartJwtController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Adds a book to the cart by its ID with additional details.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose cart is being modified
     * @param bookId the ID of the book to add to the cart
     * @param dtoToCartEntity the DTO containing additional details for adding to cart
     * @return a ResponseEntity with a success message or an error message
     */
    @PostMapping("/addToCart/{bookId}")
    public ResponseEntity<String> addToCart(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId, @PathVariable Long bookId, @RequestBody DtoToCartEntity dtoToCartEntity) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(cartService.addToCart(userId, bookId, dtoToCartEntity), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Add To Cart Book", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Adds a book to the cart by its ID.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose cart is being modified
     * @param bookId the ID of the book to add to the cart
     * @return a ResponseEntity with a success message or an error message
     */
    @PostMapping("/addTooCart/{bookId}")
    public ResponseEntity<String> addTooCart(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId, @PathVariable Long bookId) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(cartService.addTooCart(userId, bookId), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Add To Cart Book", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Removes an item from the cart by its ID.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose cart is being modified
     * @param cartId the ID of the cart item to remove
     * @return a ResponseEntity with a success message or an error message
     */
    @DeleteMapping("/removeFromCart/{cartId}")
    public ResponseEntity<String> removeFromCart(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId, @PathVariable Long cartId) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(cartService.removeFromCart(userId, cartId), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Remove From Cart ", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Removes all items from the cart for a specific user.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose cart is being cleared
     * @return a ResponseEntity with a success message or an error message
     */
    @DeleteMapping("/removeByUserID")
    public ResponseEntity<String> removeByUserID(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(cartService.removeByUserID(userId), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Remove From Cart ", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the quantity of a cart item.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose cart is being updated
     * @param cartId the ID of the cart item to update
     * @param quantity the new quantity for the cart item
     * @return a ResponseEntity with a success message or an error message
     */
    @PatchMapping("/updateCart/{cartId}/{quantity}")
    public ResponseEntity<String> updateQuantityInCart(@RequestAttribute("role") String role, @RequestAttribute("userId") long userId, @PathVariable Long cartId, @PathVariable Integer quantity) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(cartService.updateQuantityInCart(userId, cartId, quantity), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Update the Cart ", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the quantity of a cart item by adding one unit.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose cart is being updated
     * @param cartId the ID of the cart item to update
     * @return a ResponseEntity with a success message or an error message
     */
    @PatchMapping("/updateCartAdd/{cartId}")
    public ResponseEntity<String> updateQuantityInCartAdd(@RequestAttribute("role") String role, @RequestAttribute("userId") long userId, @PathVariable Long cartId) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(cartService.updateQuantityInCartAdd(userId, cartId), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Update the Cart ", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the quantity of a cart item by removing one unit.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose cart is being updated
     * @param cartId the ID of the cart item to update
     * @return a ResponseEntity with a success message or an error message
     */
    @PatchMapping("/updateCartRmv/{cartId}")
    public ResponseEntity<String> updateQuantityInCartRmv(@RequestAttribute("role") String role, @RequestAttribute("userId") long userId, @PathVariable Long cartId) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(cartService.updateQuantityInCartRmv(userId, cartId), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Update the Cart ", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves all cart items for a specific user.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose cart items are to be retrieved
     * @return a ResponseEntity with a list of cart items or an error message
     */
    @GetMapping("/getAllCartById")
    public ResponseEntity<?> getAllCartItemsByUserID(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<List<CartEntityToDto>>(cartService.getAllCartItemsByUserID(userId), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Get the Cart Details", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves all carts for admin users.
     * @param role the role of the user making the request
     * @return a ResponseEntity with a list of cart details or an error message
     */
    @GetMapping("/getAllCarts")
    public ResponseEntity<?> getAllCarts(@RequestAttribute("role") String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return new ResponseEntity<List<CartEntityToDto>>(cartService.getAllCarts(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Get the Cart Details", HttpStatus.NOT_FOUND);
        }
    }
}
