--创建表t_zone
--author bai.weixing


/*Table structure for table `t_zone` */

DROP TABLE IF EXISTS `t_zone`;

CREATE TABLE `t_zone` (
  `zoneId` varchar(50) NOT NULL COMMENT '监测点uuid',
  `siteId` varchar(50) NOT NULL COMMENT '区域所属站点id',
  `parentId` varchar(50) DEFAULT NULL COMMENT '父id为null的表明是顶级区域',
  `zoneName` varchar(50) DEFAULT NULL COMMENT '监测区域平面部署图， 主要用于设备在平面图进行部署',
  `planImage` varchar(500) DEFAULT NULL COMMENT '平面地图',
  `position` int(1) DEFAULT NULL,
  `dataVersion` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据同步版本',
  PRIMARY KEY (`zoneId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区域'










