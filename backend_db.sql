/*
 Navicat Premium Dump SQL

 Source Server         : comp9900
 Source Server Type    : MySQL
 Source Server Version : 80035 (8.0.35)
 Source Host           : localhost:3306
 Source Schema         : backend_db

 Target Server Type    : MySQL
 Target Server Version : 80035 (8.0.35)
 File Encoding         : 65001

 Date: 20/04/2025 17:13:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for event
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(80) NOT NULL,
  `Date` datetime NOT NULL,
  `Description` text,
  `IMG` blob,
  `External_Link` varchar(255) DEFAULT NULL,
  `coin` int NOT NULL DEFAULT '0' COMMENT 'Added coin field',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of event
-- ----------------------------
BEGIN;
INSERT INTO `event` (`ID`, `Title`, `Date`, `Description`, `IMG`, `External_Link`, `coin`) VALUES (1, 'test_event', '2025-03-22 16:35:22', 'This is a test event.', NULL, 'www.app.rekro.au', 0);
INSERT INTO `event` (`ID`, `Title`, `Date`, `Description`, `IMG`, `External_Link`, `coin`) VALUES (2, 'test_evnet2', '2025-03-28 15:49:46', 'This is a test event', NULL, 'www.app.rekro.au', 0);
INSERT INTO `event` (`ID`, `Title`, `Date`, `Description`, `IMG`, `External_Link`, `coin`) VALUES (3, 'Sydney Language Exchange', '2025-04-04 14:15:05', 'oin our language exchange event to make friends from around the world and improve your language skills. The event will be held in Sydney CBD with snacks and drinks provided.', NULL, 'https://example.com/events/1', 0);
INSERT INTO `event` (`ID`, `Title`, `Date`, `Description`, `IMG`, `External_Link`, `coin`) VALUES (4, 'International Student Mixer', '2025-04-04 14:15:25', 'A social event for international students to meet classmates from different countries and cultural backgrounds. The event will be held at Sydney University with food and drinks provided.', NULL, 'https://example.com/events/2', 0);
COMMIT;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `FID` int NOT NULL AUTO_INCREMENT,
  `UID` int NOT NULL,
  `Friend_ID` int NOT NULL,
  `Status` enum('pending','accept','reject') NOT NULL,
  PRIMARY KEY (`FID`),
  KEY `Friend_ID` (`Friend_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of friends
-- ----------------------------
BEGIN;
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (1, 5529095, 5529096, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (2, 5529094, 5529093, 'accept');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (3, 5529094, 5529095, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (4, 5529093, 5529094, 'accept');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (5, 5529176, 5529096, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (6, 5529093, 5529096, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (7, 5529093, 5529098, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (8, 5529093, 5529109, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (9, 5529093, 5529112, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (10, 5529177, 5529117, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (11, 5529177, 5529126, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (12, 5529177, 5529177, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (13, 5529177, 5529093, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (14, 5529177, 5529101, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (15, 5529177, 5529123, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (16, 5529179, 5529093, 'pending');
INSERT INTO `friends` (`FID`, `UID`, `Friend_ID`, `Status`) VALUES (17, 5529179, 5529113, 'pending');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of Message
-- ----------------------------
BEGIN;
INSERT INTO `Message` (`MSG_ID`, `sender_ID`, `receiver_ID`, `content`, `created_at`, `read`) VALUES (1, 5529093, 5529094, 'this is a test message', '2025-04-01 16:20:48', 0);
INSERT INTO `Message` (`MSG_ID`, `sender_ID`, `receiver_ID`, `content`, `created_at`, `read`) VALUES (2, 5529093, 5529094, 'this is a test message2', '2025-04-01 16:26:22', 0);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) NOT NULL,
  `Level_of_study` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `Email` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `Password_hash` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `Created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `User_Type` enum('admin','normal') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'normal',
  `User_city` varchar(40) DEFAULT NULL,
  `User_country` varchar(40) DEFAULT NULL,
  `User_Field` varchar(40) DEFAULT NULL,
  `User_Language` varchar(40) DEFAULT NULL,
  `User_Regions` varchar(40) DEFAULT NULL,
  `User_Uni` varchar(40) DEFAULT NULL,
  `User_icon` varchar(255) DEFAULT NULL,
  `Email_verified` tinyint(1) NOT NULL DEFAULT '0',
  `coin` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=5529180 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529093, 'Chuzhe Ma', 'postgraduate', 'user@example.com', '827ccb0eea8a706c4c34a16891f84e7b', '2025-03-26 21:54:11', 'admin', 'Sydney', 'China', 'Engeering', 'Chinese', 'Beijing', 'University of New South Wales', 'https://picsum.photos/id/315/200', 1, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529094, 'Skyler Davis', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/175/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529095, 'Riley Wilson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/733/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529096, 'Jordan Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/339/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529097, 'Riley Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/296/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529098, 'Drew Johnson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/363/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529099, 'Casey Davis', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/827/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529100, 'Drew Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/245/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529101, 'Drew Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/447/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529102, 'Skyler Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/502/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529103, 'Jordan Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/168/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529104, 'Riley Jones', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/134/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529105, 'Taylor Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/969/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529106, 'Jamie Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/741/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529107, 'Jordan Jones', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/701/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529108, 'Riley Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/282/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529109, 'Riley Jones', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/105/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529110, 'Skyler Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/482/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529111, 'Jordan Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/196/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529112, 'Chris Wilson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/332/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529113, 'Taylor Davis', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/974/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529114, 'Taylor Davis', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/173/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529115, 'Taylor Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/544/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529116, 'Chris Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/303/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529117, 'Taylor Smith', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/683/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529118, 'Casey Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/605/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529119, 'Drew Davis', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/876/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529120, 'Morgan Smith', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/666/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529121, 'Jamie Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/604/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529122, 'Alex Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/924/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529123, 'Jamie Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/906/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529124, 'Casey Wilson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/759/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529125, 'Jamie Davis', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/979/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529126, 'Riley Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/718/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529127, 'Taylor Wilson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/555/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529128, 'Alex Wilson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/521/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529129, 'Casey Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/840/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529130, 'Morgan Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/736/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529131, 'Taylor Davis', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/160/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529132, 'Riley Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/292/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529133, 'Drew Williams', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/881/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529134, 'Taylor Johnson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/728/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529135, 'Jordan Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/898/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529136, 'Casey Smith', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/409/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529137, 'Riley Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/150/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529138, 'Jamie Johnson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/323/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529139, 'Chris Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/165/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529140, 'Jordan Jones', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/656/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529141, 'Skyler Jones', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/884/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529142, 'Morgan Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/555/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529143, 'Jordan Williams', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/925/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529144, 'Jamie Smith', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/158/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529145, 'Chris Smith', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/617/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529146, 'Morgan Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/713/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529147, 'Casey Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/713/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529148, 'Morgan Wilson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/425/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529149, 'Taylor Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/789/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529150, 'Morgan Jones', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/768/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529151, 'Casey Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/476/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529152, 'Morgan Johnson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/877/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529153, 'Taylor Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/157/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529154, 'Casey Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/754/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529155, 'Skyler Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/500/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529156, 'Chris Johnson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Arts', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/137/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529157, 'Drew Williams', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/886/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529158, 'Riley Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/318/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529159, 'Chris Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/636/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529160, 'Riley Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/324/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529161, 'Riley Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/514/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529162, 'Skyler Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/597/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529163, 'Riley Thomas', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/444/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529164, 'Jordan Davis', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/328/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529165, 'Jamie Smith', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/210/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529166, 'Chris Williams', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/868/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529167, 'Jamie Williams', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Engineering', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/910/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529168, 'Alex Jones', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Monash', 'https://picsum.photos/id/944/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529169, 'Chris Miller', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Engineering', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/993/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529170, 'Alex Anderson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/233/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529171, 'Chris Brown', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Arts', NULL, NULL, 'University of Sydney', 'https://picsum.photos/id/787/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529172, 'Casey Jones', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Sydney', NULL, 'Business', NULL, NULL, 'University of New South Wales', 'https://picsum.photos/id/437/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529173, 'Jordan Johnson', NULL, NULL, NULL, '2025-03-27 14:27:31', 'normal', 'Melbourne', NULL, 'Business', NULL, NULL, 'University of Melbourne', 'https://picsum.photos/id/624/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529174, 'test name', 'postgraduate', '123@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b', '2025-03-29 22:16:44', 'normal', NULL, NULL, NULL, NULL, NULL, NULL, 'https://picsum.photos/id/809/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529176, 'test name2', 'postgraduate', '12345@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b', '2025-04-01 18:37:16', 'normal', 'Sydney', 'China', 'Engeering', 'Chinese', 'Beijing', 'University of New South Wales', 'https://picsum.photos/id/275/200', 1, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529177, 'Hex', '66666', '2685138609@qq.com', 'bae5e3208a3c700e3db642b6631e95b9', '2025-04-10 14:05:34', 'normal', 'Sydneys', 'China', 'A', 'Eng', 'Ag', 'University of Sydney', 'https://picsum.photos/id/648/200', 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529178, 'Hhhhh', '', '347574257@qq.com', '1bbd886460827015e5d605ed44252251', '2025-04-11 10:51:46', 'normal', 'Sydney', 'China', 'He', 'English', 'Xiamen', 'The University of New South Wales', NULL, 0, 0);
INSERT INTO `user` (`ID`, `Name`, `Level_of_study`, `Email`, `Password_hash`, `Created_at`, `User_Type`, `User_city`, `User_country`, `User_Field`, `User_Language`, `User_Regions`, `User_Uni`, `User_icon`, `Email_verified`, `coin`) VALUES (5529179, 'Moses', 'Master', 'jonyli0824@163.com', '1bbd886460827015e5d605ed44252251', '2025-04-11 17:36:56', 'normal', 'Sydney', 'China', 'POSsssss', 'English', 'Guangzhou', 'The University of New South Wales', NULL, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for User_Event
-- ----------------------------
DROP TABLE IF EXISTS `User_Event`;
CREATE TABLE `User_Event` (
  `UID` int NOT NULL,
  `EID` int NOT NULL,
  `Status` enum('attend','quit') NOT NULL,
  `txn_id` int NOT NULL AUTO_INCREMENT,
  `check_flag` int DEFAULT NULL,
  PRIMARY KEY (`txn_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of User_Event
-- ----------------------------
BEGIN;
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529093, 1, 'attend', 1, 1);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529094, 1, 'attend', 2, NULL);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529093, 2, 'attend', 3, 1);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529177, 4, 'attend', 4, 1);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529177, 3, 'quit', 5, 1);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529177, 1, 'attend', 6, 1);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529178, 2, 'attend', 7, 1);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529178, 4, 'quit', 8, 1);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529179, 2, 'quit', 9, 1);
INSERT INTO `User_Event` (`UID`, `EID`, `Status`, `txn_id`, `check_flag`) VALUES (5529179, 4, 'attend', 10, 1);
COMMIT;

-- ----------------------------
-- Table structure for Verification_Code
-- ----------------------------
DROP TABLE IF EXISTS `Verification_Code`;
CREATE TABLE `Verification_Code` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Email` varchar(40) NOT NULL,
  `Code` varchar(10) NOT NULL,
  `Expiry_date` datetime NOT NULL,
  `Used` tinyint(1) NOT NULL DEFAULT '0',
  `Created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `Email_index` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of Verification_Code
-- ----------------------------
BEGIN;
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (1, '1234@gmail.com', '589532', '2025-04-09 23:18:30', 1, '2025-04-09 23:08:29');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (2, '2685138609@qq.com', '425225', '2025-04-10 14:04:14', 1, '2025-04-10 13:54:13');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (3, '2685138609@qq.com', '345348', '2025-04-10 14:14:41', 1, '2025-04-10 14:04:40');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (4, '347574257@qq.com', '102139', '2025-04-11 11:00:34', 1, '2025-04-11 10:50:33');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (5, 'jonyle0824@163.com', '773771', '2025-04-11 15:40:51', 0, '2025-04-11 15:30:51');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (6, 'jonyle0824@163.com', '736998', '2025-04-11 15:42:00', 1, '2025-04-11 15:32:00');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (7, 'jonyli0824@163.com', '231431', '2025-04-11 17:32:36', 1, '2025-04-11 17:22:35');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (8, 'jonyli0824@163.com', '812228', '2025-04-11 17:34:22', 1, '2025-04-11 17:24:21');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (9, 'jonyli0824@163.com', '920470', '2025-04-11 17:36:35', 0, '2025-04-11 17:26:35');
INSERT INTO `Verification_Code` (`ID`, `Email`, `Code`, `Expiry_date`, `Used`, `Created_at`) VALUES (10, 'jonyli0824@163.com', '363174', '2025-04-11 17:45:40', 1, '2025-04-11 17:35:40');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;