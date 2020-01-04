/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : microservice_demo

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 04/01/2020 21:41:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for abtesting
-- ----------------------------
DROP TABLE IF EXISTS `abtesting`;
CREATE TABLE `abtesting`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `active` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `endpoint` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `weight` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of abtesting
-- ----------------------------
INSERT INTO `abtesting` VALUES (1, 'organizationservice', 'Y', 'http://localhost:9004', 5);

-- ----------------------------
-- Table structure for license
-- ----------------------------
DROP TABLE IF EXISTS `license`;
CREATE TABLE `license`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) NULL DEFAULT NULL,
  `license_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `license_max` int(11) NULL DEFAULT NULL,
  `license_allocated` int(11) NULL DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of license
-- ----------------------------
INSERT INTO `license` VALUES (1, 1, 'user', 'customer-crm-co', 100, 5, NULL);
INSERT INTO `license` VALUES (2, 1, 'user', 'suitability-plus', 200, 189, NULL);
INSERT INTO `license` VALUES (3, 2, 'user', 'HR-PowerSuite', 100, 4, NULL);
INSERT INTO `license` VALUES (4, 3, 'core-prod', 'WildCat Application Gateway', 16, 16, NULL);

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contact_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contact_email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contact_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES (1, 'customer-crm-co', 'Mark Balster', 'mark.balster@custcrmco.com', '823-555-1212');
INSERT INTO `organization` VALUES (2, 'HR-PowerSuite', 'Doug Drewry', 'doug.drewry@hr.com', '920-555-1212');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'ROLE_ADMIN');
INSERT INTO `role` VALUES (2, 'ROLE_USER');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'john', '$2a$10$bmqAT.2NDAjVU1fTzgBlQePrFm3mT7uYdusaQRWT3sXQXF60Wm2ii', 1);
INSERT INTO `user` VALUES (2, 'william', '$2a$10$BGcO2ELxi/mB9Eu5Sbavne4lBKTVzIpj0OkLMPX5RT12PSllaTVDS', 1);

-- ----------------------------
-- Table structure for user_org
-- ----------------------------
DROP TABLE IF EXISTS `user_org`;
CREATE TABLE `user_org`  (
  `user_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`, `org_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_org
-- ----------------------------
INSERT INTO `user_org` VALUES (1, 1);
INSERT INTO `user_org` VALUES (2, 2);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 2);
INSERT INTO `user_role` VALUES (2, 1);
INSERT INTO `user_role` VALUES (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
