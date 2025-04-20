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


        updateById(user);

        return getById(user.getId());
    }

    @Override
    public Page<User> randomPageByConditions(Page<User> page, String field, String university, String city, int userID) {
        // Directly use the int type userID passed in
        Integer myUserId = userID;

        // 1. First query all IDs that meet the conditions
        List<Integer> allIds = baseMapper.selectIdsByCondition(field, university, city);

        // If there is no data, return an empty page
        if (allIds == null || allIds.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        // 2. Remove the current user's own ID from the results
        if (myUserId != null) {
            allIds.removeIf(id -> id.equals(myUserId));
        }

        // 3. Get the current user's friend list and remove the friend's ID from the result
        if (myUserId != null) {
            List<Map<String, Object>> myFriends = friendsService.getFriends(myUserId);

            // Extract all friends' IDs
            Set<Integer> friendIds = myFriends.stream()
                    .filter(f -> f.containsKey("Friend_ID") && f.get("Friend_ID") instanceof Integer)
                    .map(f -> (Integer) f.get("Friend_ID"))
                    .collect(Collectors.toSet());

            // Remove the friend's ID from the results
            allIds.removeIf(friendIds::contains);
        }

        // If there is no data left after removing yourself and your friends, return to an empty page
        if (allIds.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        // 4. Randomly shuffle the ID list
        Collections.shuffle(allIds);

        // 5. Calculate the ID subset of the current page based on the paging parameters
        int startIndex = (int) ((page.getCurrent() - 1) * page.getSize());
        if (startIndex >= allIds.size()) {
            return new Page<>(page.getCurrent(), page.getSize(), allIds.size());
        }

        int endIndex = Math.min(startIndex + (int) page.getSize(), allIds.size());
        List<Integer> pageIds = allIds.subList(startIndex, endIndex);

        // 6. Query the complete record data of these IDs
        List<User> users = new ArrayList<>();
        if (!pageIds.isEmpty()) {
            users = baseMapper.selectByIds(pageIds);
        }

        // 7. Set the page result and return
        Page<User> resultPage = new Page<>(page.getCurrent(), page.getSize(), allIds.size());
        resultPage.setRecords(users);

        return resultPage;
    }


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


    @Override
    public boolean existsByEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }
}

