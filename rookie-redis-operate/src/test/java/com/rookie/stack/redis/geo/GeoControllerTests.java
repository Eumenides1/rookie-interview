package com.rookie.stack.redis.geo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GeoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddUserLocationAndFindNearbyUsers() throws Exception {
        // 添加用户A的位置
        mockMvc.perform(post("/geo/users/userA")
                        .param("x", "116.397128")
                        .param("y", "39.916527"))
                .andExpect(status().isOk());

        // 添加用户B的位置
        mockMvc.perform(post("/geo/users/userB")
                        .param("x", "116.410886")
                        .param("y", "39.881949"))
                .andExpect(status().isOk());

        // 查询附近的用户
        mockMvc.perform(get("/geo/nearby")
                        .param("x", "116.397128")
                        .param("y", "39.916527")
                        .param("distance", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name").value("userA"))
                .andExpect(jsonPath("$.[1].name").value("userB"));
    }
}

