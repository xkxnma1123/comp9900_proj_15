package com.comp9900.proj_15.service;


import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comp9900.proj_15.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;
import java.util.Map;

/**
 * User Service Interface
 */

//public interface UserService {


public interface UserService extends IService<User> {


    /**
     * User registration
     */
    Map<String, Object> register(User user);

    /**
     * User login
     */
    Map<String, Object> login(String email, String password);

    /**
     * Get all countries list
     */
    List<Map<String, Object>> getAllCountries();

    /**
     * Get regions/provinces list by country code
     */
    List<Map<String, Object>> getRegionsByCountry(String countryCode);

    /**
     * Get cities list by country code and region code
     */
    List<Map<String, Object>> getCitiesByRegion(String countryCode, String regionCode);



    /**
     * Update user information
     */
    User updateUser(User user);



    /**
     * Random pagination query of users based on conditions
     *
     * @param page Pagination object
     * @param field Professional field
     * @param university University
     * @param city City
     * @return Pagination result
     */
    public Page<User> randomPageByConditions(Page<User> page, String field, String university, String city, int userID);

    /**
     * Get user's location and education related information
     * @param userId User ID
     * @return Map containing user's city, university and professional field
     */
    Map<String, String> getUserPeerMatchInfo(Integer userId);

    User getUserById(Integer id);


    /**
     * check if email exists
     */
    boolean existsByEmail(String email);
}


