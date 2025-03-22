/*
 Navicat MySQL Dump SQL

 Source Server         : comp9900
 Source Server Type    : MySQL
 Source Server Version : 80035 (8.0.35)
 Source Host           : localhost:3306
 Source Schema         : backend_db

 Target Server Type    : MySQL
 Target Server Version : 80035 (8.0.35)
 File Encoding         : 65001

 Date: 21/03/2025 18:45:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Event
-- ----------------------------
DROP TABLE IF EXISTS `Event`;
CREATE TABLE `Event` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(80) NOT NULL,
  `Date` datetime NOT NULL,
  `Description` text,
  `IMG` blob,
  `External_Link` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of Event
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for Friends
-- ----------------------------
DROP TABLE IF EXISTS `Friends`;
CREATE TABLE `Friends` (
  `UID` int NOT NULL,
  `Friend_ID` int NOT NULL,
  `Status` enum('pending','accept','reject') NOT NULL,
  PRIMARY KEY (`UID`,`Friend_ID`),
  KEY `Friend_ID` (`Friend_ID`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`UID`) REFERENCES `User` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`Friend_ID`) REFERENCES `User` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of Friends
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for Message
-- ----------------------------
DROP TABLE IF EXISTS `Message`;
CREATE TABLE `Message` (
  `MSG_ID` int NOT NULL AUTO_INCREMENT,
  `sender_ID` int DEFAULT NULL,
  `receiver_ID` int DEFAULT NULL,
  `content` text NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `read` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`MSG_ID`),
  KEY `sender_ID` (`sender_ID`),
  KEY `receiver_ID` (`receiver_ID`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`sender_ID`) REFERENCES `User` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `message_ibfk_2` FOREIGN KEY (`receiver_ID`) REFERENCES `User` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of Message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for User
-- ----------------------------
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) NOT NULL,
  `Level_of_study` enum('undergraduate','postgraduate','phd','research') NOT NULL,
  `Email` varchar(40) NOT NULL,
  `Password_hash` text NOT NULL,
  `Created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `User_Type` enum('admin','normal') NOT NULL DEFAULT 'normal',
  `User_city` varchar(40) DEFAULT NULL,
  `User_country` varchar(40) DEFAULT NULL,
  `User_Field` varchar(40) DEFAULT NULL,
  `User_Language` varchar(40) DEFAULT NULL,
  `User_Regions` varchar(40) DEFAULT NULL,
  `User_Uni` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of User
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for User_Event
-- ----------------------------
DROP TABLE IF EXISTS `User_Event`;
CREATE TABLE `User_Event` (
  `UID` int NOT NULL,
  `EID` int NOT NULL,
  `Status` enum('attend','unattended') NOT NULL,
  PRIMARY KEY (`UID`,`EID`),
  KEY `EID` (`EID`),
  CONSTRAINT `user_event_ibfk_1` FOREIGN KEY (`UID`) REFERENCES `User` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `user_event_ibfk_2` FOREIGN KEY (`EID`) REFERENCES `Event` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of User_Event
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
