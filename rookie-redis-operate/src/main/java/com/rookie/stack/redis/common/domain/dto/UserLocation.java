package com.rookie.stack.redis.common.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@Data
public class UserLocation implements Serializable {
    private String userId;
    private double latitude;
    private double longitude;
}
