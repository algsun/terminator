# 正序添加,反向删除
DROP FUNCTION IF EXISTS `fun_getBaseLogicGroupChildren`;

DROP FUNCTION IF EXISTS `fun_getGatewayById`;

DROP FUNCTION IF EXISTS `fun_getChildren`;

DROP FUNCTION IF EXISTS `fun_getSeason`;

DROP FUNCTION IF EXISTS `fun_isExistAvg`;

DROP FUNCTION IF EXISTS `fun_isExistDateRb`;

DROP FUNCTION IF EXISTS `fun_isExistHourLux`;

DROP FUNCTION IF EXISTS `fun_isExistHourRb`;

DROP FUNCTION IF EXISTS `fun_isExistTable`;

DROP FUNCTION IF EXISTS `fun_isExistWindRose`;

DROP FUNCTION IF EXISTS `fun_precision_filter`;

DROP FUNCTION IF EXISTS `fun_pt100_mapping`;

DROP FUNCTION IF EXISTS `fun_threshold_filter`;

DROP FUNCTION IF EXISTS `getParentId`;

DROP TABLE IF EXISTS `m_control_module_condition_rfl`;

DROP TABLE IF EXISTS `m_emptyrecord`;

DROP TABLE IF EXISTS `m_threshold`;

DROP TABLE IF EXISTS `m_replace_sensor`;

DROP TABLE IF EXISTS `m_float_sensor`;

DROP TABLE IF EXISTS `m_pm_sensor_data`;

DROP TABLE IF EXISTS `m_windrose`;

DROP TABLE IF EXISTS `m_windmark`;

DROP TABLE IF EXISTS `m_tbl_lxh_acc`;

DROP TABLE IF EXISTS `m_tbl_sp_hour_acc`;

DROP TABLE IF EXISTS `m_tbl_op_hour_acc`;

DROP TABLE IF EXISTS `m_tbl_ip_hour_acc`;

DROP TABLE IF EXISTS `m_tbl_evap_hour_acc`;

DROP TABLE IF EXISTS `m_tbl_rb_day_acc`;

DROP TABLE IF EXISTS `m_tbl_rb_hour_acc`;

DROP TABLE IF EXISTS `m_control_module_notification_device`;

DROP TABLE IF EXISTS `m_control_module_notification`;

DROP TABLE IF EXISTS `m_control_module_action_interval`;

DROP TABLE IF EXISTS `m_control_module_action_daily`;

DROP TABLE IF EXISTS `m_control_module_sensor_condition`;

DROP TABLE IF EXISTS `m_control_module_sensor_action`;

DROP TABLE IF EXISTS `m_control_module_switch_change`;

DROP TABLE IF EXISTS `m_control_module_status`;

DROP TABLE IF EXISTS `m_control_module_switch`;

DROP TABLE IF EXISTS `m_formula_param`;

DROP TABLE IF EXISTS `m_formula_sensor`;

DROP TABLE IF EXISTS `m_formula`;

DROP TABLE IF EXISTS `m_netinfo`;

DROP TABLE IF EXISTS `m_zone_avgdata`;

DROP TABLE IF EXISTS `m_sensorinfo`;

DROP TABLE IF EXISTS `t_logicgroup`;

DROP TABLE IF EXISTS `m_location_sensor`;

DROP TABLE IF EXISTS `m_location_history`;

DROP TABLE IF EXISTS `m_location`;

DROP TABLE IF EXISTS `m_texture_threshold`;

DROP TABLE IF EXISTS `m_tbl_pt100_mapping`;

DROP TABLE IF EXISTS `m_nodesensor`;

DROP TABLE IF EXISTS `m_nodeinfomemory`;

DROP TABLE IF EXISTS `m_nodeinfo`;

DROP TABLE IF EXISTS `m_avgdata`;

