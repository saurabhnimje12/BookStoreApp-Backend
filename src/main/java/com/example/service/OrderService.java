package com.example.service;

import com.example.dto.DtoToAddressEntity;
import com.example.dto.DtoToOrderEntity;
import com.example.entity.Order;

public interface OrderService {
    Order placeOrder(Long userId, DtoToAddressEntity dtoToAddressEntity);

//    Order placeOrder(Long userId, DtoToAddressEntity dtoToAddressEntity);

    String cancelOrder(Long userId, Long orderId);

    Object getAllOrders(Long userId);

//    Object getAllOrdersForUser(Long userId);
}
