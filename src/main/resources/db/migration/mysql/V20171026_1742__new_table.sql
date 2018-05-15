--创建表
--author bai.weixing


/*Table structure for table `sys_era` */

DROP TABLE IF EXISTS `sys_era`;

CREATE TABLE `sys_era` (
  `id` varchar(64) NOT NULL COMMENT '时代id',
  `name` varchar(20) NOT NULL COMMENT '时代名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_era` */

insert  into `sys_era`(`id`,`name`) values
('1','秦'),
('2','汉');

/*Table structure for table `sys_level` */

DROP TABLE IF EXISTS `sys_level`;

CREATE TABLE `sys_level` (
  `id` varchar(64) NOT NULL COMMENT '文物级别id',
  `name` varchar(20) NOT NULL COMMENT '文物级别名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_level` */

insert  into `sys_level`(`id`,`name`) values
('1','一级'),
('2','二级');

/*Table structure for table `sys_photo` */

DROP TABLE IF EXISTS `sys_photo`;

CREATE TABLE `sys_photo` (
  `id` varchar(64) NOT NULL COMMENT '照片id',
  `filmCode` varchar(50) DEFAULT NULL COMMENT '底片号',
  `photographer` varchar(20) DEFAULT NULL COMMENT '摄影师',
  `ratio` varchar(50) DEFAULT NULL COMMENT '图片比例',
  `path` varchar(100) NOT NULL COMMENT '图片路径',
  `create_by` varchar(64) NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新人',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文物照片';

/*Table structure for table `sys_texture` */

DROP TABLE IF EXISTS `sys_texture`;

CREATE TABLE `sys_texture` (
  `id` varchar(64) NOT NULL COMMENT '质地id',
  `name` varchar(20) NOT NULL COMMENT '质地名称',
  `enName` varchar(20) DEFAULT NULL COMMENT '质地英文名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_texture` */

insert  into `sys_texture`(`id`,`name`,`enName`) values
('1','石器',NULL),
('2','铜',NULL);


/*Table structure for table `sys_relic` */

DROP TABLE IF EXISTS `sys_relic`;

CREATE TABLE `sys_relic` (
  `id` varchar(64) NOT NULL COMMENT '文物id',
  `name` varchar(30) NOT NULL COMMENT '文物名称',
  `era_id` varchar(64) NOT NULL COMMENT '时代id',
  `texture_id` varchar(64) NOT NULL COMMENT '质地id',
  `level_id` varchar(64) NOT NULL COMMENT '级别id',
  `office_id` varchar(64) DEFAULT NULL COMMENT '机构id',
  `lng` double DEFAULT NULL COMMENT '经度',
  `lat` double DEFAULT NULL COMMENT '纬度',
  `photo_id` varchar(64) DEFAULT NULL COMMENT '照片id',
  `create_by` varchar(64) NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新人',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0.未删除 1.删除',
  PRIMARY KEY (`id`),
  KEY `sys_relic_ibfk_5` (`era_id`),
  KEY `sys_relic_ibfk_6` (`texture_id`),
  KEY `sys_relic_ibfk_7` (`level_id`),
  KEY `sys_relic_ibfk_8` (`office_id`),
  KEY `sys_relic_ibfk_9` (`photo_id`),
  CONSTRAINT `sys_relic_ibfk_5` FOREIGN KEY (`era_id`) REFERENCES `sys_era` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `sys_relic_ibfk_6` FOREIGN KEY (`texture_id`) REFERENCES `sys_texture` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `sys_relic_ibfk_7` FOREIGN KEY (`level_id`) REFERENCES `sys_level` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;







