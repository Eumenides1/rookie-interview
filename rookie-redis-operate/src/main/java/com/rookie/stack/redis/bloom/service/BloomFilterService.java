package com.rookie.stack.redis.bloom.service;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author eumenides
 * @description
 * @date 2024/5/15
 */
@Service
public class BloomFilterService {

    @Autowired
    private RedissonClient redissonClient;

    private RBloomFilter<String> bloomFilter;

    @PostConstruct
    public void init() {
        bloomFilter = redissonClient.getBloomFilter("blacklistBloomFilter");
        bloomFilter.tryInit(1000000, 0.01);  // 初始化布隆过滤器，预期插入量，误判率
    }

    public void addToBlacklist(String userId) {
        bloomFilter.add(userId);
    }

    public boolean isInBlacklist(String userId) {
        return bloomFilter.contains(userId);
    }
}
