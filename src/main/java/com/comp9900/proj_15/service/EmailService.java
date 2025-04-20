package com.comp9900.proj_15.service;

/**
 * email service interface
 */
public interface EmailService {
    /**
     * send verification email
     */
    void sendVerificationEmail(String to, String code);
}