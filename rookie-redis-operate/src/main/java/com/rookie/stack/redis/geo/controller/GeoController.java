package com.rookie.stack.redis.geo.controller;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
import com.rookie.stack.redis.geo.service.GeoService;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geo")
public class GeoController {
    private final GeoService geoService;

    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }

    @PostMapping("/users/{userId}")
    public void addUserLocation(@PathVariable String userId, @RequestParam double x, @RequestParam double y) {
        geoService.addUserLocation(userId, x, y);
    }

    @GetMapping("/nearby")
    public List<RedisGeoCommands.GeoLocation<String>> findNearbyUsers(@RequestParam double x, @RequestParam double y, @RequestParam double distance) {
        return geoService.findNearbyUsers(x, y, distance);
    }
}

