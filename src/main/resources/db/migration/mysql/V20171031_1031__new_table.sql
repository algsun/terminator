--创建表log_transfer
--author bai.weixing


/*Table structure for table `log_transfer` */

DROP TABLE IF EXISTS `log_transfer`;

CREATE TABLE `log_transfer` (
  `tableName` varchar(100) NOT NULL COMMENT '数据同步的表名或视图名',
  `dataVersion` bigint(20) NOT NULL DEFAULT '0' COMMENT '每次同步成功之后+1',
  `eventTime` datetime DEFAULT NULL COMMENT '系统后台同步事件发生的时间',
  PRIMARY KEY (`tableName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据同步状态表'









