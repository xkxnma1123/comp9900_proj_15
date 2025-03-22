package com.comp9900.proj_15.controller;

import com.comp9900.proj_15.entity.Friends;
import com.comp9900.proj_15.mapper.FriendsMapper;
// 或者使用 import com.comp9900.proj_15.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@RestController
@RequestMapping("/friends")
public class FriendsController {

    @Autowired
    private FriendsMapper friendsMapper;
    // 或者使用 private FriendsService friendsService;

    @GetMapping("/test-connection")
    public ResponseEntity<?> testConnection() {
        try {
            // 使用Mapper直接查询
            List<Friends> list = friendsMapper.selectList(null);
            // 或者使用Service: List<Friends> list = friendsService.list();
            
            return ResponseEntity.ok("数据库连接成功，记录数：" + list.size());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("数据库连接失败: " + e.getMessage());
        }
    }
}