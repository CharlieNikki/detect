/*
 Navicat Premium Data Transfer

 Source Server         : 8.136.84.248
 Source Server Type    : MySQL
 Source Server Version : 50739
 Source Host           : 8.136.84.248:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 50739
 File Encoding         : 65001

 Date: 24/11/2022 19:48:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for check_in_sheet
-- ----------------------------
DROP TABLE IF EXISTS `check_in_sheet`;
CREATE TABLE `check_in_sheet`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '考勤人员id',
  `check_in_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考勤人员签到时间',
  `longitude` decimal(10, 2) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 2) NULL DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for detect_records
-- ----------------------------
DROP TABLE IF EXISTS `detect_records`;
CREATE TABLE `detect_records`  (
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测描述',
  `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测时间',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '委托单号',
  `detect_person_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测人员姓名',
  `image` longblob NULL COMMENT '图片附件',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '检测编号',
  `longitude` decimal(10, 2) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 2) NULL DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of detect_records
-- ----------------------------
INSERT INTO `detect_records` VALUES (NULL, '2022-11-24 13:21:09', NULL, NULL, '', 12, NULL, NULL);

-- ----------------------------
-- Table structure for detect_request
-- ----------------------------
DROP TABLE IF EXISTS `detect_request`;
CREATE TABLE `detect_request`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '委托单号',
  `project_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '施工工程名称',
  `leader_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `leader_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `project_detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测内容',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测地点',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `image` longblob NULL COMMENT '图片附件',
  `data_status` int(3) NULL DEFAULT 1 COMMENT '1:待检测，2:检测中，3:检测完成',
  `detect_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测时间',
  `project_company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '施工单位名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` mediumblob NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `company_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55424 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '李四', '111111', '中国地质大学', '222');

SET FOREIGN_KEY_CHECKS = 1;
