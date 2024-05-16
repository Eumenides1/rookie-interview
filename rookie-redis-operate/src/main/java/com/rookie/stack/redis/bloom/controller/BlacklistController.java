package com.rookie.stack.redis.bloom.controller;

import com.rookie.stack.redis.bloom.service.BloomFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author eumenides
 * @description
 * @date 2024/5/15
 */
@RestController
@RequestMapping("/blacklist")
public class BlacklistController {

    @Autowired
    private BloomFilterService bloomFilterService;

    @PostMapping
    public String addToBlacklist(@RequestParam String userId) {
        bloomFilterService.addToBlacklist(userId);
        return "Added to blacklist";
    }

    @GetMapping
    public String checkBlacklist(@RequestParam String userId) {
        if (bloomFilterService.isInBlacklist(userId)) {
            return "User is in blacklist";
        } else {
            return "User is not in blacklist";
        }
    }
}
