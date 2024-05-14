package com.rookie.stack.redis.geo.service;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GeoService {
    private final RedisTemplate<String, String> redisTemplate;

    public GeoService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addUserLocation(String userId, double x, double y) {
        redisTemplate.opsForGeo().add("users", new Point(x, y), userId);
    }

    public List<RedisGeoCommands.GeoLocation<String>> findNearbyUsers(double x, double y, double distance) {
        Circle circle = new Circle(new Point(x, y), new Distance(distance, Metrics.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates().includeDistance().sortAscending();
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo()
                .radius("users", circle, args);
        return results.getContent().stream()
                .map(geoResult -> geoResult.getContent())
                .collect(Collectors.toList());
    }
}


