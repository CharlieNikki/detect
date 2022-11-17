/*
 Navicat Premium Data Transfer

 Source Server         : 杨镇玮
 Source Server Type    : MySQL
 Source Server Version : 50536
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 50536
 File Encoding         : 65001

 Date: 17/11/2022 14:12:26
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
  `check_in_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考勤人员签到地点',
  `check_out_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考勤人员签退时间',
  `check_out_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考勤人员签退地点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of check_in_sheet
-- ----------------------------
INSERT INTO `check_in_sheet` VALUES (1, 55422, '2022年11月17日 下午2:08:58', '武汉', '2022年11月17日 下午2:09:32', '地大');

-- ----------------------------
-- Table structure for detect_records
-- ----------------------------
DROP TABLE IF EXISTS `detect_records`;
CREATE TABLE `detect_records`  (
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测描述',
  `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测时间',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '委托单号',
  `detect_person_id` int(255) NOT NULL COMMENT '检测人员id',
  `image` longblob NULL COMMENT '图片附件',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '检测编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4123124 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

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
-- Records of detect_request
-- ----------------------------
INSERT INTO `detect_request` VALUES (2, '测试工程1', '测试员1', '测试1', '测试1', '测试1', '测试1', NULL, 3, '2022年11月17日 下午1:35:17', '测试公司1');
INSERT INTO `detect_request` VALUES (3, '测试工程2', '测试员2', '测试2', '测试2', '测试2', '测试2', NULL, 2, NULL, '测试公司2');
INSERT INTO `detect_request` VALUES (4, '测试工程3', '测试员3', '测试3', '测试3', '测试3', '测试3', NULL, 1, NULL, '测试公司3');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `company_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55424 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (55422, '测试人员', '123123', '测试', '测试');
INSERT INTO `user` VALUES (55423, '张三', '112233', '33333', '123123123123');

SET FOREIGN_KEY_CHECKS = 1;
