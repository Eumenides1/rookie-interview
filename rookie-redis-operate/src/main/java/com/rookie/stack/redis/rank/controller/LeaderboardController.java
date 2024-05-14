package com.rookie.stack.redis.rank.controller;

import com.rookie.stack.redis.rank.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
// REST控制器
@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PostMapping("/update/{userId}/{score}")
    public void updateScore(@PathVariable String userId, @PathVariable double score) {
        // 获取当前日期作为排行榜的键
        String currentDate = LocalDate.now().toString();
        leaderboardService.updateScore(userId, score, currentDate);
    }

    @GetMapping("/leaders/{start}/{stop}")
    public Set<String> getLeaders(@PathVariable int start, @PathVariable int stop) {
        return leaderboardService.getLeaders(start, stop);
    }

    @GetMapping("/consistent-leaders/{date1}/{date2}")
    public ResponseEntity<List<String>> getConsistentLeaders(@PathVariable String date1, @PathVariable String date2) {
        List<String> consistentLeaders = leaderboardService.getConsistentLeaders(date1, date2);
        return ResponseEntity.ok(consistentLeaders);
    }
}
