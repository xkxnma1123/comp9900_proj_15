package com.comp9900.proj_15.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.comp9900.proj_15.entity.User;

import com.comp9900.proj_15.mapper.UserMapper;
import com.comp9900.proj_15.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



import java.util.Collections;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private UserMapper userMapper;

//    @Override
//    public Map<String, Object> register (String name, String email, String levelOfStudy, String password){
//        // 检查邮箱是否已存在
//        Long count = userMapper.countByEmail(email);
//
//        if (count > 0) {
//            throw new RuntimeException("邮箱已被注册");
//        }
//
//        try {
//            // 使用MD5加密密码
//            String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
//
//            // 插入用户数据
//            userMapper.insertUser(
//                    name,
//                    email,
//                    levelOfStudy,
//                    hashedPassword,
//                    LocalDateTime.now(),
//                    userCountry,
//
//                    "normal"
//            );
//
//            // 查询并返回用户信息
//            Map<String, Object> user = userMapper.findUserByEmail(email);
//
//            if (user == null || user.isEmpty()) {
//                throw new RuntimeException("用户创建失败");
//            }
//
//            return user;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("创建用户失败: " + e.getMessage());
//        }
//    }
    @Override
    public Map<String, Object> register(User user) {
        // 检查邮箱是否已存在
        Long count = userMapper.countByEmail(user.getEmail());

        if (count > 0) {
            throw new RuntimeException("邮箱已被注册");
        }

        try {
            // 使用MD5加密密码
            String hashedPassword = DigestUtils.md5DigestAsHex(user.getPasswordHash().getBytes());
            user.setPasswordHash(hashedPassword);

            // 设置创建时间
            user.setCreatedAt(LocalDateTime.now());

            // 设置默认用户类型
            user.setUserType("normal");

            // 插入用户数据
            userMapper.insert(user);

            // 查询并返回用户信息
            Map<String, Object> userMap = userMapper.findUserByEmail(user.getEmail());

            if (userMap == null || userMap.isEmpty()) {
                throw new RuntimeException("用户创建失败");
            }

            return userMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建用户失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> login (String email, String password){
        // 获取用户密码
        String storedPassword = userMapper.getPasswordByEmail(email);

        if (storedPassword == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!storedPassword.equals(hashedPassword)) {
            throw new RuntimeException("密码错误");
        }

        // 查询并返回用户信息
        Map<String, Object> user = userMapper.findUserByEmail(email);

        if (user == null || user.isEmpty()) {
            throw new RuntimeException("获取用户信息失败");
        }

        return user;
    }


    private static final String COUNTRY_STATE_CITY_API_URL = "https://api.countrystatecity.in/v1";

    @Value("${countrystatecity.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取所有国家列表
     */
    @Override
    public List<Map<String, Object>> getAllCountries () {
        String url = COUNTRY_STATE_CITY_API_URL + "/countries";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CSCAPI-KEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> countries = new ArrayList<>();
        if (response.getBody() != null) {
            for (Object obj : response.getBody()) {
                if (obj instanceof Map) {
                    Map<String, Object> countryData = (Map<String, Object>) obj;
                    Map<String, Object> country = new HashMap<>();
                    country.put("code", countryData.get("iso2"));
                    country.put("name", countryData.get("name"));
                    countries.add(country);
                }
            }
        }

        return countries;
    }

    /**
     * 根据国家代码获取地区/省份列表
     */
    @Override
    public List<Map<String, Object>> getRegionsByCountry (String countryCode){
        String url = COUNTRY_STATE_CITY_API_URL + "/countries/" + countryCode + "/states";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CSCAPI-KEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> regions = new ArrayList<>();
        if (response.getBody() != null) {
            for (Object obj : response.getBody()) {
                if (obj instanceof Map) {
                    Map<String, Object> regionData = (Map<String, Object>) obj;
                    Map<String, Object> region = new HashMap<>();
                    region.put("code", regionData.get("iso2"));
                    region.put("name", regionData.get("name"));
                    regions.add(region);
                }
            }
        }

        return regions;
    }

    /**
     * 根据国家代码和地区代码获取城市列表
     */
    @Override
    public List<Map<String, Object>> getCitiesByRegion (String countryCode, String regionCode){
        String url = COUNTRY_STATE_CITY_API_URL + "/countries/" + countryCode + "/states/" + regionCode + "/cities";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CSCAPI-KEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> cities = new ArrayList<>();
        if (response.getBody() != null) {
            for (Object obj : response.getBody()) {
                if (obj instanceof Map) {
                    Map<String, Object> cityData = (Map<String, Object>) obj;
                    Map<String, Object> city = new HashMap<>();
                    city.put("code", cityData.get("id").toString());  // 城市通常使用ID作为代码
                    city.put("name", cityData.get("name"));
                    cities.add(city);
                }
            }
        }

        return cities;
    }

//    /**
//     * 注册新用户
//     */
//    @Override
//    public User registerUser(User user) {
//        // 设置创建时间
//        user.setCreatedAt(LocalDateTime.now());
//
//        // 这里应该添加密码加密逻辑
//        // user.setPasswordHash(encryptPassword(user.getPasswordHash()));
//
//        // 保存到数据库
//        save(user);
//
//        return user;
//    }

    /**
     * 更新用户信息
     */
    @Override
    public User updateUser (User user){
        // 获取原有用户信息
        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("User not found with id: " + user.getId());
        }

        // 如果提供了新密码，应该进行加密
        // if (user.getPasswordHash() != null && !user.getPasswordHash().equals(existingUser.getPasswordHash())) {
        //     user.setPasswordHash(encryptPassword(user.getPasswordHash()));
        // }

        // 更新用户信息
        updateById(user);

        return getById(user.getId());
    }

    @Override
    public Page<User> randomPageByConditions (Page < User > page, String field, String university, String city){
        // 1. 先查询满足条件的所有ID
        List<Integer> allIds = baseMapper.selectIdsByCondition(field, university, city);

        // 如果没有数据，返回空页
        if (allIds == null || allIds.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        // 2. 随机打乱ID列表
        Collections.shuffle(allIds);

        // 3. 根据分页参数计算当前页的ID子集
        int startIndex = (int) ((page.getCurrent() - 1) * page.getSize());
        if (startIndex >= allIds.size()) {
            return new Page<>(page.getCurrent(), page.getSize(), allIds.size());
        }

        int endIndex = Math.min(startIndex + (int) page.getSize(), allIds.size());
        List<Integer> pageIds = allIds.subList(startIndex, endIndex);

        // 4. 查询这批ID对应的完整记录数据
        List<User> users = new ArrayList<>();
        if (!pageIds.isEmpty()) {
            users = baseMapper.selectByIds(pageIds);
        }

        // 5. 设置分页结果并返回
        Page<User> resultPage = new Page<>(page.getCurrent(), page.getSize(), allIds.size());
        resultPage.setRecords(users);

        return resultPage;
    }

    @Override
    public Map<String, String> getUserPeerMatchInfo(Integer userId) {
        // 根据用户ID查询用户
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 创建返回的Map对象
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userCity", user.getUserCity());
        userInfo.put("userUni", user.getUserUni());
        userInfo.put("userField", user.getUserField());

        return userInfo;
    }
}

