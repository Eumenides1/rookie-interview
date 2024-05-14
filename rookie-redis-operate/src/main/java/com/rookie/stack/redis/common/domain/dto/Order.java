package com.rookie.stack.redis.common.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@Data
public class Order implements Serializable {
    private String id;
    private String items;
    private double total;
    private String status;

    // Getters and Setters
}
