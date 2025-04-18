package com.comp9900.proj_15.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.comp9900.proj_15.entity.User;

import com.comp9900.proj_15.mapper.UserMapper;
import com.comp9900.proj_15.service.FriendsService;
import com.comp9900.proj_15.service.UserService;
import com.comp9900.proj_15.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User Service Implementation
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private FriendsService friendsService;

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
        // Check if email already exists
        Long count = userMapper.countByEmail(user.getEmail());

        if (count > 0) {
            throw new RuntimeException("Email has already been registered");
        }

        try {
            // Use MD5 to encrypt password
            String hashedPassword = DigestUtils.md5DigestAsHex(user.getPasswordHash().getBytes());
            user.setPasswordHash(hashedPassword);

            // Set creation time
            user.setCreatedAt(LocalDateTime.now());

            // Set default user type
            user.setUserType("normal");

            // Insert user data
            userMapper.insert(user);

            // Query and return user information
            Map<String, Object> userMap = userMapper.findUserByEmail(user.getEmail());

            if (userMap == null || userMap.isEmpty()) {
                throw new RuntimeException("User creation failed");
            }

            return userMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> login(String email, String password) {
        // Get user password
        String storedPassword = userMapper.getPasswordByEmail(email);

        if (storedPassword == null) {
            throw new RuntimeException("User does not exist");
        }

        // Verify password
        String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!storedPassword.equals(hashedPassword)) {
            throw new RuntimeException("Incorrect password");
        }

        // Query and return user information
        Map<String, Object> user = userMapper.findUserByEmail(email);

        if (user == null || user.isEmpty()) {
            throw new RuntimeException("Failed to get user information");
        }

        // 打印整个用户对象，查看所有字段
        // System.out.println("用户数据: " + user);

        // 特别检查email_verified字段
        //Object verifiedObj = user.get("email_verified");
        // System.out.println("邮箱验证状态: " + verifiedObj + ", 类型: " + (verifiedObj != null ? verifiedObj.getClass().getName() : "null"));


//        boolean isVerified = false;
//
//        if (verifiedObj instanceof Boolean) {
//            isVerified = (Boolean) verifiedObj;
//        } else if (verifiedObj instanceof Number) {
//            isVerified = ((Number) verifiedObj).intValue() == 1;
//        } else if (verifiedObj instanceof String) {
//            isVerified = "1".equals(verifiedObj) || "true".equalsIgnoreCase((String) verifiedObj);
//        }
//
//        if (!isVerified) {
//            throw new RuntimeException("请先验证您的邮箱再登录");
//        }

        return user;
    }


    private static final String COUNTRY_STATE_CITY_API_URL = "https://api.countrystatecity.in/v1";

    @Value("${countrystatecity.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get all countries list
     */
    @Override
    public List<Map<String, Object>> getAllCountries() {
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
     * Get regions/provinces list by country code
     */
    @Override
    public List<Map<String, Object>> getRegionsByCountry(String countryCode) {
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
     * Get cities list by country code and region code
     */
    @Override
    public List<Map<String, Object>> getCitiesByRegion(String countryCode, String regionCode) {
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
                    city.put("code", cityData.get("id").toString());  // Cities usually use ID as code
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
     * Update user information
     */
    @Override
    public User updateUser(User user) {
        // Get existing user information
        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("User not found with id: " + user.getId());
        }

        // If new password is provided, it should be encrypted
        // if (user.getPasswordHash() != null && !user.getPasswordHash().equals(existingUser.getPasswordHash())) {
        //     user.setPasswordHash(encryptPassword(user.getPasswordHash()));
        // }

        // Update user information
        updateById(user);

        return getById(user.getId());
    }

    @Override
    public Page<User> randomPageByConditions(Page<User> page, String field, String university, String city, int userID) {
        // 直接使用传入的int类型userID
        Integer myUserId = userID;

        // 1. 首先查询所有满足条件的ID
        List<Integer> allIds = baseMapper.selectIdsByCondition(field, university, city);

        // 如果没有数据，返回空页面
        if (allIds == null || allIds.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        // 2. 从结果中去除当前用户自己的ID
        if (myUserId != null) {
            allIds.removeIf(id -> id.equals(myUserId));
        }

        // 3. 获取当前用户的好友列表，并从结果中去除好友的ID
        if (myUserId != null) {
            List<Map<String, Object>> myFriends = friendsService.getFriends(myUserId);

            // 提取所有好友的ID
            Set<Integer> friendIds = myFriends.stream()
                    .filter(f -> f.containsKey("Friend_ID") && f.get("Friend_ID") instanceof Integer)
                    .map(f -> (Integer) f.get("Friend_ID"))
                    .collect(Collectors.toSet());

            // 从结果中去除好友的ID
            allIds.removeIf(friendIds::contains);
        }

        // 如果去除自己和好友后没有剩余数据，返回空页面
        if (allIds.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        // 4. 随机打乱ID列表
        Collections.shuffle(allIds);

        // 5. 根据分页参数计算当前页面的ID子集
        int startIndex = (int) ((page.getCurrent() - 1) * page.getSize());
        if (startIndex >= allIds.size()) {
            return new Page<>(page.getCurrent(), page.getSize(), allIds.size());
        }

        int endIndex = Math.min(startIndex + (int) page.getSize(), allIds.size());
        List<Integer> pageIds = allIds.subList(startIndex, endIndex);

        // 6. 查询这些ID的完整记录数据
        List<User> users = new ArrayList<>();
        if (!pageIds.isEmpty()) {
            users = baseMapper.selectByIds(pageIds);
        }

        // 7. 设置分页结果并返回
        Page<User> resultPage = new Page<>(page.getCurrent(), page.getSize(), allIds.size());
        resultPage.setRecords(users);

        return resultPage;
    }

//    @Override
//    public Page<User> randomPageByConditions(Page<User> page, String field, String university, String city, String userID) {

//        // 1. First query all IDs that meet the conditions
//        List<Integer> allIds = baseMapper.selectIdsByCondition(field, university, city);
//
//        // If no data, return empty page
//        if (allIds == null || allIds.isEmpty()) {
//            return new Page<>(page.getCurrent(), page.getSize(), 0);
//        }
//
//        // 2. Randomly shuffle ID list
//        Collections.shuffle(allIds);
//
//        // 3. Calculate current page's ID subset based on pagination parameters
//        int startIndex = (int) ((page.getCurrent() - 1) * page.getSize());
//        if (startIndex >= allIds.size()) {
//            return new Page<>(page.getCurrent(), page.getSize(), allIds.size());
//        }
//
//        int endIndex = Math.min(startIndex + (int) page.getSize(), allIds.size());
//        List<Integer> pageIds = allIds.subList(startIndex, endIndex);
//
//        // 4. Query complete record data for these IDs
//        List<User> users = new ArrayList<>();
//        if (!pageIds.isEmpty()) {
//            users = baseMapper.selectByIds(pageIds);
//        }
//
//        // 5. Set pagination result and return
//        Page<User> resultPage = new Page<>(page.getCurrent(), page.getSize(), allIds.size());
//        resultPage.setRecords(users);
//
//        return resultPage;
        // 转换用户ID为整数类型

//    }

    @Override
    public Map<String, String> getUserPeerMatchInfo(Integer userId) {
        // Query user by user ID
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new RuntimeException("User does not exist");
        }

        // Create return Map object
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userCity", user.getUserCity());
        userInfo.put("userUni", user.getUserUni());
        userInfo.put("userField", user.getUserField());

        return userInfo;
    }

    @Override
    public User getUserById(Integer id) {
        return this.getById(id);
    }

//    @Override
//    public boolean updateEmailVerificationStatus(String email, int verifiedStatus) {
//        return userMapper.updateEmailVerificationStatus(email, verifiedStatus) > 0;
//    }

    @Override
    public boolean existsByEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }
}

