package com.rookie.stack.redis.geo;

import com.rookie.stack.redis.geo.service.GeoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@ExtendWith(MockitoExtension.class)
public class GeoServiceTests {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private GeoOperations<String, String> geoOperations;

    @InjectMocks
    private GeoService geoService;

    @Test
    public void testFindNearbyUsers() {
        // 模拟GeoOperations
        when(redisTemplate.opsForGeo()).thenReturn(geoOperations);

        // 模拟地理位置数据返回
        List<RedisGeoCommands.GeoLocation<String>> mockResult = List.of(
                new RedisGeoCommands.GeoLocation<>("userA", new Point(116.397128, 39.916527)),
                new RedisGeoCommands.GeoLocation<>("userB", new Point(116.410886, 39.881949))
        );
        when(geoOperations.radius(any(String.class), any(Circle.class), any(RedisGeoCommands.GeoRadiusCommandArgs.class)))
                .thenReturn(new GeoResults<>(mockResult.stream().map(loc -> new GeoResult<>(loc, new Distance(0))).collect(Collectors.toList())));

        // 测试查询附近的用户
        List<RedisGeoCommands.GeoLocation<String>> results = geoService.findNearbyUsers(116.397128, 39.916527, 5.0);

        // 验证结果
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("userA");
        assertThat(results.get(1).getName()).isEqualTo("userB");
    }
}

