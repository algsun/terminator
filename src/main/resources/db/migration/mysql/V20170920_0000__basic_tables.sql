
/*Table structure for table `sys_log` */

DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `type` smallint(1) DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) DEFAULT '' COMMENT '日志标题',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(5) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `exception` longtext COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志表';

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `href` varchar(2000) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `is_show` char(1) NOT NULL COMMENT '是否在菜单中显示',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_menu_parent_id` (`parent_id`),
  KEY `sys_menu_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';


/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`parent_id`,`parent_ids`,`name`,`sort`,`href`,`target`,`icon`,`is_show`,`permission`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values
('1','0','0,','功能菜单',65535,NULL,NULL,NULL,'1',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,0),
('10','5','0,1,2,5,','修改',12,NULL,NULL,NULL,'0','sys:role:edit','1','2017-09-08 14:08:42','1','2017-09-08 14:08:51',NULL,0),
('11','8','0,1,2,8,','修改',10,NULL,NULL,NULL,'0','sys:menu:edit','1','2017-09-08 14:13:20','1','2017-09-08 14:13:24',NULL,0),
('12','4','0,1,2,4,','修改',10,NULL,NULL,NULL,'0','sys:user:edit','1','2017-09-08 14:15:12','1','2017-09-08 14:15:20',NULL,0),
('13','4','0,1,2,4,','查看',10,NULL,NULL,NULL,'0','sys:user:view','1','2017-09-08 14:16:14','1','2017-09-08 14:16:19',NULL,0),
('14','3','0,1,2,3,','查看',10,NULL,NULL,NULL,'0','sys:office:view','1','2017-09-08 14:31:00','1','2017-09-08 14:31:08',NULL,0),
('15','3','0,1,2,3,','修改',10,NULL,NULL,NULL,'0','sys:office:edit','1','2017-09-08 14:31:58','1','2017-09-08 14:32:06',NULL,0),
('16','6','0,1,2,6,','查看',10,NULL,NULL,NULL,'0','sys:log:view','1','2017-09-08 14:33:06','1','2017-09-08 14:33:11',NULL,0),
('17','7','0,1,2,7,','查看',10,NULL,NULL,NULL,'0','sys:druid:view','1','2017-09-08 14:34:59','1','2017-09-08 14:35:06',NULL,0),
('2','1','0,1,','系统管理',26,'','','','1',NULL,'1','2013-10-16 08:00:00','1','2017-08-25 15:59:57','',0),
('3','2','0,1,2,','机构管理',0,'/sys/offices/',NULL,NULL,'1',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,0),
('4','2','0,1,2,','用户管理',2,'/sys/users/','','','1',NULL,'1','2013-05-27 08:00:00','1','2017-08-25 15:31:08','',0),
('5','2','0,1,2,','角色管理',8,'/sys/roles/','','','1',NULL,'1','2013-05-27 08:00:00','1','2017-08-24 17:14:48','',0),
('6','2','0,1,2,','日志管理',30,'/sys/logs/',NULL,NULL,'1',NULL,'1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,0),
('7','2','0,1,2,','连接监视',15,'/sys/druids/',NULL,NULL,'1',NULL,'1','2017-08-28 18:33:41','1','2017-08-28 18:33:45',NULL,0),
('8','2','0,1,2,','菜单管理',10,'/sys/menus/',NULL,NULL,'1',NULL,'1','2017-09-01 13:48:15','1','2017-09-01 13:48:21',NULL,0),
('9','5','0,1,2,5,','查看',12,NULL,NULL,NULL,'0','sys:role:view','1','2017-09-08 14:05:36','1','2017-09-08 14:05:44',NULL,0);


/*Table structure for table `sys_office` */

DROP TABLE IF EXISTS `sys_office`;

CREATE TABLE `sys_office` (
  `id` varchar(64) NOT NULL COMMENT '机构编号',
  `office_name` varchar(100) NOT NULL COMMENT '机构名称',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构表';

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `menu_id` varchar(64) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-菜单';

/*Table structure for table `sys_role_office` */

DROP TABLE IF EXISTS `sys_role_office`;

CREATE TABLE `sys_role_office` (
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `office_id` varchar(64) NOT NULL COMMENT '机构编号',
  PRIMARY KEY (`role_id`,`office_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-机构';

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `office_id` varchar(64) NOT NULL COMMENT '所属机构',
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `login_flag` tinyint(1) DEFAULT '1' COMMENT '是否可登录',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '修改者',
  `update_date` datetime NOT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`office_id`,`login_name`,`password`,`name`,`email`,`mobile`,`login_ip`,
`login_date`,`login_flag`,`create_by`,`create_date`,`update_by`,`update_date`,`del_flag`) values
('1','1','admin','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','系统管理员',
'li.jianfei@microwise-system.com',NULL,'0:0:0:0:0:0:0:1','2017-09-13 14:49:11',1,'1',
'2017-08-29 09:23:03','1','2017-08-29 09:23:07',0);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` varchar(64) NOT NULL COMMENT '用户编号',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色';


