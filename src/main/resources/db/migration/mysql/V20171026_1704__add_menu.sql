--修改menu表 添加菜单
--author bai.weixing

INSERT  INTO `sys_menu`(`id`,`parent_id`,`parent_ids`,`name`,`sort`,`href`,`target`,`icon`,`is_show`,`permission`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) VALUES

('18','2','0,1,2','文物管理',30,'/sys/relics/',NULL,NULL,'1',NULL,'1','2017-09-20 18:22:02','1','2017-09-20 18:22:07',NULL,0),
('19','2','0,1,2','设备管理',35,'/sys/devices',NULL,NULL,'1','','1','2017-09-21 11:37:37','1','2017-09-21 11:37:45',NULL,0),
('20','18','0,1,2,18,','查看',10,'/sys/relics/',NULL,NULL,'0','sys:relic:view','1','2017-09-21 13:20:12','1','2017-09-21 13:20:18',NULL,0),
('21','18','0,1,2,18,','修改',10,'/sys/relics/',NULL,NULL,'0','sys:relic:edit','1','2017-09-21 13:21:26','1','2017-09-21 13:21:32',NULL,0),
('22','19','0,1,2,19','查看',10,NULL,NULL,NULL,'0','sys:device:view','1','2017-09-22 14:34:38','1','2017-09-22 14:34:42',NULL,0),
('23','19','0,1,2,19','修改',10,NULL,NULL,NULL,'0','sys:device:edit','1','2017-09-22 14:35:33','1','2017-09-22 14:35:36',NULL,0),
('24','2','0,1,2','位置点管理',40,'/sys/locations',NULL,NULL,'1',NULL,'1','2017-09-25 08:50:47','1','2017-09-25 08:50:53',NULL,0),
('25','24','0,1,2,24','查看',10,NULL,NULL,NULL,'0','sys:location:view','1','2017-09-25 08:55:30','1','2017-09-25 08:55:37',NULL,0),
('26','24','0,1,2,24','修改',10,NULL,NULL,NULL,'0','sys:location:edit','1','2017-09-25 08:56:30','1','2017-09-25 08:56:35',NULL,0),
('27','1','0,1','概览',45,NULL,NULL,NULL,'1',NULL,'1','2017-09-29 09:08:30','1','2017-09-29 09:08:36',NULL,0)
