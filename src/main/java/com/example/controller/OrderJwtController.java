package com.example.controller;
/**
 * OrderJwtController.java
 * This controller handles order-related operations with JWT authentication.
 * It allows placing, canceling, and retrieving orders based on user roles.
 */

import com.example.dto.DtoToAddressEntity;
import com.example.dto.DtoToOrderEntity;
import com.example.entity.Order;
import com.example.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderApi")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderJwtController {
    private OrderService orderService;

    public OrderJwtController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Places an order for a user.
     * @param role the role of the user making the request
     * @param userId the ID of the user placing the order
     * @param dtoToAddressEntity the DTO containing address details for the order
     * @return a ResponseEntity with the created Order or an error message
     */
    @PostMapping("/orderPlace")
    public ResponseEntity<?> placeOrder(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId, @RequestBody DtoToAddressEntity dtoToAddressEntity) {
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<Order>(orderService.placeOrder(userId,dtoToAddressEntity), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Add To Cart Book!!", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Cancels an order by its ID.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose order is being canceled
     * @param orderId the ID of the order to cancel
     * @return a ResponseEntity with a success message or an error message
     */
    @PostMapping("cancelOrder/{orderId}")
    public ResponseEntity<String> cancelOrder(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId, @PathVariable Long orderId){
        if ("USER".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(orderService.cancelOrder(userId, orderId), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Cancel Order from Cart!!", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves all orders for a user or admin.
     * @param role the role of the user making the request
     * @param userId the ID of the user whose orders are being retrieved
     * @return a ResponseEntity with a list of orders or an error message
     */
    @GetMapping("/getAllOrders")
    public ResponseEntity<?> getAllOrders(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId){
        if ("ADMIN".equalsIgnoreCase(role)) {
            return new ResponseEntity<>(orderService.getAllOrders(userId), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Get All Cart!!", HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/getAllOrdersForUser")
//    public ResponseEntity<?> getAllOrdersForUser(@RequestAttribute("role") String role, @RequestAttribute("userId") Long userId){
//        if ("USER".equalsIgnoreCase(role)) {
//            return new ResponseEntity<>(orderService.getAllOrdersForUser(userId), HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<String>("Token is NOT Valid to Get Cart!!", HttpStatus.NOT_FOUND);
//        }
//    }


}