CREATE TABLE `m_avgdata` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `sensorPhysicalid` INT (11) NOT NULL COMMENT '传感量标识',
  `maxValue` DOUBLE DEFAULT NULL COMMENT '最大值',
  `maxTime` DATETIME DEFAULT NULL COMMENT '最大值时间',
  `minValue` DOUBLE DEFAULT NULL COMMENT '最小值',
  `minTime` DATETIME DEFAULT NULL COMMENT '最小值时间',
  `avgValue` DOUBLE DEFAULT NULL COMMENT '平均值',
  `waveValue` DOUBLE DEFAULT NULL COMMENT '日波动值=最大值-最小值',
  `complianceRate` DOUBLE DEFAULT NULL COMMENT '达标率：达标率=达标数据包/总数据包',
  `ms_date` DATE DEFAULT NULL COMMENT '日期值',
  `isupdate` INT (11) DEFAULT '0' COMMENT '当数据因链路问题出现丢包，然后通过数据回补逻辑将基础数据补充完整后，统计表需要重新进行数据统计。（涉及到：均峰值、降雨量、日照、风向）',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  PRIMARY KEY (`id`),
  KEY `stamp` (`ms_date`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '在线设备均峰值数据表';

CREATE TABLE `m_nodeinfo` (
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `nodeType` INT (11) NOT NULL COMMENT '1：节点  2：中继  3:节点-主模块(可控) 4:节点-从模块(可控) 7：网关',
  `createTime` DATETIME NOT NULL COMMENT '节点创建时间或更新时间，与原add_time字段合并\n            （记录生成后不可修改）',
  `X` INT (4) NOT NULL DEFAULT '0' COMMENT 'X轴坐标',
  `Y` INT (4) NOT NULL DEFAULT '0' COMMENT 'Y轴坐标',
  `Z` INT (4) NOT NULL DEFAULT '0' COMMENT 'Z轴坐标',
  `siteId` VARCHAR (50) NOT NULL COMMENT '站点id',
  `deviceImage` VARCHAR (100) DEFAULT NULL COMMENT '系统相对路径和名称',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据版本',
  `binding` INT (2) DEFAULT '0' COMMENT '绑定状态：0 未绑定  1已绑定',
  `isActive` INT (1) NOT NULL DEFAULT '1' COMMENT '设备状态：0 无效  1有效',
  `sn` VARCHAR (20) NOT NULL DEFAULT '0' COMMENT '产品序列号',
  `uploadState` INT (11) NOT NULL DEFAULT '0' COMMENT '上传状态：0 未上传 1 已上传',
  `isHumCompensate` INT (1) NOT NULL DEFAULT '1' COMMENT '是否有温度补偿：0 否，1 是',
  `isHumdityDevice` INT (1) DEFAULT '0' COMMENT '是否是恒湿机：0 否，1 是',
  `voltageThreshold` FLOAT DEFAULT NULL COMMENT '设备电压阈值',
  PRIMARY KEY (`nodeid`),
  UNIQUE KEY `index_unique_nodeid` (`nodeid`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '设备信息表';

CREATE TABLE `m_nodeinfomemory` (
  `id` VARCHAR (50) NOT NULL COMMENT 'uuid',
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识=接入点号（8位）+ip号（5位）',
  `nodeVersion` INT (11) NOT NULL COMMENT '节点协议版本号',
  `isControl` INT (11) NOT NULL COMMENT '0:可控 1:不可控',
  `parentIP` INT (11) NOT NULL COMMENT '父节点IP号',
  `childIP` INT (11) NOT NULL COMMENT '当前节点IP号',
  `feedbackIP` INT (11) NOT NULL COMMENT '反馈地址IP号',
  `sequence` INT (11) NOT NULL COMMENT '包序列号',
  `stamp` DATETIME NOT NULL COMMENT '时间戳',
  `emptyStamp` DATETIME DEFAULT NULL COMMENT '空数据时间戳',
  `warmUp` INT (11) DEFAULT '0' COMMENT '设备预热时间，用于限定设备工作周期（设备工作周期必须大于预热时间）',
  `interval_i` INT (11) NOT NULL DEFAULT '600' COMMENT '工作周期',
  `rssi` INT (11) NOT NULL COMMENT '接收信号强度',
  `lqi` INT (11) NOT NULL COMMENT '连接质量参数',
  `lowvoltage` FLOAT NOT NULL DEFAULT '0' COMMENT '电压：-1、无电压值，其他的、电压值',
  `anomaly` INT (11) NOT NULL DEFAULT '0' COMMENT '设备状态：-1、超时, 0、正常, 1、低电压, 2、掉电',
  `deviceMode` INT (11) NOT NULL DEFAULT '0' COMMENT '0：正常模式 1：巡检模式',
  `remoteIp` VARCHAR (15) NOT NULL DEFAULT '192.168.0.1' COMMENT '网关设备ip',
  `remotePort` INT (11) NOT NULL DEFAULT '10000' COMMENT '网关数据监听端口',
  `sdCardState` INT (1) NOT NULL DEFAULT '0' COMMENT 'SD卡状态：0未插卡或卡未插好 1卡已插好 2卡已写满',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  `isThresholdAlarm` INT (1) NOT NULL DEFAULT '0' COMMENT '报警状态',
  `demarcate` INT (11) NOT NULL DEFAULT '0' COMMENT '0-费标定模式；1-标定模式',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_unique_nodeid` (`nodeid`),
  KEY `FK_设备信息和在线设备实时数据关系` (`nodeid`),
  CONSTRAINT `FK_设备信息和在线设备实时数据关系` FOREIGN KEY (`nodeid`) REFERENCES `m_nodeinfo` (`nodeid`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '在线设备实时状态表';

CREATE TABLE `m_nodesensor` (
  `id` VARCHAR (50) NOT NULL COMMENT 'uuid',
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `sensorPhysicalid` INT (11) NOT NULL COMMENT '传感量标识',
  `sensorPhysicalValue` VARCHAR (30) NOT NULL COMMENT '传感量值',
  `state` INT (11) NOT NULL DEFAULT '1' COMMENT '0：采样失败  0xFFFF为采样失败\n            1：采样正常\n            2：低于低阈值\n            3：超过高阈值\n            4：空数据（前台暂不处理）',
  `stamp` DATETIME NOT NULL COMMENT '数据采样时间戳（实时数据显示时采用一组数据中时间最大值）',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nodeid` (`nodeid`, `sensorPhysicalid`),
  KEY `FK_nodeinfo_nodesensor` (`nodeid`),
  CONSTRAINT `FK_nodeinfo_nodesensor` FOREIGN KEY (`nodeid`) REFERENCES `m_nodeinfo` (`nodeid`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '实时数据表';

CREATE TABLE `m_tbl_pt100_mapping` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `ohm` DOUBLE DEFAULT NULL COMMENT '电阻值',
  `temp` DOUBLE DEFAULT NULL COMMENT '温度',
  PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 232 DEFAULT CHARSET = utf8 COMMENT = 'pt100字典表';

INSERT INTO `m_tbl_pt100_mapping` (`id`, `ohm`, `temp`)
VALUES
  (1, 76.33, - 60),
  (2, 76.73, - 59),
  (3, 77.12, - 58),
  (4, 77.52, - 57),
  (5, 77.92, - 56),
  (6, 78.32, - 55),
  (7, 78.72, - 54),
  (8, 79.11, - 53),
  (9, 79.51, - 52),
  (10, 79.91, - 51),
  (11, 80.31, - 50),
  (12, 80.7, - 49),
  (13, 81.1, - 48),
  (14, 81.5, - 47),
  (15, 81.89, - 46),
  (16, 82.29, - 45),
  (17, 82.69, - 44),
  (18, 83.08, - 43),
  (19, 83.48, - 42),
  (20, 83.87, - 41),
  (21, 84.27, - 40),
  (22, 84.67, - 39),
  (23, 85.06, - 38),
  (24, 85.46, - 37),
  (25, 85.85, - 36),
  (26, 86.25, - 35),
  (27, 86.64, - 34),
  (28, 87.04, - 33),
  (29, 87.43, - 32),
  (30, 87.83, - 31),
  (31, 88.22, - 30),
  (32, 88.62, - 29),
  (33, 89.01, - 28),
  (34, 89.4, - 27),
  (35, 89.8, - 26),
  (36, 90.19, - 25),
  (37, 90.59, - 24),
  (38, 90.98, - 23),
  (39, 91.37, - 22),
  (40, 91.77, - 21),
  (41, 92.16, - 20),
  (42, 92.55, - 19),
  (43, 92.95, - 18),
  (44, 93.34, - 17),
  (45, 93.73, - 16),
  (46, 94.12, - 15),
  (47, 94.52, - 14),
  (48, 94.91, - 13),
  (49, 95.3, - 12),
  (50, 95.69, - 11),
  (51, 96.09, - 10),
  (52, 96.48, - 9),
  (53, 96.87, - 8),
  (54, 97.26, - 7),
  (55, 97.65, - 6),
  (56, 98.04, - 5),
  (57, 98.44, - 4),
  (58, 98.83, - 3),
  (59, 99.22, - 2),
  (60, 99.61, - 1),
  (61, 100, 0),
  (62, 100.39, 1),
  (63, 100.78, 2),
  (64, 101.17, 3),
  (65, 101.56, 4),
  (66, 101.95, 5),
  (67, 102.34, 6),
  (68, 102.73, 7),
  (69, 103.12, 8),
  (70, 103.51, 9),
  (71, 103.9, 10),
  (72, 104.29, 11),
  (73, 104.68, 12),
  (74, 105.07, 13),
  (75, 105.46, 14),
  (76, 105.85, 15),
  (77, 106.24, 16),
  (78, 106.63, 17),
  (79, 107.02, 18),
  (80, 107.4, 19),
  (81, 107.79, 20),
  (82, 108.18, 21),
  (83, 108.57, 22),
  (84, 108.96, 23),
  (85, 109.35, 24),
  (86, 109.73, 25),
  (87, 110.12, 26),
  (88, 110.51, 27),
  (89, 110.9, 28),
  (90, 111.29, 29),
  (91, 111.67, 30),
  (92, 112.06, 31),
  (93, 112.45, 32),
  (94, 112.83, 33),
  (95, 113.22, 34),
  (96, 113.61, 35),
  (97, 114, 36),
  (98, 114.38, 37),
  (99, 114.77, 38),
  (100, 115.15, 39),
  (101, 115.54, 40),
  (102, 115.93, 41),
  (103, 116.31, 42),
  (104, 116.7, 43),
  (105, 117.08, 44),
  (106, 117.47, 45),
  (107, 117.86, 46),
  (108, 118.24, 47),
  (109, 118.63, 48),
  (110, 119.01, 49),
  (111, 119.4, 50),
  (112, 119.78, 51),
  (113, 120.17, 52),
  (114, 120.55, 53),
  (115, 120.94, 54),
  (116, 121.32, 55),
  (117, 121.71, 56),
  (118, 122.09, 57),
  (119, 122.47, 58),
  (120, 122.86, 59),
  (121, 123.24, 60),
  (122, 123.63, 61),
  (123, 124.01, 62),
  (124, 124.39, 63),
  (125, 124.78, 64),
  (126, 125.16, 65),
  (127, 125.54, 66),
  (128, 125.93, 67),
  (129, 126.31, 68),
  (130, 126.69, 69),
  (131, 127.08, 70),
  (132, 127.46, 71),
  (133, 127.84, 72),
  (134, 128.22, 73),
  (135, 128.61, 74),
  (136, 128.99, 75),
  (137, 129.37, 76),
  (138, 129.75, 77),
  (139, 130.13, 78),
  (140, 130.52, 79),
  (141, 130.9, 80),
  (142, 131.28, 81),
  (143, 131.66, 82),
  (144, 132.04, 83),
  (145, 132.42, 84),
  (146, 132.8, 85),
  (147, 133.18, 86),
  (148, 133.57, 87),
  (149, 133.95, 88),
  (150, 134.33, 89),
  (151, 134.71, 90),
  (152, 135.09, 91),
  (153, 135.47, 92),
  (154, 135.85, 93),
  (155, 136.23, 94),
  (156, 136.61, 95),
  (157, 136.99, 96),
  (158, 137.37, 97),
  (159, 137.75, 98),
  (160, 138.13, 99),
  (161, 138.51, 100),
  (162, 138.88, 101),
  (163, 139.26, 102),
  (164, 139.64, 103),
  (165, 140.02, 104),
  (166, 140.4, 105),
  (167, 140.78, 106),
  (168, 141.16, 107),
  (169, 141.54, 108),
  (170, 141.91, 109),
  (171, 142.29, 110),
  (172, 142.67, 111),
  (173, 143.05, 112),
  (174, 143.43, 113),
  (175, 143.8, 114),
  (176, 144.18, 115),
  (177, 144.56, 116),
  (178, 144.94, 117),
  (179, 145.31, 118),
  (180, 145.69, 119),
  (181, 146.07, 120),
  (182, 146.44, 121),
  (183, 146.82, 122),
  (184, 147.2, 123),
  (185, 147.57, 124),
  (186, 147.95, 125),
  (187, 148.33, 126),
  (188, 148.7, 127),
  (189, 149.08, 128),
  (190, 149.46, 129),
  (191, 149.83, 130),
  (192, 150.21, 131),
  (193, 150.58, 132),
  (194, 150.96, 133),
  (195, 151.33, 134),
  (196, 151.71, 135),
  (197, 152.08, 136),
  (198, 152.46, 137),
  (199, 152.83, 138),
  (200, 153.21, 139),
  (201, 153.58, 140),
  (202, 153.96, 141),
  (203, 154.33, 142),
  (204, 154.71, 143),
  (205, 155.08, 144),
  (206, 155.46, 145),
  (207, 155.83, 146),
  (208, 156.2, 147),
  (209, 156.58, 148),
  (210, 156.95, 149),
  (211, 157.33, 150),
  (212, 157.7, 151),
  (213, 158.07, 152),
  (214, 158.45, 153),
  (215, 158.82, 154),
  (216, 159.19, 155),
  (217, 159.56, 156),
  (218, 159.94, 157),
  (219, 160.31, 158),
  (220, 160.68, 159),
  (221, 161.05, 160),
  (222, 161.43, 161),
  (223, 161.8, 162),
  (224, 162.17, 163),
  (225, 162.54, 164),
  (226, 162.91, 165),
  (227, 163.29, 166),
  (228, 163.66, 167),
  (229, 164.03, 168),
  (230, 164.4, 169),
  (231, 164.77, 170);

CREATE TABLE `m_texture_threshold` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `textureId` INT (11) NOT NULL COMMENT '质地id',
  `sensorPhysicalId` INT (11) NOT NULL COMMENT '监测指标id',
  `conditionType` INT (11) NOT NULL COMMENT '条件',
  `target` FLOAT NOT NULL COMMENT '目标值',
  `floating` FLOAT DEFAULT NULL COMMENT '浮动值',
  PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 105 DEFAULT CHARSET = utf8 COMMENT = '质地阈值表';

INSERT INTO `m_texture_threshold` (
  `id`,
  `textureId`,
  `sensorPhysicalId`,
  `conditionType`,
  `target`,
  `floating`
)
VALUES
  (1, 11, 33, 1, 20, 4),
  (2, 11, 32, 1, 50, 20),
  (3, 11, 41, 5, 300, NULL),
  (4, 11, 42, 3, 20, NULL),
  (5, 11, 85, 3, 4, NULL),
  (6, 11, 2049, 3, 5, NULL),
  (7, 11, 2048, 3, 5, NULL),
  (8, 11, 103, 3, 100, NULL),
  (9, 11, 104, 3, 250, NULL),
  (10, 11, 105, 3, 100, NULL),
  (11, 11, 91, 3, 80, NULL),
  (12, 11, 83, 3, 300, NULL),
  (13, 11, 2068, 3, 75, NULL),
  (14, 1, 33, 1, 20, 4),
  (15, 1, 32, 1, 50, 20),
  (16, 1, 41, 5, 300, NULL),
  (17, 1, 42, 3, 20, NULL),
  (18, 1, 85, 3, 4, NULL),
  (19, 1, 2049, 3, 5, NULL),
  (20, 1, 2048, 3, 5, NULL),
  (21, 1, 103, 3, 100, NULL),
  (22, 1, 104, 3, 250, NULL),
  (23, 1, 105, 3, 100, NULL),
  (24, 1, 91, 3, 80, NULL),
  (25, 1, 83, 3, 300, NULL),
  (26, 1, 2068, 3, 75, NULL),
  (27, 4, 33, 1, 20, 4),
  (28, 4, 32, 3, 45, NULL),
  (29, 4, 41, 5, 300, NULL),
  (30, 4, 42, 3, 20, NULL),
  (31, 4, 85, 3, 4, NULL),
  (32, 4, 2049, 3, 5, NULL),
  (33, 4, 2048, 3, 5, NULL),
  (34, 4, 103, 3, 100, NULL),
  (35, 4, 104, 3, 250, NULL),
  (36, 4, 105, 3, 100, NULL),
  (37, 4, 91, 3, 80, NULL),
  (38, 4, 83, 3, 300, NULL),
  (39, 4, 2068, 3, 75, NULL),
  (40, 2, 33, 1, 20, 4),
  (41, 2, 32, 3, 45, NULL),
  (42, 2, 41, 5, 300, NULL),
  (43, 2, 42, 3, 20, NULL),
  (44, 2, 85, 3, 4, NULL),
  (45, 2, 2049, 3, 5, NULL),
  (46, 2, 2048, 3, 5, NULL),
  (47, 2, 103, 3, 100, NULL),
  (48, 2, 104, 3, 250, NULL),
  (49, 2, 105, 3, 100, NULL),
  (50, 2, 91, 3, 80, NULL),
  (51, 2, 83, 3, 300, NULL),
  (52, 2, 2068, 3, 75, NULL),
  (53, 56, 33, 1, 20, 4),
  (54, 56, 32, 1, 50, 20),
  (55, 56, 41, 5, 50, NULL),
  (56, 56, 42, 3, 20, NULL),
  (57, 56, 85, 3, 4, NULL),
  (58, 56, 2049, 3, 5, NULL),
  (59, 56, 2048, 3, 5, NULL),
  (60, 56, 103, 3, 100, NULL),
  (61, 56, 104, 3, 250, NULL),
  (62, 56, 105, 3, 100, NULL),
  (63, 56, 91, 3, 80, NULL),
  (64, 56, 83, 3, 300, NULL),
  (65, 56, 2068, 3, 75, NULL),
  (66, 51, 33, 1, 20, 4),
  (67, 51, 32, 1, 50, 20),
  (68, 51, 41, 5, 50, NULL),
  (69, 51, 42, 3, 20, NULL),
  (70, 51, 85, 3, 4, NULL),
  (71, 51, 2049, 3, 5, NULL),
  (72, 51, 2048, 3, 5, NULL),
  (73, 51, 103, 3, 100, NULL),
  (74, 51, 104, 3, 250, NULL),
  (75, 51, 105, 3, 100, NULL),
  (76, 51, 91, 3, 80, NULL),
  (77, 51, 83, 3, 300, NULL),
  (78, 51, 2068, 3, 75, NULL),
  (79, 52, 33, 1, 20, 4),
  (80, 52, 32, 1, 50, 20),
  (81, 52, 41, 5, 50, NULL),
  (82, 52, 42, 3, 20, NULL),
  (83, 52, 85, 3, 4, NULL),
  (84, 52, 2049, 3, 5, NULL),
  (85, 52, 2048, 3, 5, NULL),
  (86, 52, 103, 3, 100, NULL),
  (87, 52, 104, 3, 250, NULL),
  (88, 52, 105, 3, 100, NULL),
  (89, 52, 91, 3, 80, NULL),
  (90, 52, 83, 3, 300, NULL),
  (91, 52, 2068, 3, 75, NULL),
  (92, 24, 33, 1, 20, 4),
  (93, 24, 32, 1, 50, 20),
  (94, 24, 41, 5, 150, NULL),
  (95, 24, 42, 3, 20, NULL),
  (96, 24, 85, 3, 4, NULL),
  (97, 24, 2049, 3, 5, NULL),
  (98, 24, 2048, 3, 5, NULL),
  (99, 24, 103, 3, 100, NULL),
  (100, 24, 104, 3, 250, NULL),
  (101, 24, 105, 3, 100, NULL),
  (102, 24, 91, 3, 80, NULL),
  (103, 24, 83, 3, 300, NULL),
  (104, 24, 2068, 3, 75, NULL);

CREATE TABLE `m_location` (
  `id` VARCHAR (36) NOT NULL COMMENT '主键',
  `locationName` VARCHAR (50) NOT NULL COMMENT '位置点名称',
  `nodeId` VARCHAR (20) DEFAULT NULL COMMENT '节点id',
  `zoneId` VARCHAR (50) DEFAULT NULL COMMENT '区域编号',
  `siteId` VARCHAR (50) NOT NULL COMMENT '站点编号',
  `createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `type` INT (1) DEFAULT '0' COMMENT '位置点类型：0:设备位置点;1:批次位置点;',
  `remark` VARCHAR (200) DEFAULT NULL,
  `photo` VARCHAR (200) DEFAULT NULL COMMENT '文物图片',
  PRIMARY KEY (`id`),
  KEY `FK_m_nodeinfo_nodeid` (`nodeId`),
  CONSTRAINT `FK_m_nodeinfo_nodeid` FOREIGN KEY (`nodeId`) REFERENCES `m_nodeinfo` (`nodeid`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '位置点表';

CREATE TABLE `m_location_history` (
  `id` VARCHAR (36) NOT NULL COMMENT '主键',
  `locationId` VARCHAR (36) NOT NULL COMMENT '位置点id',
  `nodeId` VARCHAR (20) NOT NULL COMMENT '节点id',
  `startTime` TIMESTAMP NULL DEFAULT NULL COMMENT '开始时间',
  `endTime` TIMESTAMP NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`),
  KEY `locationId` (`locationId`),
  CONSTRAINT `m_location_history_ibfk_1` FOREIGN KEY (`locationId`) REFERENCES `m_location` (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '位置点历史记录';

CREATE TABLE `m_location_sensor` (
  `id` VARCHAR (50) NOT NULL COMMENT 'uuid',
  `locationId` VARCHAR (20) NOT NULL COMMENT '位置点唯一标识',
  `sensorPhysicalId` INT (11) NOT NULL COMMENT '传感量标识',
  `sensorPhysicalValue` VARCHAR (30) NOT NULL COMMENT '传感量值',
  `state` INT (11) NOT NULL DEFAULT '1' COMMENT '0：采样失败  0xFFFF为采样失败\n            1：采样正常\n            2：低于低阈值\n            3：超过高阈值\n            4：空数据（前台暂不处理）',
  `stamp` DATETIME NOT NULL COMMENT '数据采样时间戳（实时数据显示时采用一组数据中时间最大值）',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  PRIMARY KEY (`id`),
  UNIQUE KEY `locationId` (
    `locationId`,
    `sensorPhysicalId`
  ),
  KEY `FK_location_locationsensor` (`locationId`),
  CONSTRAINT `FK_location_locationsensor` FOREIGN KEY (`locationId`) REFERENCES `m_location` (`id`) ON DELETE CASCADE
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '位置点实时数据表';

CREATE TABLE `t_logicgroup` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT 'logicGroupId编号',
  `logicGroupName` VARCHAR (50) NOT NULL COMMENT 'logicGroup名称',
  `siteId` VARCHAR (50) DEFAULT NULL COMMENT '站点唯一标识',
  `parentLogicGroupId` INT (11) DEFAULT NULL COMMENT '父级logicGroup',
  `orgCode` VARCHAR (20) DEFAULT NULL COMMENT '机构代码',
  `orgAddress` VARCHAR (100) DEFAULT NULL COMMENT '地址',
  `orgZipcode` VARCHAR (10) DEFAULT NULL COMMENT '邮编',
  `orgWebsite` VARCHAR (50) DEFAULT NULL COMMENT '网址',
  `orgTel` VARCHAR (20) DEFAULT NULL COMMENT '联系电话',
  `orgFax` VARCHAR (20) DEFAULT NULL COMMENT '传真',
  `activeState` INT (1) NOT NULL DEFAULT '1' COMMENT '1-未激活；2-待激活；3-已激活。',
  `logicGroupType` INT (1) NOT NULL DEFAULT '1' COMMENT '1本实例创建；2别的tomcat实例同步上来的。',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  `titleImage` VARCHAR (100) DEFAULT NULL,
  `bgImage` VARCHAR (100) DEFAULT NULL,
  `useTitle` INT (10) DEFAULT '1',
  `useBg` INT (10) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK42DD368C58ED2DA3` (`parentLogicGroupId`),
  KEY `FK42DD368C654F2A65` (`siteId`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = 't_logicGroup(站点组表)';

CREATE TABLE `m_sensorinfo` (
  `id` INT (11) NOT NULL AUTO_INCREMENT,
  `sensorPhysicalid` INT (11) NOT NULL COMMENT '传感量标识',
  `escape_sensor_id` INT (11) NOT NULL DEFAULT '0' COMMENT '转义传感量标识',
  `en_name` VARCHAR (20) NOT NULL COMMENT '传感量缩写',
  `cn_name` VARCHAR (50) NOT NULL COMMENT '监测量中文名',
  `sensorPrecision` INT (4) NOT NULL DEFAULT '2' COMMENT '传感量精度',
  `units` VARCHAR (20) NOT NULL COMMENT '计量单位',
  `positions` INT (4) NOT NULL DEFAULT '0' COMMENT '显示位',
  `isActive` INT (1) NOT NULL DEFAULT '1' COMMENT '是否有效 1：有效    0：无效',
  `showType` INT (11) NOT NULL COMMENT '0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类;',
  `minValue` DOUBLE NOT NULL DEFAULT '0' COMMENT '允许的最小值',
  `maxValue` DOUBLE NOT NULL DEFAULT '0' COMMENT '允许的最大值',
  `rangeType` INT (11) NOT NULL DEFAULT '0' COMMENT '无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;',
  `signType` INT (11) NOT NULL DEFAULT '0' COMMENT '原始值是否有符号。无符号 0; 有符号 1;',
  `conditionType` INT (11) DEFAULT NULL COMMENT '达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于。与目标值/浮动值有关',
  `target` FLOAT DEFAULT NULL COMMENT '文保行业监测调控预期目标值',
  `floating` FLOAT DEFAULT NULL COMMENT '浮动值：以目标值为中心的浮动范围',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sensorPhysicalid` (`sensorPhysicalid`)
) ENGINE = INNODB AUTO_INCREMENT = 127 DEFAULT CHARSET = utf8 COMMENT = '传感信息表';

INSERT INTO `m_sensorinfo` (
  `id`,
  `sensorPhysicalid`,
  `escape_sensor_id`,
  `en_name`,
  `cn_name`,
  `sensorPrecision`,
  `units`,
  `positions`,
  `isActive`,
  `showType`,
  `minValue`,
  `maxValue`,
  `rangeType`,
  `signType`,
  `conditionType`,
  `target`,
  `floating`
)
VALUES
  (1,32,0,'HUM','湿度',1,'%',0,1,0,0,100,3,0,1,50,20),
  ( 2, 33, 0, 'TMT', '温度', 1, '℃', 1, 1, 0, - 40, 120,3,1,1,20,4 ),
   ( 3, 34, 0, 'HCHO', '甲醛', 2, 'ppm', 2, 1, 0, 0, 10, 3, 0, NULL, NULL, NULL ),
    ( 4, 35, 0, 'DST', '粉尘', 1, 'mg/m³', 3, 1, 0, 0, 0.4, 3, 0, NULL, NULL, NULL ),
  ( 5, 36, 0, 'CO2', '二氧化碳', 0, 'ppm', 4, 1, 0, 0, 50000, 3, 0, 5, 1000, NULL ),
  ( 6, 37, 0, 'H2S', '硫化氢', 0, 'ppm', 5, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 7, 38, 0, 'O3', '臭氧', 0, 'ppm', 6, 1, 0, 0, 50, 3, 0, NULL, NULL, NULL ),
  ( 8, 39, 0, 'NO2', '二氧化氮', 0, 'ppm', 7, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 9, 40, 0, 'ACC', '加速度', 1, 'g', 8, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 10, 41, 0, 'LUX', '光照', 2, 'lx', 9, 1, 0, 0, 200000, 3, 1, 5, 300, NULL ),
  ( 11, 42, 0, 'UV', '紫外', 2, 'uw/c㎡', 10, 1, 0, 0, 10000, 3, 1, 3, 20, NULL ),
  ( 12, 43, 0, 'DT/FT', '露点/霜点', 1, '℃', 11, 1, 0, 0, 120, 3, 0, NULL, NULL, NULL ),
  ( 13, 44, 0, 'STMT', '土壤温度', 3, '℃', 12, 1, 0, 0, 65535, 3, 0, NULL, NULL, NULL ),
  ( 14, 45, 0, 'SHUM', '土壤含水率', 1, '%', 13, 1, 0, 0, 100, 3, 0, NULL, NULL, NULL ),
  ( 15, 46, 0, 'VOC', 'VOC', 2, 'ppm', 14, 1, 0, 0, 20, 3, 1, NULL, NULL, NULL ),
  ( 16, 47, 0, 'RB', '降雨量', 2, 'mm', 15, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 17, 48, 0, 'WDD', '风向', 1, '°', 16, 1, 1, 0, 360, 3, 1, NULL, NULL, NULL ),
  ( 18, 49, 0, 'WDP', '风速', 1, 'm/s', 17, 1, 0, 0, 30, 3, 1, NULL, NULL, NULL ),
  ( 19, 50, 0, 'WDF', '风力', 0, '级', 18, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 20, 51, 0, 'LTMT', '导线温度', 1, '℃', 19, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 21, 52, 0, 'TSN', '拉力', 1, 'KN', 20, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 22, 53, 0, 'FIT', '绝缘子泄漏电流', 0, 'μa', 21, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 23, 54, 0, 'SWD', '摆角(横向)', 1, '°', 22, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 24, 55, 0, 'AOL', '线上电流', 0, 'a', 23, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 25, 56, 0, 'WT', '水温', 1, '℃', 24, 1, 0, - 60, 100, 3, 1, NULL, NULL, NULL ),
  ( 26, 57, 0, 'PH', 'PH值', 2, '~', 25, 1, 0, 0.1, 14, 3, 1, NULL, NULL, NULL ),
  ( 27, 58, 0, 'DO', '溶氧', 0, 'mg/L', 26, 1, 0, 0, 20, 3, 1, NULL, NULL, NULL ),
  ( 28, 59, 0, 'SWDH', '摆角(纵向)', 1, '°', 27, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 29, 60, 0, 'BTMT', '表面温度', 1, '℃', 28, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 30, 61, 0, 'PA', '大气压强', 1, 'hPa', 29, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 31, 62, 0, 'COND', '电导率', 1, 'dS/m', 30, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 32, 63, 0, 'RRB', '降雨强度', 1, 'mm/h', 31, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 33, 65, 0, 'MWDD', '微风风向', 1, '°', 32, 1, 1, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 34, 66, 0, 'MWDP', '微风风速', 2, 'm/s', 33, 1, 0, 0, 60, 3, 0, NULL, NULL, NULL ),
  ( 35, 67, 0, 'SO2', '二氧化硫', 1, 'ppm', 34, 1, 0, 0, 20, 3, 1, NULL, NULL, NULL ),
  ( 36, 68, 0, 'STMT', '5TE土壤温度', 1, '℃', 35, 1, 0, - 40, 50, 3, 0, NULL, NULL, NULL ),
  ( 37, 69, 0, 'SHUM', '5TE容积含水率', 2, '%m³/m³', 36, 1, 0, 0, 100, 3, 0, NULL, NULL, NULL ),
  ( 38, 70, 0, 'COND', '5TE电导率', 2, 'dS/m', 37, 1, 0, 0, 23, 3, 0, NULL, NULL, NULL ),
  ( 39, 71, 0, 'USD', '距离', 3, 'mm', 38, 1, 0, 100, 1000, 3, 0, NULL, NULL, NULL ),
  ( 40, 72, 0, 'RM', '辐射度', 0, 'W/㎡', 39, 1, 0, 0, 1800, 3, 0, NULL, NULL, NULL ),
  ( 41, 73, 0, 'EC', 'EC值', 2, 'mS/m', 40, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 42, 74, 0, 'RTMT', '雨水温度', 2, '℃', 41, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 43, 75, 0, 'PWL', '液面高度', 1, 'mm', 42, 1, 0, 0, 100, 3, 0, NULL, NULL, NULL ),
  ( 44, 76, 0, 'SGRX', 'X方向裂隙', 5, 'mm', 43, 1, 0, - 1, 1, 3, 1, NULL, NULL, NULL ),
  ( 45, 77, 0, 'SGRY', 'Y方向裂隙', 5, 'mm', 44, 1, 0, - 1, 1, 3, 1, NULL, NULL, NULL ),
  ( 46, 78, 0, 'SGRZ', 'Z方向裂隙', 5, 'mm', 45, 1, 0, - 1, 1, 3, 1, NULL, NULL, NULL ),
  ( 47, 79, 0, 'LVDT', '位移量', 4, 'mm', 46, 1, 0, - 50, 50, 3, 1, NULL, NULL, NULL ),
  ( 48, 80, 0, 'EVAP', '蒸发量', 1, 'mm', 47, 1, 0, - 100, 100, 3, 1, NULL, NULL, NULL ),
  ( 49, 81, 0, 'DR', '液位增量', 1, 'mm', 48, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 50, 82, 0, 'LEVEL', '液位', 0, 'mm', 49, 1, 0, 0, 10000, 3, 1, NULL, NULL, NULL ),
  ( 51, 83, 0, 'VOC_HS', 'VOC-高灵敏度', 0, 'ppb', 50, 1, 0, 0, 50000, 3, 1, 3, 300, NULL ),
  ( 52, 84, 0, 'PA_HS', '大气压强-高灵敏度', 2, 'hPa', 51, 1, 0, 300, 1200, 3, 0, NULL, NULL, NULL ),
  ( 53, 85, 0, 'SO2_HS', '二氧化硫-高灵敏度', 0, 'ppb', 52, 1, 0, 0, 10000, 3, 1, 3, 4, NULL ),
  ( 54, 12287, 0, 'LONGITUDE', '经度', 5, '°', 53, 1, 2, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 55, 12286, 0, 'LATITUDE', '纬度', 5, '°', 54, 1, 2, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 56, 12285, 0, 'ALTITUDE', '海拔', 1, 'm', 56, 1, 2, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 57, 12284, 0, 'SPEED', '速率', 2, 'km/h', 57, 1, 2, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 58, 12283, 0, 'DIRECTION', '航向', 1, '°', 58, 1, 2, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 59, 86, 0, 'VSP', '土压力', 2, 'kPa', 53, 1, 0, - 200, 200, 3, 1, NULL, NULL, NULL ),
  ( 63, 87, 0, 'ACCL', '加速度', 2, 'g', 54, 1, 0, - 10, 10, 3, 1, NULL, NULL, NULL ),
  ( 64, 88, 0, 'SHAKE', '震动', 2, 'g', 55, 1, 0, - 10, 10, 3, 1, NULL, NULL, NULL ),
  ( 65, 89, 0, 'SWH', '开关量', 0, ' ', 56, 1, 0, 0, 1, 3, 0, NULL, NULL, NULL ),
  ( 69, 2048, 0, 'O3-HS', '臭氧-高灵敏度', 0, 'ppb', 56, 1, 0, 0, 500, 3, 0, 3, 5, NULL ),
  ( 70, 90, 0, 'PULSE', '水速', 2, 'm/s', 57, 1, 0, 0, 9.14, 3, 0, NULL, NULL, NULL ),
  ( 71, 91, 0, 'CH20-HS', '甲醛-高灵敏度', 0, 'ppb', 0, 1, 0, 0, 10000, 3, 1, 3, 80, NULL ),
  ( 72, 2049, 0, 'NO2-HS', '二氧化氮-高灵敏度', 0, 'ppb', 0, 1, 0, 0, 1000, 3, 0, 3, 5, NULL ),
  ( 73, 2050, 85, 'SO2-HS', '二氧化硫-高灵敏度', 0, 'ppb', 0, 1, 0, 0, 10000, 3, 0, NULL, NULL, NULL ),
  ( 74, 2051, 83, 'VOC-HS', 'VOC-高灵敏度', 0, 'ppb', 0, 1, 0, 0, 20000, 3, 0, NULL, NULL, NULL ),
  ( 75, 2052, 0, 'PM2.5', 'PM2.5', 1, 'ug/m³', 0, 1, 0, 0, 1000, 3, 0, 3, 75, NULL ),
  ( 76, 2053, 0, 'PM10', 'PM10', 1, 'ug/m³', 0, 1, 0, 0, 1000, 3, 0, NULL, NULL, NULL ),
  ( 77, 2054, 0, 'PM0.5', 'PM0.5', 1, 'ug/m³', 0, 1, 0, 0, 1000, 3, 0, NULL, NULL, NULL ),
  ( 78, 2055, 0, 'PM1', 'PM1', 1, 'ug/m³', 0, 1, 0, 0, 1000, 3, 0, NULL, NULL, NULL ),
  ( 79, 92, 0, 'SGRX-DIF', 'X方向裂隙差值', 5, 'mm', 0, 1, 0, 0, 1, 3, 1, NULL, NULL, NULL ),
  ( 80, 93, 0, 'SGRY-DIF', 'Y方向裂隙差值', 5, 'mm', 0, 1, 0, 0, 1, 3, 1, NULL, NULL, NULL ),
  ( 81, 94, 0, 'SGRZ-DIF', 'Z方向裂隙差值', 5, 'mm', 0, 1, 0, 0, 1, 3, 1, NULL, NULL, NULL ),
  ( 82, 2056, 0, 'CO-HS', '一氧化碳-高灵敏度', 0, 'ppb', 0, 1, 0, 0, 25000, 3, 0, NULL, NULL, NULL ),
  ( 83, 95, 0, 'COND-HS', '电导率-高灵敏度', 0, 'us/cm', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 84, 2060, 0, 'HUM', '湿度-气站', 4, '%', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 85, 2061, 0, 'TMT', '温度-气站', 5, '℃', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 86, 2062, 0, 'WDD', '风向-气站', 4, '°', 0, 1, 1, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 87, 2063, 0, 'WDP', '风速-气站', 5, 'm/s', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 88, 2064, 0, 'PA', '压力-气站', 3, 'kPa', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 89, 2065, 0, 'SO2', '二氧化硫-气站', 4, 'mg/m³', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 90, 2066, 0, 'CO', '一氧化碳-气站', 4, 'mg/m³', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 91, 2067, 0, 'O3', '臭氧-气站', 3, 'mg/m³', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 92, 2068, 0, 'PM2.5', 'PM2.5-气站', 4, 'mg/m³', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 93, 2069, 0, 'PM10', 'PM10-气站', 4, 'mg/m³', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 94, 2070, 0, 'NOX', '氮氧化物-气站', 4, 'mg/m³', 0, 1, 0, 0, 0, 0, 1, NULL, NULL, NULL ),
  ( 99, 2057, 0, 'ORGANIC_POL', '有机污染物', 3, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 100, 2058, 0, 'INORGANIC_POL', '无机污染物', 3, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 101, 2059, 0, 'SULFUROUS_POL', '含硫污染物', 3, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 102, 2071, 0, 'ORGANIC_POL_DIF', '有机污染物差值', 2, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 103, 2072, 0, 'INORGANIC_POL_DIF', '无机污染物差值', 2, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 104, 2073, 0, 'SULFUROUS_POL_DIF', '含硫污染物差值', 2, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 105, 3072, 0, 'ORGANIC_POL', '有机污染物', 3, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 106, 3073, 0, 'INORGANIC_POL', '无机污染物', 3, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 107, 3074, 0, 'SULFUROUS_POL', '含硫污染物', 3, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 108, 3075, 0, 'ORGANIC_POL_DIF', '有机污染物差值', 2, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 109, 3076, 0, 'INORGANIC_POL_DIF', '无机污染物差值', 2, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 110, 3077, 0, 'SULFUROUS_POL_DIF', '含硫污染物差值', 2, 'Hz', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 111, 98, 0, 'UV', '紫外', 2, 'μW/lm', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 112, 99, 0, 'Noise', '噪声', 0, 'dB', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 113, 100, 0, 'HCOOH（O3）', '甲酸（臭氧）', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 114, 101, 0, 'CH3COOH（O3）', '乙酸（臭氧）', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 115, 102, 0, 'O3', '臭氧', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 116, 103, 0, 'HCOOH', '甲酸', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 117, 104, 0, 'CH3COOH', '乙酸', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 118, 105, 0, 'NH3', '氨气', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 119, 106, 0, 'SO2', '二氧化硫', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 120, 107, 0, 'NO2', '二氧化氮', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 121, 108, 0, 'HCHO', '甲醛', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 122, 109, 0, 'F+', '氟离子', 2, 'μg/m³', 0, 1, 0, 0, 0, 0, 0, NULL, NULL, NULL ),
  ( 126, 2074, 0, 'TMT', '非接触式表面温度', 1, '℃', 0, 1, 0, - 40, 600, 3, 1, NULL, NULL, NULL ),
  ( 127, 0, 0, '', '', 0, '', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL );

CREATE TABLE `m_zone_avgdata` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识UUID',
  `zoneId` VARCHAR (50) NOT NULL COMMENT '区域ID',
  `sensorId` INT (11) NOT NULL COMMENT '监测指标ID',
  `avgValue` DOUBLE DEFAULT NULL COMMENT '平均值',
  `ms_date` DATE DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`),
  KEY `ms_date` (`ms_date`),
  KEY `FK_M_SENSORINFO_SENSOR_PHYSICAL_ID` (`sensorId`),
  CONSTRAINT `FK_M_SENSORINFO_SENSOR_PHYSICAL_ID` FOREIGN KEY (`sensorId`) REFERENCES `m_sensorinfo` (`sensorPhysicalid`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '各区域不同监测指标平均值';



CREATE TABLE `m_netinfo` (
  `id` INT (11) NOT NULL AUTO_INCREMENT,
  `radds` VARCHAR (20) DEFAULT NULL COMMENT 'TCPClient通讯模式使用',
  `rport` INT (5) DEFAULT '-1' COMMENT 'TCPClient通讯模式使用',
  `lport` INT (5) DEFAULT '-1' COMMENT 'UDP通讯模式使用',
  `sport` VARCHAR (20) DEFAULT NULL COMMENT '串口通讯使用',
  `brate` INT (11) DEFAULT '-1' COMMENT '串口通讯使用',
  `model` INT (11) NOT NULL DEFAULT '-1' COMMENT '1：串口\n            2：UDP\n            3：TCP',
  `state` INT (11) NOT NULL DEFAULT '0' COMMENT '0：未连接\n            1：正在连接\n            2：已连接\n            3：正在断开连接',
  `siteId` VARCHAR (50) DEFAULT NULL COMMENT '站点id，如果是v1.3协议对应的siteId，v3协议忽略此字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `lport` (`lport`)
) ENGINE = INNODB AUTO_INCREMENT = 2 DEFAULT CHARSET = utf8 COMMENT = '主机与网关设备网络连接信息表';

INSERT INTO `m_netinfo` (
  `id`,
  `radds`,
  `rport`,
  `lport`,
  `sport`,
  `brate`,
  `model`,
  `state`,
  `siteId`
)
VALUES
  (1, NULL, - 1, 10000, NULL, - 1, 2, 0, NULL);

CREATE TABLE `m_formula` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR (20) NOT NULL COMMENT '名称',
  `paramCount` INT (11) NOT NULL DEFAULT '0' COMMENT '公式系数个数',
  `description` VARCHAR (100) NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 6 DEFAULT CHARSET = utf8 COMMENT = '公式表';

INSERT INTO `m_formula` (
  `id`,
  `name`,
  `paramCount`,
  `description`
)
VALUES
  (1, '一元一次', 2, 'aX + b'),
  ( 2, '一元二次', 3, 'aX2 + bX + c' ),
  ( 3, '一元三次', 4, 'aX3 + bX2 + cX + d' ),
  ( 4, '角度规整', 2, 'degree(aX + b)' ),
  ( 5, '一元一次分段', 5, 'if(X <= e){ aX + b } else { cX + d }' );

CREATE TABLE `m_formula_sensor` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `device_id` VARCHAR (13) NOT NULL DEFAULT '0' COMMENT '设备ID。 如果为 0 则为监测指标默认参数；如果不为 0 则为设备自定义参数',
  `sensor_id` INT (11) NOT NULL COMMENT '监测指标标识',
  `formula_id` INT (11) NOT NULL COMMENT '公式ID',
  `min_x` INT (11) NOT NULL DEFAULT '0' COMMENT 'x 最小取值范围',
  `max_x` INT (11) NOT NULL DEFAULT '0' COMMENT 'x 最大取值范围',
  `x_range_type` INT (1) NOT NULL DEFAULT '0' COMMENT '无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;',
  `min_y` DOUBLE NOT NULL DEFAULT '0' COMMENT 'y 最小取值范围',
  `max_y` DOUBLE NOT NULL DEFAULT '0' COMMENT 'y 最大取值范围',
  `y_range_type` INT (1) NOT NULL DEFAULT '0' COMMENT '无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;超出范围显示限定值 4;',
  `sign_type` INT (11) NOT NULL DEFAULT '0' COMMENT '原始值是否有符号。无符号 0; 有符号 1;',
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_id` (`device_id`, `sensor_id`),
  KEY `sensor_id` (`sensor_id`),
  KEY `formula_id` (`formula_id`),
  CONSTRAINT `m_formula_sensor_ibfk_1` FOREIGN KEY (`sensor_id`) REFERENCES `m_sensorinfo` (`sensorPhysicalid`),
  CONSTRAINT `m_formula_sensor_ibfk_2` FOREIGN KEY (`formula_id`) REFERENCES `m_formula` (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 121 DEFAULT CHARSET = utf8 COMMENT = '监测指标公式表';

INSERT INTO `m_formula_sensor` (
  `id`,
  `device_id`,
  `sensor_id`,
  `formula_id`,
  `min_x`,
  `max_x`,
  `x_range_type`,
  `min_y`,
  `max_y`,
  `y_range_type`,
  `sign_type`
)
VALUES
  (1, '0', 32, 2, 100, 3338, 3, 0, 100, 3, 0),
  (2, '0', 33, 1, 0, 0, 0, - 40, 120, 3, 1),
  (3, '0', 34, 1, 0, 0, 0, 0, 10, 3, 0),
  (4, '0', 35, 1, 0, 0, 0, 0, 10, 3, 0),
  (5, '0', 36, 1, 0, 0, 0, 0, 50000, 3, 0),
  (6, '0', 37, 1, 0, 0, 0, 0, 0, 0, 0),
  (7, '0', 38, 1, 0, 0, 0, 0, 50, 3, 0),
  (8, '0', 39, 1, 0, 0, 0, 0, 0, 0, 0),
  (9, '0', 40, 2, 0, 0, 0, 0, 0, 0, 0),
  (10, '0', 41, 5, 0, 0, 0, 0, 200000, 3, 1),
  (11, '0', 42, 5, 0, 0, 0, 0, 10000, 3, 1),
  (12, '0', 43, 1, 0, 0, 0, 0, 120, 0, 0),
  (13, '0', 44, 1, 0, 0, 0, 0, 65535, 3, 0),
  (14, '0', 45, 2, 0, 0, 0, 0, 100, 3, 0),
  (15, '0', 46, 1, 0, 0, 0, 0, 20, 3, 1),
  (16, '0', 47, 1, 0, 0, 0, 0, 0, 0, 0),
  (17, '0', 48, 4, 0, 0, 0, 0, 360, 3, 1),
  (18, '0', 49, 1, 0, 0, 0, 0, 30, 3, 1),
  (19, '0', 50, 1, 0, 0, 0, 0, 0, 0, 0),
  (20, '0', 51, 1, 0, 0, 0, 0, 0, 0, 0),
  (21, '0', 52, 1, 0, 0, 0, 0, 0, 0, 0),
  (22, '0', 53, 1, 0, 0, 0, 0, 0, 0, 0),
  (23, '0', 54, 1, 0, 0, 0, 0, 0, 0, 0),
  (24, '0', 55, 2, 0, 0, 0, 0, 0, 0, 0),
  (25, '0', 56, 2, 0, 0, 0, - 60, 100, 0, 1),
  (26, '0', 57, 1, 0, 0, 0, 0.1, 14, 3, 1),
  (27, '0', 58, 1, 0, 0, 0, 0, 20, 3, 1),
  (28, '0', 59, 1, 0, 0, 0, 0, 0, 0, 0),
  (29, '0', 60, 1, 0, 0, 0, 0, 0, 0, 0),
  (30, '0', 61, 1, 0, 0, 0, 0, 0, 0, 0),
  (31, '0', 62, 1, 0, 0, 0, 0, 0, 0, 0),
  (32, '0', 63, 1, 0, 0, 0, 0, 0, 0, 0),
  (33, '0', 65, 4, 0, 0, 0, 0, 0, 0, 0),
  (34, '0', 66, 1, 0, 0, 0, 0, 60, 3, 0),
  (35, '0', 67, 1, 0, 0, 0, 0, 20, 3, 1),
  (36, '0', 68, 5, 0, 0, 0, - 40, 50, 3, 0),
  (37, '0', 69, 3, 0, 0, 0, 0, 100, 3, 0),
  (38, '0', 70, 5, 0, 0, 0, 0, 23, 3, 0),
  (39, '0', 71, 1, 0, 0, 0, 100, 1000, 3, 0),
  (40, '0', 72, 1, 0, 0, 0, 0, 1800, 3, 0),
  (41, '0', 73, 1, 0, 0, 0, 0, 0, 0, 0),
  (42, '0', 74, 1, 0, 0, 0, 0, 0, 0, 0),
  (43, '0', 75, 1, 0, 0, 0, 0, 100, 3, 0),
  (44, '0', 76, 1, 0, 0, 0, - 1, 1, 3, 1),
  (45, '0', 77, 1, 0, 0, 0, - 1, 1, 3, 1),
  (46, '0', 78, 1, 0, 0, 0, - 1, 1, 3, 1),
  (47, '0', 79, 1, 0, 0, 0, - 50, 50, 3, 1),
  (48, '0', 80, 1, 0, 0, 0, - 100, 100, 3, 1),
  (49, '0', 81, 1, 0, 0, 0, 0, 0, 0, 0),
  (50, '0', 82, 1, 0, 0, 0, 0, 10000, 3, 1),
  (51, '0', 83, 1, 0, 0, 0, 0, 50000, 3, 1),
  (52, '0', 84, 1, 0, 0, 0, 300, 1200, 3, 0),
  (53, '0', 85, 1, 0, 0, 0, 0, 10000, 3, 1),
  (54, '0', 86, 1, 0, 0, 0, - 200, 200, 3, 1),
  (55, '0', 87, 1, 0, 0, 0, - 10, 10, 3, 1),
  (56, '0', 88, 1, 0, 0, 0, - 10, 10, 3, 1),
  (57, '0', 89, 1, 0, 0, 0, 0, 1, 3, 0),
  (60, '0', 12283, 1, 0, 0, 0, 0, 0, 0, 0),
  (61, '0', 12284, 1, 0, 0, 0, 0, 0, 0, 0),
  (62, '0', 12285, 1, 0, 0, 0, 0, 0, 0, 1),
  (63, '0', 12286, 1, 0, 0, 0, 0, 0, 0, 1),
  (64, '0', 12287, 1, 0, 0, 0, 0, 0, 0, 1),
  (66, '0', 2048, 1, 0, 0, 0, 0, 500, 3, 0),
  (67, '0', 90, 1, 0, 0, 0, 0, 9.14, 3, 0),
  (68, '0', 91, 1, 0, 0, 0, 0, 10000, 3, 1),
  (69, '0', 2049, 1, 0, 0, 0, 0, 1000, 3, 0),
  (70, '0', 2050, 1, 0, 0, 0, 0, 10000, 3, 0),
  (71, '0', 2051, 1, 0, 0, 0, 0, 20000, 3, 0),
  (72, '0', 2052, 2, 0, 0, 0, 0, 1000, 3, 0),
  (73, '0', 2053, 2, 0, 0, 0, 0, 1000, 3, 0),
  (74, '0', 2054, 2, 0, 0, 0, 0, 1000, 3, 0),
  (75, '0', 2055, 2, 0, 0, 0, 0, 1000, 3, 0),
  (76, '0', 92, 1, 0, 0, 0, 0, 1, 3, 1),
  (77, '0', 93, 1, 0, 0, 0, 0, 1, 3, 1),
  (78, '0', 94, 1, 0, 0, 0, 0, 1, 3, 1),
  (79, '0', 2056, 1, 0, 0, 0, 0, 25000, 3, 0),
  (80, '0', 95, 2, 0, 0, 0, 0, 0, 0, 0),
  (81, '0', 2060, 1, 0, 0, 0, 0, 0, 0, 1),
  (82, '0', 2061, 1, 0, 0, 0, 0, 0, 0, 1),
  (83, '0', 2062, 1, 0, 0, 0, 0, 0, 0, 1),
  (84, '0', 2063, 1, 0, 0, 0, 0, 0, 0, 1),
  (85, '0', 2064, 1, 0, 0, 0, 0, 0, 0, 1),
  (86, '0', 2065, 1, 0, 0, 0, 0, 0, 0, 1),
  (87, '0', 2066, 1, 0, 0, 0, 0, 0, 0, 1),
  (88, '0', 2067, 1, 0, 0, 0, 0, 0, 0, 1),
  (89, '0', 2068, 1, 0, 0, 0, 0, 0, 0, 1),
  (90, '0', 2069, 1, 0, 0, 0, 0, 0, 0, 1),
  (91, '0', 2070, 1, 0, 0, 0, 0, 0, 0, 1),
  (96, '0', 2057, 1, 0, 0, 0, 0, 0, 0, 0),
  (97, '0', 2058, 1, 0, 0, 0, 0, 0, 0, 0),
  (98, '0', 2059, 1, 0, 0, 0, 0, 0, 0, 0),
  (99, '0', 2071, 1, 0, 0, 0, 0, 0, 0, 0),
  (100, '0', 2072, 1, 0, 0, 0, 0, 0, 0, 0),
  (101, '0', 2073, 1, 0, 0, 0, 0, 0, 0, 0),
  (102, '0', 3072, 1, 0, 0, 0, 0, 0, 0, 0),
  (103, '0', 3073, 1, 0, 0, 0, 0, 0, 0, 0),
  (104, '0', 3074, 1, 0, 0, 0, 0, 0, 0, 0),
  (105, '0', 3075, 1, 0, 0, 0, 0, 0, 0, 0),
  (106, '0', 3076, 1, 0, 0, 0, 0, 0, 0, 0),
  (107, '0', 3077, 1, 0, 0, 0, 0, 0, 0, 0),
  (108, '0', 98, 1, 0, 0, 0, 0, 0, 0, 1),
  (109, '0', 99, 1, 0, 0, 0, 0, 0, 0, 1),
  (110, '0', 100, 1, 0, 0, 0, 0, 0, 0, 1),
  (111, '0', 101, 1, 0, 0, 0, 0, 0, 0, 1),
  (112, '0', 102, 1, 0, 0, 0, 0, 0, 0, 1),
  (113, '0', 103, 1, 0, 0, 0, 0, 0, 0, 1),
  (114, '0', 104, 1, 0, 0, 0, 0, 0, 0, 1),
  (115, '0', 105, 1, 0, 0, 0, 0, 0, 0, 1),
  (116, '0', 106, 1, 0, 0, 0, 0, 0, 0, 1),
  (117, '0', 107, 1, 0, 0, 0, 0, 0, 0, 1),
  (118, '0', 108, 1, 0, 0, 0, 0, 0, 0, 1),
  (119, '0', 109, 1, 0, 0, 0, 0, 0, 0, 1),
  (120, '0', 2074, 1, - 40, 600, 3, - 40, 600, 3, 1);

CREATE TABLE `m_formula_param` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `device_id` VARCHAR (13) NOT NULL DEFAULT '0' COMMENT '设备ID。 如果为 0 则为监测指标默认参数；如果不为 0 则为设备自定义参数',
  `sensor_id` INT (11) NOT NULL COMMENT '监测指标标识',
  `name` VARCHAR (2) NOT NULL COMMENT '参数名称：例如 a, b, c',
  `value` VARCHAR (20) NOT NULL COMMENT '参数值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_id` (`device_id`, `sensor_id`, `name`),
  KEY `sensor_id` (`sensor_id`),
  CONSTRAINT `m_formula_param_ibfk_1` FOREIGN KEY (`sensor_id`) REFERENCES `m_sensorinfo` (`sensorPhysicalid`)
) ENGINE = INNODB AUTO_INCREMENT = 649 DEFAULT CHARSET = utf8 COMMENT = '公式系数表';

INSERT INTO `m_formula_param` (
  `id`,
  `device_id`,
  `sensor_id`,
  `name`,
  `value`
)
VALUES
  (1, '0', 32, 'a', '-0.0000028'),
  (2, '0', 32, 'b', '0.0405'),
  (3, '0', 32, 'c', '-4'),
  (4, '0', 33, 'a', '0.01'),
  (5, '0', 33, 'b', '-39.66'),
  (6, '0', 34, 'a', '3.2414'),
  (7, '0', 34, 'b', '-0.3241'),
  (10, '0', 36, 'a', '1'),
  (11, '0', 36, 'b', '0'),
  (12, '0', 37, 'a', '17.241'),
  (13, '0', 37, 'b', '-1.7241'),
  (14, '0', 38, 'a', '1'),
  (15, '0', 38, 'b', '0'),
  (16, '0', 39, 'a', '1'),
  (17, '0', 39, 'b', '0'),
  (18, '0', 40, 'a', '1'),
  (19, '0', 40, 'b', '2'),
  (20, '0', 40, 'c', '1'),
  (21, '0', 41, 'a', '0.06926'),
  (22, '0', 41, 'b', '-0.96964'),
  (23, '0', 42, 'a', '0.1283'),
  (24, '0', 42, 'b', '-0.0007'),
  (25, '0', 43, 'a', '1'),
  (26, '0', 43, 'b', '0'),
  (27, '0', 44, 'a', '0.0028'),
  (28, '0', 44, 'b', '73'),
  (29, '0', 45, 'a', '0.000000092308'),
  (30, '0', 45, 'b', '0.000812469465'),
  (31, '0', 45, 'c', '0.918274078066'),
  (32, '0', 46, 'a', '0.02522'),
  (33, '0', 46, 'b', '0'),
  (34, '0', 47, 'a', '0.1'),
  (35, '0', 47, 'b', '0'),
  (36, '0', 48, 'a', '0.4580153'),
  (37, '0', 48, 'b', '-88.229'),
  (38, '0', 49, 'a', '0.038168'),
  (39, '0', 49, 'b', '-7.319'),
  (40, '0', 50, 'a', '1'),
  (41, '0', 50, 'b', '0'),
  (42, '0', 51, 'a', '1'),
  (43, '0', 51, 'b', '0'),
  (44, '0', 52, 'a', '1'),
  (45, '0', 52, 'b', '0'),
  (46, '0', 53, 'a', '1'),
  (47, '0', 53, 'b', '0'),
  (48, '0', 54, 'a', '1'),
  (49, '0', 54, 'b', '0'),
  (50, '0', 55, 'a', '1'),
  (51, '0', 55, 'b', '2'),
  (52, '0', 55, 'c', '1'),
  (53, '0', 56, 'a', '0.000003073992'),
  (54, '0', 56, 'b', '0.24204384'),
  (55, '0', 56, 'c', '-91.417648'),
  (56, '0', 57, 'a', '0.0178117'),
  (57, '0', 57, 'b', '-3.4589'),
  (58, '0', 58, 'a', '0.012766'),
  (59, '0', 58, 'b', '0'),
  (60, '0', 59, 'a', '1'),
  (61, '0', 59, 'b', '0'),
  (62, '0', 60, 'a', '1'),
  (63, '0', 60, 'b', '0'),
  (64, '0', 61, 'a', '0.1'),
  (65, '0', 61, 'b', '0'),
  (66, '0', 62, 'a', '1'),
  (67, '0', 62, 'b', '0'),
  (68, '0', 63, 'a', '0.1'),
  (69, '0', 63, 'b', '0'),
  (70, '0', 65, 'a', '1'),
  (71, '0', 65, 'b', '0'),
  (72, '0', 66, 'a', '0.01'),
  (73, '0', 66, 'b', '0'),
  (74, '0', 67, 'a', '0.0595'),
  (75, '0', 67, 'b', '-129.96'),
  (76, '0', 68, 'a', '0.1'),
  (77, '0', 68, 'b', '-40'),
  (78, '0', 68, 'c', '0.5'),
  (79, '0', 68, 'd', '-400'),
  (80, '0', 68, 'e', '900'),
  (81, '0', 69, 'a', '0.00000000344'),
  (82, '0', 69, 'b', '-0.000022'),
  (83, '0', 69, 'c', '0.0584'),
  (84, '0', 69, 'd', '-5.3'),
  (85, '0', 70, 'a', '0.01'),
  (86, '0', 70, 'b', '0'),
  (87, '0', 70, 'c', '0.05'),
  (88, '0', 70, 'd', '-28'),
  (89, '0', 70, 'e', '700'),
  (90, '0', 71, 'a', '0.01221'),
  (91, '0', 71, 'b', '150'),
  (92, '0', 72, 'a', '0.43956044'),
  (93, '0', 72, 'b', '0'),
  (94, '0', 73, 'a', '0.1'),
  (95, '0', 73, 'b', '0'),
  (96, '0', 74, 'a', '0.1'),
  (97, '0', 74, 'b', '0'),
  (98, '0', 75, 'a', '0.014060992'),
  (99, '0', 75, 'b', '-173.35'),
  (100,'0',76,'a','0.000001515197755317'),
  (101, '0', 76, 'b', '0'),
  (102,'0',77,'a','0.000001515197755317'),
  (103, '0', 77, 'b', '0'),
  (104,'0',78, 'a','0.000001515197755317'),
  (105, '0', 78, 'b', '0'),
  (106, '0', 79, 'a', '0.0003111004'),
  (107, '0', 79, 'b', '0.6857831575'),
  (108, '0', 80, 'a', '1'),
  (109, '0', 80, 'b', '0'),
  (110, '0', 81, 'a', '1'),
  (111, '0', 81, 'b', '0'),
  (112, '0', 82, 'a', '0.00023675'),
  (113, '0', 82, 'b', '-1.25'),
  (114, '0', 83, 'a', '0.001786'),
  (115, '0', 83, 'b', '-0.815314'),
  (116, '0', 84, 'a', '0.02'),
  (117, '0', 84, 'b', '0'),
  (118, '0', 85, 'a', '0.495241'),
  (119, '0', 85, 'b', '-28.04'),
  (120, '0', 86, 'a', '0.01'),
  (121, '0', 86, 'b', '0'),
  (122,'0',87,'a','0.000305203723485426'),
  (123, '0', 87, 'b', '0'),
  (124,'0',88,'a','0.000305203723485426'),
  (125, '0', 88, 'b', '0'),
  (126, '0', 89, 'a', '1'),
  (127, '0', 89, 'b', '0'),
  (132, '0', 12283, 'a', '1'),
  (133, '0', 12283, 'b', '0'),
  (134, '0', 12284, 'a', '1'),
  (135, '0', 12284, 'b', '0'),
  (136, '0', 12285, 'a', '1'),
  (137, '0', 12285, 'b', '0'),
  (138, '0', 12286, 'a', '1'),
  (139, '0', 12286, 'b', '0'),
  (140, '0', 12287, 'a', '1'),
  (141, '0', 12287, 'b', '0'),
  (144, '0', 35, 'a', '0.000036'),
  (145, '0', 35, 'b', '0'),
  (146, '0', 2048, 'a', '1'),
  (147, '0', 2048, 'b', '0'),
  (148, '0', 90, 'a', '0.00006781684'),
  (149, '0', 90, 'b', '0'),
  (150, '0', 91, 'a', '1'),
  (151, '0', 91, 'b', '0'),
  (152, '0', 2049, 'a', '1'),
  (153, '0', 2049, 'b', '0'),
  (154, '0', 2050, 'a', '1'),
  (155, '0', 2050, 'b', '0'),
  (156, '0', 2051, 'a', '1'),
  (157, '0', 2051, 'b', '0'),
  (158, '0', 2052, 'a', '0'),
  (159, '0', 2052, 'b', '1'),
  (160, '0', 2052, 'c', '0'),
  (161, '0', 2053, 'a', '0'),
  (162, '0', 2053, 'b', '1'),
  (163, '0', 2053, 'c', '0'),
  (164, '0', 2054, 'a', '0'),
  (165, '0', 2054, 'b', '1'),
  (166, '0', 2054, 'c', '0'),
  (167, '0', 2055, 'a', '0'),
  (168, '0', 2055, 'b', '1'),
  (169, '0', 2055, 'c', '0'),
  (170, '0', 92, 'a', '1'),
  (171, '0', 92, 'b', '0'),
  (172, '0', 93, 'a', '1'),
  (173, '0', 93, 'b', '0'),
  (174, '0', 94, 'a', '1'),
  (175, '0', 94, 'b', '0'),
  (176, '0', 2056, 'a', '1'),
  (177, '0', 2056, 'b', '0'),
  (178, '0', 95, 'a', '0'),
  (179, '0', 95, 'b', '1'),
  (180, '0', 95, 'c', '0'),
  (181, '0', 2060, 'a', '1'),
  (182, '0', 2061, 'a', '1'),
  (183, '0', 2062, 'a', '1'),
  (184, '0', 2063, 'a', '1'),
  (185, '0', 2064, 'a', '1'),
  (186, '0', 2065, 'a', '1'),
  (187, '0', 2066, 'a', '1'),
  (188, '0', 2067, 'a', '1'),
  (189, '0', 2068, 'a', '1'),
  (190, '0', 2069, 'a', '1'),
  (191, '0', 2070, 'a', '1'),
  (192, '0', 2060, 'b', '0'),
  (193, '0', 2061, 'b', '0'),
  (194, '0', 2062, 'b', '0'),
  (195, '0', 2063, 'b', '0'),
  (196, '0', 2064, 'b', '0'),
  (197, '0', 2065, 'b', '0'),
  (198, '0', 2066, 'b', '0'),
  (199, '0', 2067, 'b', '0'),
  (200, '0', 2068, 'b', '0'),
  (201, '0', 2069, 'b', '0'),
  (202, '0', 2070, 'b', '0'),
  (212, '0', 2057, 'a', '1'),
  (213, '0', 2057, 'b', '0'),
  (214, '0', 2058, 'a', '1'),
  (215, '0', 2058, 'b', '0'),
  (216, '0', 2059, 'a', '1'),
  (217, '0', 2059, 'b', '0'),
  (218, '0', 2071, 'a', '1'),
  (219, '0', 2071, 'b', '0'),
  (220, '0', 2072, 'a', '1'),
  (221, '0', 2072, 'b', '0'),
  (222, '0', 2073, 'a', '1'),
  (223, '0', 2073, 'b', '0'),
  (224, '0', 3072, 'a', '1'),
  (225, '0', 3072, 'b', '0'),
  (226, '0', 3073, 'a', '1'),
  (227, '0', 3073, 'b', '0'),
  (228, '0', 3074, 'a', '1'),
  (229, '0', 3074, 'b', '0'),
  (230, '0', 3075, 'a', '1'),
  (231, '0', 3075, 'b', '0'),
  (232, '0', 3076, 'a', '1'),
  (233, '0', 3076, 'b', '0'),
  (234, '0', 3077, 'a', '1'),
  (235, '0', 3077, 'b', '0'),
  (236, '0', 98, 'a', '1'),
  (237, '0', 99, 'a', '1'),
  (238, '0', 100, 'a', '1'),
  (239, '0', 101, 'a', '1'),
  (240, '0', 102, 'a', '1'),
  (241, '0', 103, 'a', '1'),
  (242, '0', 104, 'a', '1'),
  (243, '0', 105, 'a', '1'),
  (244, '0', 106, 'a', '1'),
  (245, '0', 107, 'a', '1'),
  (246, '0', 108, 'a', '1'),
  (247, '0', 109, 'a', '1'),
  (248, '0', 98, 'b', '0'),
  (249, '0', 99, 'b', '0'),
  (250, '0', 100, 'b', '0'),
  (251, '0', 101, 'b', '0'),
  (252, '0', 102, 'b', '0'),
  (253, '0', 103, 'b', '0'),
  (254, '0', 104, 'b', '0'),
  (255, '0', 105, 'b', '0'),
  (256, '0', 106, 'b', '0'),
  (257, '0', 107, 'b', '0'),
  (258, '0', 108, 'b', '0'),
  (259, '0', 109, 'b', '0'),
  (267, '0', 41, 'c', '0.06926'),
  (268, '0', 41, 'd', '-0.96964'),
  (269, '0', 41, 'e', '32768'),
  (270, '0', 42, 'c', '0.1283'),
  (271, '0', 42, 'd', '-0.0007'),
  (272, '0', 42, 'e', '32768'),
  (1102, '0', 2074, 'a', '1'),
  (1103, '0', 2074, 'b', '0');

CREATE TABLE `m_control_module_switch` (
  `id` VARCHAR (36) NOT NULL DEFAULT '',
  `node_id` VARCHAR (20) NOT NULL COMMENT '控制模块ID',
  `route` INT (11) NOT NULL DEFAULT '1' COMMENT '路数，从 1 开始',
  `alias` VARCHAR (50) NOT NULL DEFAULT '' COMMENT '别名/备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `node_id` (`node_id`, `route`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块端口表';

CREATE TABLE `m_control_module_status` (
  `id` VARCHAR (36) NOT NULL DEFAULT '',
  `node_id` VARCHAR (20) NOT NULL COMMENT '控制模块ID',
  `enable` INT (11) NOT NULL DEFAULT '0' COMMENT '启用状态, 从低位开始, 1 启用，0 禁用',
  `switch` INT (11) NOT NULL DEFAULT '0' COMMENT '开关状态, 从低位开始, 1 开，0 关',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块状态表';

CREATE TABLE `m_control_module_switch_change` (
  `id` VARCHAR (36) NOT NULL DEFAULT '',
  `node_id` VARCHAR (20) NOT NULL COMMENT '控制模块ID',
  `enable_before` INT (11) NOT NULL DEFAULT '0' COMMENT '启用状态, 从低位开始, 1 启用，0 禁用',
  `switch_before` INT (11) NOT NULL DEFAULT '0' COMMENT '开关状态, 从低位开始, 1 开，0 关',
  `create_time_before` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `enable_after` INT (11) NOT NULL DEFAULT '0' COMMENT '启用状态, 从低位开始, 1 启用，0 禁用',
  `switch_after` INT (11) NOT NULL DEFAULT '0' COMMENT '开关状态, 从低位开始, 1 开，0 关',
  `create_time_after` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `route` INT (11) NOT NULL DEFAULT '1' COMMENT '变化线路',
  `action` INT (11) NOT NULL DEFAULT '0' COMMENT '动作：1. 开, 0. 关',
  `actionDriver` INT (11) NOT NULL COMMENT '动作模式，1 条件反射， 0 正常',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块状态变化表';

CREATE TABLE `m_control_module_sensor_action` (
  `id` VARCHAR (36) NOT NULL COMMENT 'uuid',
  `controlModuleId` VARCHAR (20) NOT NULL COMMENT '控制模块ID uuid',
  `route` INT (11) NOT NULL COMMENT '路数',
  `action` INT (11) NOT NULL COMMENT '满足条件的动作 0关1开',
  `logic` INT (11) NOT NULL DEFAULT '1' COMMENT '逻辑关系 1与，2或',
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `controlModuleId_route` (`route`, `controlModuleId`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块条件逻辑表';

CREATE TABLE `m_control_module_sensor_condition` (
  `id` VARCHAR (36) NOT NULL COMMENT 'uuid',
  `sensorActionId` VARCHAR (36) NOT NULL COMMENT ',逻辑Id uuid 外键',
  `deviceId` VARCHAR (20) NOT NULL COMMENT '设备ID',
  `sensorId` INT (11) NOT NULL COMMENT '监测ID',
  `operator` INT (11) NOT NULL COMMENT '条件 1>,2<,3=',
  `value` DOUBLE NOT NULL COMMENT '条件的值',
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `sensorActionId` (`sensorActionId`),
  CONSTRAINT `m_control_module_sensor_condition_ibfk_1` FOREIGN KEY (`sensorActionId`) REFERENCES `m_control_module_sensor_action` (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块条件动作表';

CREATE TABLE `m_control_module_condition_rfl` (
  `id` VARCHAR (36) NOT NULL DEFAULT '',
  `node_id` VARCHAR (20) NOT NULL COMMENT '控制模块ID',
  `route` INT (11) NOT NULL DEFAULT '1' COMMENT '路数，从 1 开始',
  `sub_node_id` VARCHAR (20) DEFAULT NULL COMMENT '参与条件反射设备ID',
  `sensorId` INT (11) DEFAULT NULL COMMENT '监测指标标识',
  `lowLeft` INT (11) DEFAULT NULL COMMENT '低阈值 左值',
  `low` INT (11) DEFAULT NULL COMMENT '低阈值',
  `lowRight` INT (11) DEFAULT NULL COMMENT '低阈值 右值',
  `lowTarget` DOUBLE DEFAULT '0' COMMENT '低阈值的结果',
  `highLeft` INT (11) DEFAULT NULL COMMENT '高阈值 左值',
  `high` INT (11) DEFAULT NULL COMMENT '高阈值',
  `highRight` INT (11) DEFAULT NULL COMMENT '高阈值 右值',
  `highTarget` DOUBLE DEFAULT '0' COMMENT '高阈值的结果',
  `action` INT (11) NOT NULL DEFAULT '8' COMMENT '动作, 0 无条件关; 2 范围内开，范围外关; 3 高于高阈值开，低于低阈值关; 4 高于高阈值关，低于低阈值开;5 范围外关，范围外开;7 无条件开;8 无条件反射',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `node_id` (`node_id`, `route`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '条件反射参数';

CREATE TABLE `m_control_module_action_daily` (
  `id` VARCHAR (36) NOT NULL COMMENT 'uuid',
  `deviceId` VARCHAR (20) NOT NULL COMMENT 'uuid 设备id',
  `route` INT (11) NOT NULL COMMENT '路数',
  `time` TIME NOT NULL COMMENT '定时',
  `action` INT (11) NOT NULL COMMENT '动作 1开 0关',
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块定时动作表';

CREATE TABLE `m_control_module_action_interval` (
  `id` VARCHAR (36) NOT NULL COMMENT 'uuid',
  `deviceId` VARCHAR (20) NOT NULL COMMENT 'uuid 设备id',
  `route` INT (11) NOT NULL COMMENT '路数',
  `intervalTime` INT (11) NOT NULL COMMENT '间隔时间 单位:秒',
  `executionTime` INT (11) NOT NULL COMMENT '执行时间 单位:秒',
  `action` INT (11) NOT NULL COMMENT '动作 1开 0关',
  `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块周期动作表';

CREATE TABLE `m_control_module_notification` (
  `id` VARCHAR (36) NOT NULL COMMENT '主键uuid',
  `siteId` VARCHAR (8) NOT NULL COMMENT '站点ID',
  `userId` INT (11) NOT NULL COMMENT '用户id',
  `subscribeType` INT (2) NOT NULL COMMENT '全部设备或者自定义设备，全部设备存1，自定义设备存2',
  `triggerEvent` INT (2) NOT NULL COMMENT '发送事件：1电池供电，2开关切换，3全选',
  `notifyMethod` INT (2) NOT NULL COMMENT '通知方式：1短信，2邮件，3全选',
  `beginTime` DATE DEFAULT NULL COMMENT '免打扰时间段，开始时间',
  `endTime` DATE DEFAULT NULL COMMENT '免打扰时间段，结束时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `siteId` (`siteId`, `userId`),
  KEY `userId` (`userId`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块通知设置';

CREATE TABLE `m_control_module_notification_device` (
  `id` VARCHAR (36) NOT NULL COMMENT '主键uuid',
  `notifyId` VARCHAR (36) NOT NULL COMMENT '外键',
  `deviceId` VARCHAR (20) NOT NULL COMMENT '控制模块Id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `notifyId` (`notifyId`, `deviceId`),
  CONSTRAINT `m_control_module_notification_device_ibfk_1` FOREIGN KEY (`notifyId`) REFERENCES `m_control_module_notification` (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '控制模块通知设置--选择控制模块';

CREATE TABLE `m_tbl_rb_hour_acc` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `rb` DOUBLE DEFAULT '0' COMMENT '降雨量',
  `ms_datetime` DATETIME DEFAULT NULL COMMENT '记录时间',
  `isupdate` INT (11) DEFAULT '0' COMMENT '是否修改',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '小时降雨量表';

CREATE TABLE `m_tbl_rb_day_acc` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `rb` DOUBLE DEFAULT '0' COMMENT '降雨量',
  `ms_date` DATE DEFAULT NULL COMMENT '记录日期',
  `isupdate` INT (11) DEFAULT '0' COMMENT '是否修改',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '日降水量 信息统计表';

CREATE TABLE `m_tbl_evap_hour_acc` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `evap` DOUBLE DEFAULT '0' COMMENT '蒸发量',
  `ms_datetime` DATETIME DEFAULT NULL COMMENT '记录时间',
  `isupdate` INT (11) DEFAULT '0' COMMENT '是否修改',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本'
) ENGINE = INNODB DEFAULT CHARSET = utf8;

CREATE TABLE `m_tbl_ip_hour_acc` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `locationId` VARCHAR (20) NOT NULL COMMENT '位置点id',
  `ip` DOUBLE DEFAULT '0' COMMENT '无机污染物',
  `ms_datetime` DATETIME DEFAULT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '小时无机污染物表';

CREATE TABLE `m_tbl_op_hour_acc` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `locationId` VARCHAR (20) NOT NULL COMMENT '位置点id',
  `op` DOUBLE DEFAULT '0' COMMENT '有机污染物',
  `ms_datetime` DATETIME DEFAULT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '小时有机污染物表';

CREATE TABLE `m_tbl_sp_hour_acc` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `locationId` VARCHAR (20) NOT NULL COMMENT '位置点id',
  `sp` DOUBLE DEFAULT '0' COMMENT '含硫污染物',
  `ms_datetime` DATETIME DEFAULT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '小时含硫污染物表';

CREATE TABLE `m_tbl_lxh_acc` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `lx_h` DOUBLE DEFAULT '0' COMMENT '日照量',
  `ms_datetime` DATETIME DEFAULT NULL COMMENT '记录时间',
  `season` INT (11) DEFAULT NULL COMMENT '季度',
  `isupdate` INT (11) DEFAULT '0' COMMENT '是否修改',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = 'LXH光照日统计表';

CREATE TABLE `m_windmark` (
  `windmark` VARCHAR (10) DEFAULT NULL COMMENT '存放风向16个极坐标的角度值'
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '玫瑰风向标表';

INSERT INTO `m_windmark` (`windmark`)
VALUES
  ('0'),
  ('22.5'),
  ('45'),
  ('67.5'),
  ('90'),
  ('112.5'),
  ('135'),
  ('157.5'),
  ('180'),
  ('202.5'),
  ('225'),
  ('247.5'),
  ('270'),
  ('292.5'),
  ('315'),
  ('337.5');

CREATE TABLE `m_windrose` (
  `id` VARCHAR (50) NOT NULL COMMENT '唯一标识uuid',
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `O_N` DOUBLE DEFAULT '0' COMMENT '北风风向统计值',
  `S_N` DOUBLE DEFAULT '0' COMMENT '北风风速平均值',
  `O_NNE` DOUBLE DEFAULT '0' COMMENT '东北偏北风风向统计值',
  `S_NNE` DOUBLE DEFAULT '0' COMMENT '东北偏北风风速平均值',
  `O_NE` DOUBLE DEFAULT '0' COMMENT '东北风风向统计值',
  `S_NE` DOUBLE DEFAULT '0' COMMENT '东北风风速平均值',
  `O_ENE` DOUBLE DEFAULT '0' COMMENT '东北偏东风风向统计值',
  `S_ENE` DOUBLE DEFAULT '0' COMMENT '东北偏东风风速平均值',
  `O_E` DOUBLE DEFAULT '0' COMMENT '东风风向统计值',
  `S_E` DOUBLE DEFAULT '0' COMMENT '东风风速平均值',
  `O_ESE` DOUBLE DEFAULT '0' COMMENT '东南偏东风风向统计值',
  `S_ESE` DOUBLE DEFAULT '0' COMMENT '东南偏东风风速平均值',
  `O_SE` DOUBLE DEFAULT '0' COMMENT '东南风风向统计值',
  `S_SE` DOUBLE DEFAULT '0' COMMENT '东南风风速平均值',
  `O_SSE` DOUBLE DEFAULT '0' COMMENT '东南偏南风风向统计值',
  `S_SSE` DOUBLE DEFAULT '0' COMMENT '东南偏南风风速平均值',
  `O_S` DOUBLE DEFAULT '0' COMMENT '南风风向统计值',
  `S_S` DOUBLE DEFAULT '0' COMMENT '南风风速平均值',
  `O_SSW` DOUBLE DEFAULT '0' COMMENT '西南偏南风风向统计值',
  `S_SSW` DOUBLE DEFAULT '0' COMMENT '西南偏南风风速平均值',
  `O_SW` DOUBLE DEFAULT '0' COMMENT '西南风风向统计值',
  `S_SW` DOUBLE DEFAULT '0' COMMENT '西南风风速平均值',
  `O_WSW` DOUBLE DEFAULT '0' COMMENT '西南偏西风风向统计值',
  `S_WSW` DOUBLE DEFAULT '0' COMMENT '西南偏西风风速平均值',
  `O_W` DOUBLE DEFAULT '0' COMMENT '西风风向统计值',
  `S_W` DOUBLE DEFAULT '0' COMMENT '西风风速平均值',
  `O_WNW` DOUBLE DEFAULT '0' COMMENT '西北偏西风风向统计值',
  `S_WNW` DOUBLE DEFAULT '0' COMMENT '西北偏西风风速平均值',
  `O_NW` DOUBLE DEFAULT '0' COMMENT '西北风风向统计值',
  `S_NW` DOUBLE DEFAULT '0' COMMENT '西北风风速平均值',
  `O_NNW` DOUBLE DEFAULT '0' COMMENT '西北偏北风风向统计值',
  `S_NNW` DOUBLE DEFAULT '0' COMMENT '西北偏北风风速平均值',
  `windcalm` DOUBLE DEFAULT '0' COMMENT '风速小于0.2m/s时为静风',
  `sum` INT (11) DEFAULT '0' COMMENT '当天风向总数',
  `ms_date` DATE DEFAULT NULL COMMENT '日期',
  `season` INT (11) NOT NULL COMMENT '季度',
  `isupdate` INT (11) DEFAULT '0' COMMENT '是否更新',
  `dataVersion` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '风向玫瑰图数据表';

CREATE TABLE `m_pm_sensor_data` (
  `id` INT (11) NOT NULL AUTO_INCREMENT,
  `nodeid` VARCHAR (20) NOT NULL COMMENT '产品入网唯一标识',
  `sensorPhysicalid` INT (11) NOT NULL COMMENT '传感标识',
  `sensorPhysicalvalue` VARCHAR (30) NOT NULL COMMENT '传感值',
  `lowvoltage` FLOAT NOT NULL DEFAULT '0' COMMENT '电压值(默认为0)',
  `createtime` DATETIME NOT NULL COMMENT '创建时间',
  `state` INT (11) NOT NULL COMMENT '传感状态0：采样失败,1：采样正常,2：低于低阀值,3：超过高阀值,4：空数据',
  `dataVersion` INT (11) DEFAULT '0' COMMENT '数据版本',
  `anomaly` INT (11) NOT NULL DEFAULT '0' COMMENT '-1：超时,0：正常,1：低压,2：掉电',
  PRIMARY KEY (`id`),
  KEY `createtime` (`createtime`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = 'pm传感数据表';

CREATE TABLE `m_float_sensor` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `device_id` VARCHAR (13) NOT NULL COMMENT '设备id',
  `sensor_id` INT (11) NOT NULL COMMENT '监测指标id',
  `max_up_float` DOUBLE DEFAULT '0' COMMENT '上限浮动值向上浮动',
  `min_down_float` DOUBLE DEFAULT '0' COMMENT '下限浮动值向下浮动',
  `min_up_float` DOUBLE DEFAULT '0' COMMENT '下限浮动值向上浮动',
  PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 113 DEFAULT CHARSET = utf8 COMMENT = '设备监测指标上下限浮动值';

INSERT INTO `m_float_sensor` (
  `id`,
  `device_id`,
  `sensor_id`,
  `max_up_float`,
  `min_down_float`,
  `min_up_float`
)
VALUES
  (1, '0', 32, 0, 0, 0),
  (2, '0', 33, 0, 0, 0),
  (3, '0', 34, 0, 0, 0),
  (4, '0', 35, 0, 0, 0),
  (5, '0', 36, 0, 0, 0),
  (6, '0', 37, 0, 0, 0),
  (7, '0', 38, 0, 0, 0),
  (8, '0', 39, 0, 0, 0),
  (9, '0', 40, 0, 0, 0),
  (10, '0', 41, 0, 0, 0),
  (11, '0', 42, 0, 0, 0),
  (12, '0', 43, 0, 0, 0),
  (13, '0', 44, 0, 0, 0),
  (14, '0', 45, 0, 0, 0),
  (15, '0', 46, 0, 0, 0),
  (16, '0', 47, 0, 0, 0),
  (17, '0', 48, 0, 0, 0),
  (18, '0', 49, 0, 0, 0),
  (19, '0', 50, 0, 0, 0),
  (20, '0', 51, 0, 0, 0),
  (21, '0', 52, 0, 0, 0),
  (22, '0', 53, 0, 0, 0),
  (23, '0', 54, 0, 0, 0),
  (24, '0', 55, 0, 0, 0),
  (25, '0', 56, 0, 0, 0),
  (26, '0', 57, 0, 0, 0),
  (27, '0', 58, 0, 0, 0),
  (28, '0', 59, 0, 0, 0),
  (29, '0', 60, 0, 0, 0),
  (30, '0', 61, 0, 0, 0),
  (31, '0', 62, 0, 0, 0),
  (32, '0', 63, 0, 0, 0),
  (33, '0', 65, 0, 0, 0),
  (34, '0', 66, 0, 0, 0),
  (35, '0', 67, 0, 0, 0),
  (36, '0', 68, 0, 0, 0),
  (37, '0', 69, 0, 0, 0),
  (38, '0', 70, 0, 0, 0),
  (39, '0', 71, 0, 0, 0),
  (40, '0', 72, 0, 0, 0),
  (41, '0', 73, 0, 0, 0),
  (42, '0', 74, 0, 0, 0),
  (43, '0', 75, 0, 0, 0),
  (44, '0', 76, 0, 0, 0),
  (45, '0', 77, 0, 0, 0),
  (46, '0', 78, 0, 0, 0),
  (47, '0', 79, 0, 0, 0),
  (48, '0', 80, 0, 0, 0),
  (49, '0', 81, 0, 0, 0),
  (50, '0', 82, 0, 0, 0),
  (51, '0', 83, 0, 0, 0),
  (52, '0', 84, 0, 0, 0),
  (53, '0', 85, 0, 0, 0),
  (54, '0', 86, 0, 0, 0),
  (55, '0', 87, 0, 0, 0),
  (56, '0', 88, 0, 0, 0),
  (57, '0', 89, 0, 0, 0),
  (58, '0', 90, 0, 0, 0),
  (59, '0', 91, 0, 0, 0),
  (60, '0', 92, 0, 0, 0),
  (61, '0', 93, 0, 0, 0),
  (62, '0', 94, 0, 0, 0),
  (63, '0', 95, 0, 0, 0),
  (64, '0', 98, 0, 0, 0),
  (65, '0', 99, 0, 0, 0),
  (66, '0', 100, 0, 0, 0),
  (67, '0', 101, 0, 0, 0),
  (68, '0', 102, 0, 0, 0),
  (69, '0', 103, 0, 0, 0),
  (70, '0', 104, 0, 0, 0),
  (71, '0', 105, 0, 0, 0),
  (72, '0', 106, 0, 0, 0),
  (73, '0', 107, 0, 0, 0),
  (74, '0', 108, 0, 0, 0),
  (75, '0', 109, 0, 0, 0),
  (76, '0', 2048, 0, 0, 0),
  (77, '0', 2049, 0, 0, 0),
  (78, '0', 2050, 0, 0, 0),
  (79, '0', 2051, 0, 0, 0),
  (80, '0', 2052, 0, 0, 0),
  (81, '0', 2053, 0, 0, 0),
  (82, '0', 2054, 0, 0, 0),
  (83, '0', 2055, 0, 0, 0),
  (84, '0', 2056, 0, 0, 0),
  (85, '0', 2057, 0, 0, 0),
  (86, '0', 2058, 0, 0, 0),
  (87, '0', 2059, 0, 0, 0),
  (88, '0', 2060, 0, 0, 0),
  (89, '0', 2061, 0, 0, 0),
  (90, '0', 2062, 0, 0, 0),
  (91, '0', 2063, 0, 0, 0),
  (92, '0', 2064, 0, 0, 0),
  (93, '0', 2065, 0, 0, 0),
  (94, '0', 2066, 0, 0, 0),
  (95, '0', 2067, 0, 0, 0),
  (96, '0', 2068, 0, 0, 0),
  (97, '0', 2069, 0, 0, 0),
  (98, '0', 2070, 0, 0, 0),
  (99, '0', 2071, 0, 0, 0),
  (100, '0', 2072, 0, 0, 0),
  (101, '0', 2073, 0, 0, 0),
  (102, '0', 3072, 0, 0, 0),
  (103, '0', 3073, 0, 0, 0),
  (104, '0', 3074, 0, 0, 0),
  (105, '0', 3075, 0, 0, 0),
  (106, '0', 3076, 0, 0, 0),
  (107, '0', 3077, 0, 0, 0),
  (108, '0', 12283, 0, 0, 0),
  (109, '0', 12284, 0, 0, 0),
  (110, '0', 12285, 0, 0, 0),
  (111, '0', 12286, 0, 0, 0),
  (112, '0', 12287, 0, 0, 0);

CREATE TABLE `m_replace_sensor` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `locationId` VARCHAR (20) NOT NULL COMMENT '位置点id',
  `replaceDate` DATETIME NOT NULL COMMENT '更换时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '更换探头记录表';

CREATE TABLE `m_emptyrecord` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `nodeId` VARCHAR (30) NOT NULL COMMENT '设备编号',
  `stamp` DATETIME NOT NULL COMMENT '时间戳(小时)',
  `gatewaySuccess` INT (11) NOT NULL DEFAULT '0' COMMENT '网关成功次数',
  `dataCacheSuccess` INT (11) NOT NULL DEFAULT '0' COMMENT '缓存成功次数',
  PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 79985 DEFAULT CHARSET = utf8 COMMENT = '空数据记录表';

CREATE TABLE `m_threshold` (
  `id` INT (11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `locationId` VARCHAR (50) NOT NULL COMMENT '位置点ID',
  `sensorPhysicalId` INT (11) NOT NULL COMMENT '监测指标id',
  `conditionType` INT (11) DEFAULT NULL COMMENT '达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于。与目标值/浮动值有关',
  `target` FLOAT DEFAULT NULL COMMENT '文保行业监测调控预期目标值',
  `floating` FLOAT DEFAULT NULL COMMENT '浮动值：以目标值为中心的浮动范围',
  `thresholdType` INT (1) NOT NULL COMMENT '类型0：位置点，1：区域，2：质地',
  `maxValue` FLOAT DEFAULT NULL COMMENT '上限',
  `minValue` FLOAT DEFAULT NULL COMMENT '下限',
  `nodeId` VARCHAR (20) DEFAULT NULL COMMENT '设备id',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_80` (`sensorPhysicalId`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;

DELIMITER $$

CREATE FUNCTION `getParentId` (NodeId VARCHAR (1000)) RETURNS VARCHAR (1000) CHARSET utf8 DETERMINISTIC
BEGIN
  DECLARE sTemp VARCHAR (4000);
  DECLARE sTempPar VARCHAR (4000);
  SET sTemp = '$';
  SET sTempPar = NodeId;
  WHILE
    sTempPar IS NOT NULL DO SET sTemp = CONCAT(sTemp, ',', sTempPar);
    SELECT
      GROUP_CONCAT(t.p_siteid) INTO sTempPar
    FROM
      mapping_area_site t
    WHERE t.p_siteid <> t.siteId
      AND FIND_IN_SET(siteId, sTempPar) > 0;
  END WHILE;
  RETURN sTemp;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_threshold_filter` (
  deviceid_in CHAR(10),
  sensorid_in INT,
  value_in DOUBLE
) RETURNS INT (11)
BEGIN
  DECLARE state INT;
  SELECT
    TYPE INTO state
  FROM
    m_threshold
  WHERE (
      (value_in >= VALUE
        AND TYPE = 3)
      OR (value_in <= VALUE
        AND TYPE = 2)
    )
    AND nodeid = deviceid_in
    AND sensorid = sensorid_in;
  IF state IS NULL
  THEN SET state = 1;
  END IF;
  RETURN state;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_pt100_mapping` (i_ohm DOUBLE) RETURNS DOUBLE
BEGIN
  DECLARE r_temp DOUBLE;
  SELECT
    COUNT(1),
    MAX(temp) INTO @tmp_count,
    @tmp_temp
  FROM
    m_tbl_pt100_mapping
  WHERE ohm = i_ohm;
  SELECT
    COUNT(1) INTO @min_count
  FROM
    m_tbl_pt100_mapping
  WHERE ohm <= i_ohm;
  SELECT
    COUNT(1) INTO @max_count
  FROM
    m_tbl_pt100_mapping
  WHERE ohm >= i_ohm;
  IF @tmp_count = 1
  THEN SET r_temp = @tmp_temp;
  ELSEIF @min_count = 0
  THEN
  SELECT
    MIN(temp) INTO r_temp
  FROM
    m_tbl_pt100_mapping;
  ELSEIF @max_count = 0
  THEN
  SELECT
    MAX(temp) INTO r_temp
  FROM
    m_tbl_pt100_mapping;
  ELSE
  SELECT
    ohm,
    temp INTO @R1,
    @T1
  FROM
    m_tbl_pt100_mapping
  WHERE ohm <= i_ohm
  ORDER BY id DESC
  LIMIT 0, 1;
  SELECT
    ohm,
    temp INTO @R2,
    @T2
  FROM
    m_tbl_pt100_mapping
  WHERE ohm >= i_ohm
  LIMIT 0, 1;
  SET r_temp = ROUND(
    ((i_ohm - @R1) * (@T2 - @T1) / (@R2 - @R1)) + @T1,
    1
  );
  END IF;
  RETURN r_temp;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_precision_filter` (
  sensorPhysicalid_in INT,
  value_in DOUBLE
) RETURNS DOUBLE
BEGIN
  DECLARE _sensorprec INT;
  SET _sensorprec = 0;
  SELECT
    sensorPrecision INTO _sensorprec
  FROM
    m_sensorinfo
  WHERE sensorPhysicalid = sensorPhysicalid_in;
  RETURN ROUND(value_in, _sensorprec);
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_isExistWindRose` (
  deviceid_in VARCHAR (20),
  date_in DATE
) RETURNS TINYINT (1)
BEGIN
  DECLARE isok BOOL DEFAULT FALSE;
  DECLARE count_i INT;
  SELECT
    COUNT(1) INTO count_i
  FROM
    m_windrose
  WHERE nodeid = deviceid_in
    AND ms_date = date_in;
  IF count_i > 0
  THEN SET isok = TRUE;
  END IF;
  RETURN isok;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_isExistTable` (tablename_in VARCHAR (50)) RETURNS TINYINT (1)
BEGIN
  DECLARE isExist BOOL;
  DECLARE dbName VARCHAR (50);
  SELECT
    DATABASE() INTO dbName;
  SELECT
    COUNT(1) INTO isExist
  FROM
    `information_schema`.`TABLES` t
  WHERE t.TABLE_NAME = tablename_in
    AND t.TABLE_SCHEMA = dbName;
  RETURN isExist;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_isExistHourRb` (
  deviceid_in VARCHAR (20),
  datetime_in DATETIME
) RETURNS TINYINT (1)
BEGIN
  DECLARE isok BOOL DEFAULT FALSE;
  DECLARE count_i INT;
  SELECT
    COUNT(1) INTO count_i
  FROM
    m_tbl_rb_hour_acc
  WHERE nodeid = deviceid_in
    AND DATE_FORMAT(ms_datetime, '%Y-%m-%d %H') = DATE_FORMAT(datetime_in, '%Y-%m-%d %H');
  IF count_i > 0
  THEN SET isok = TRUE;
  END IF;
  RETURN isok;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_isExistHourLux` (
  deviceid_in VARCHAR (20),
  datetime_in DATETIME
) RETURNS TINYINT (1)
BEGIN
  DECLARE isok BOOL DEFAULT FALSE;
  DECLARE count_i INT;
  SELECT
    COUNT(1) INTO count_i
  FROM
    m_tbl_lxh_acc
  WHERE nodeid = deviceid_in
    AND DATE_FORMAT(ms_datetime, '%Y-%m-%d %H') = DATE_FORMAT(datetime_in, '%Y-%m-%d %H');
  IF count_i > 0
  THEN SET isok = TRUE;
  END IF;
  RETURN isok;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_isExistDateRb` (
  deviceid_in VARCHAR (20),
  date_in DATE
) RETURNS TINYINT (1)
BEGIN
  DECLARE isok BOOL DEFAULT FALSE;
  DECLARE count_i INT;
  SELECT
    COUNT(1) INTO count_i
  FROM
    m_tbl_rb_day_acc
  WHERE nodeid = deviceid_in
    AND ms_date = date_in;
  IF count_i > 0
  THEN SET isok = TRUE;
  END IF;
  RETURN isok;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_isExistAvg` (
  deviceid_in VARCHAR (50),
  date_in DATE,
  sensorPhysicalid_in INT
) RETURNS TINYINT (1)
BEGIN
  DECLARE isok BOOL DEFAULT FALSE;
  SELECT
    COUNT(1) INTO @count_i
  FROM
    m_avgdata
  WHERE nodeid = deviceid_in
    AND ms_date = date_in
    AND sensorPhysicalid = sensorPhysicalid_in;
  IF @count_i > 0
  THEN SET isok = TRUE;
  END IF;
  RETURN isok;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_getSeason` (date_in DATE) RETURNS INT (11)
BEGIN
  DECLARE month_i INT;
  DECLARE season_i INT;
  SELECT
    MONTH(date_in) INTO month_i;
  IF month_i >= 1 && month_i <= 3
  THEN SET season_i = 1;
  ELSEIF month_i >= 4 && month_i <= 6
  THEN SET season_i = 2;
  ELSEIF month_i >= 7 && month_i <= 9
  THEN SET season_i = 3;
  ELSE SET season_i = 4;
  END IF;
  RETURN season_i;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_getChildren` (id VARCHAR (50)) RETURNS TEXT CHARSET utf8
BEGIN
  DECLARE sTemp TEXT;
  DECLARE sTempChd TEXT;
  SET sTemp = "";
  SET sTempChd = CAST(id AS CHAR);
  WHILE
    sTempChd IS NOT NULL DO SET sTemp = CONCAT(sTemp, ',', sTempChd);
    SELECT
      GROUP_CONCAT(zoneId) INTO sTempChd
    FROM
      t_zone
    WHERE FIND_IN_SET(parentId, sTempChd) > 0;
  END WHILE;
  -- 注意这里截取字符串是因为最开始变量赋的是空串，第一次拼接会留下一个逗号
   SET sTemp = SUBSTRING(sTemp, 2);
  RETURN sTemp;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_getGatewayById` (deviceId VARCHAR (30)) RETURNS INT (11)
BEGIN
  DECLARE iSelfId,
  iParentId INT (11);
  SELECT
    parentIP INTO iSelfId
  FROM
    m_nodeinfomemory
  WHERE nodeid = deviceId
  LIMIT 1;
  SELECT
    parentIP INTO iParentId
  FROM
    m_nodeinfomemory
  WHERE childIP = iSelfId
  LIMIT 1;
  WHILE
    deviceId IS NOT NULL DO IF iParentId <> iSelfId
    THEN
    SELECT
      childIP INTO iSelfId
    FROM
      m_nodeinfomemory
    WHERE childIP = iParentId
    LIMIT 1;
    SELECT
      parentIP INTO iParentId
    FROM
      m_nodeinfomemory
    WHERE childIP = iParentId
    LIMIT 1;
    ELSE RETURN iParentId;
    END IF;
  END WHILE;
END $$

DELIMITER;

DELIMITER $$

CREATE FUNCTION `fun_getBaseLogicGroupChildren` (idi VARCHAR (50)) RETURNS TEXT CHARSET utf8
BEGIN
  DECLARE sTemp TEXT;
  DECLARE sTempChd TEXT;
  SET sTemp = "";
  SET sTempChd = CAST(idi AS CHAR);
  WHILE
    sTempChd IS NOT NULL DO SET sTemp = CONCAT(sTemp, ',', sTempChd);
    SELECT
      GROUP_CONCAT(id) INTO sTempChd
    FROM
      t_logicgroup
    WHERE FIND_IN_SET(parentLogicGroupId, sTempChd) > 0;
  END WHILE;
  SET sTemp = SUBSTRING(sTemp, 2);
  RETURN sTemp;
END $$

DELIMITER;

