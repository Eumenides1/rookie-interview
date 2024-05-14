package com.rookie.stack.redis.mq;

import cn.hutool.json.JSONUtil;
import com.rookie.stack.redis.common.domain.dto.Order;
import com.rookie.stack.redis.mq.process.OrderProcessor;
import com.rookie.stack.redis.mq.service.OrderQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
         mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    private OrderQueueService orderQueueService;

    @MockBean
    private OrderProcessor orderProcessor;

    @Test
    public void shouldSubmitOrderAndProcessIt() throws Exception {
        Order order = new Order();
        order.setId("123");
        order.setItems("Item1, Item2");
        order.setTotal(99.99);
        order.setStatus("new");

        // 提交订单
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtil.toJsonStr(order)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order submitted successfully"));
        // 验证订单是否被提交到队列
        verify(orderQueueService, times(1)).submitOrder(order);

        // 模拟订单处理逻辑
        doNothing().when(orderProcessor).processOrders();

        // 验证订单处理器是否被调用
        verify(orderProcessor, times(1)).processOrders();

        // 验证订单状态是否更新
        assertThat("processed", equals(order.getStatus()));
    }

    // ... 其他测试
}
