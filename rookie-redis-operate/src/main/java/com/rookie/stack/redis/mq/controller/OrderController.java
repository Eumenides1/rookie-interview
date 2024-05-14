package com.rookie.stack.redis.mq.controller;

import com.rookie.stack.redis.common.domain.dto.Order;
import com.rookie.stack.redis.mq.service.OrderQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; /**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderQueueService orderQueueService;

    @Autowired
    public OrderController(OrderQueueService orderQueueService) {
        this.orderQueueService = orderQueueService;
    }

    @PostMapping
    public ResponseEntity<String> submitOrder(@RequestBody Order order) {
        orderQueueService.submitOrder(order);
        return ResponseEntity.ok("Order submitted successfully");
    }
}
