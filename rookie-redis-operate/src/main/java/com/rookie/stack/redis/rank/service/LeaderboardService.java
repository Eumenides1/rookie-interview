package com.rookie.stack.redis.rank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
// 排行榜服务
@Service
public class LeaderboardService {

    private final StringRedisTemplate redisTemplate;
    private final String PREFIX = "leaderboard:";

    private final String BITMAP_PREFIX = "presence:";

    @Autowired
    public LeaderboardService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void updateScore(String userId, double score, String date) {
        String leaderboardKey = PREFIX + date;
        String presenceKey = BITMAP_PREFIX + date;

        // 更新用户的分数
        redisTemplate.opsForZSet().incrementScore(leaderboardKey, userId, score);

        // 标记用户在Bitmap中为存在
        redisTemplate.opsForValue().setBit(presenceKey, userId.hashCode(), true);
    }
    public Set<String> getLeaders(int start, int stop) {
        return redisTemplate.opsForZSet().reverseRange(PREFIX, start, stop);
    }

    public List<String> getConsistentLeaders(String date1, String date2) {
        String key1 = BITMAP_PREFIX + date1;
        String key2 = BITMAP_PREFIX + date2;
        String tempKey = "temp:" + UUID.randomUUID().toString();

        try {
            // 使用Redis的BITOP命令计算两个Bitmap的AND操作
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.bitOp(RedisStringCommands.BitOperation.AND, tempKey.getBytes(), key1.getBytes(), key2.getBytes());
                    return null;
                }
            });

            // 获取结果Bitmap中的所有设置为1的位（即两天都上榜的用户）
            Set<String> userIds = redisTemplate.opsForZSet().rangeByScore(PREFIX + date1, 0, Double.MAX_VALUE);
            List<String> consistentLeaders = new ArrayList<>();
            for (String userId : userIds) {
                if (redisTemplate.opsForValue().getBit(tempKey, userId.hashCode())) {
                    consistentLeaders.add(userId);
                }
            }
            return consistentLeaders;
        } finally {
            // 清理临时生成的Bitmap
            redisTemplate.delete(tempKey);
        }
    }
}
