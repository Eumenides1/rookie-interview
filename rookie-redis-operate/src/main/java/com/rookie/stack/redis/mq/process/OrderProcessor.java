package com.rookie.stack.redis.mq.process;

import com.rookie.stack.redis.common.domain.dto.Order;
import com.rookie.stack.redis.mq.service.OrderQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@Component
public class OrderProcessor {

    private final OrderQueueService orderQueueService;

    @Autowired
    public OrderProcessor(OrderQueueService orderQueueService) {
        this.orderQueueService = orderQueueService;
    }

    @Scheduled(fixedRate = 5000) // 每5秒执行一次
    public void processOrders() {
        Order order = orderQueueService.processOrder();
        if (order != null) {
            // 处理订单逻辑，比如更新订单状态、发送确认邮件等
            order.setStatus("processed");
            System.out.println("Processed order: " + order.getId());
        }
    }
}
