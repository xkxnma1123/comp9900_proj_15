package com.comp9900.proj_15.service;


import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comp9900.proj_15.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */

//public interface UserService {


public interface UserService extends IService<User> {


    /**
     * 用户注册
     */
    Map<String, Object> register(User user);

    /**
     * 用户登录
     */
    Map<String, Object> login(String email, String password);

    /**
     * 获取所有国家列表
     */
    List<Map<String, Object>> getAllCountries();

    /**
     * 根据国家代码获取地区/省份列表
     */
    List<Map<String, Object>> getRegionsByCountry(String countryCode);

    /**
     * 根据国家代码和地区代码获取城市列表
     */
    List<Map<String, Object>> getCitiesByRegion(String countryCode, String regionCode);

//    /**
//     * 注册新用户
//     */
//    User registerUser(User user);

    /**
     * 更新用户信息
     */
    User updateUser(User user);



    /**
     * 根据条件随机分页查询用户
     *
     * @param page 分页对象
     * @param field 专业领域
     * @param university 大学
     * @param city 城市
     * @return 分页结果
     */
    public Page<User> randomPageByConditions(Page<User> page, String field, String university, String city);

    /**
     * 获取用户的位置和教育相关信息
     * @param userId 用户ID
     * @return 包含用户城市、大学和专业字段的Map
     */
    Map<String, String> getUserPeerMatchInfo(Integer userId);
}

