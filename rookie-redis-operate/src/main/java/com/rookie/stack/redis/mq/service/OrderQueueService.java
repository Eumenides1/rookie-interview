package com.rookie.stack.redis.mq.service;

import com.rookie.stack.redis.mq.domain.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@Service
public class OrderQueueService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String ORDER_QUEUE_KEY = "order:queue";

    @Autowired
    public OrderQueueService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void submitOrder(Order order) {
        redisTemplate.opsForList().leftPush(ORDER_QUEUE_KEY, order);
    }

    public Order processOrder() {
        return (Order) redisTemplate.opsForList().rightPop(ORDER_QUEUE_KEY);
    }
}
