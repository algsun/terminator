--修改menu表 添加菜单
--author bai.weixing

INSERT  INTO `sys_menu`(`id`,`parent_id`,`parent_ids`,`name`,`sort`,`href`,`target`,`icon`,`is_show`,`permission`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) VALUES

('30','1','0,1','统计分析',55,NULL,NULL,NULL,'1',NULL,'1','2017-11-21 10:58:30','1','2017-11-21 10:58:30',NULL,0),
('31','30','0,1,30,','日报',55,'/analysis/daily',NULL,NULL,'1',NULL,'1','2017-11-21 10:58:30','1','2017-11-21 10:58:30',NULL,0),
('32','31','0,1,30,31','查看',65536,'',NULL,NULL,'0','analysis:daily:view','1','2017-11-21 10:58:30','1','2017-11-21 10:58:30',NULL,0),
('33','31','0,1,30,31','修改',65536,'',NULL,NULL,'0','analysis:daily:edit','1','2017-12-12 09:15:12','1','2017-12-12 09:15:18',NULL,0),
('34','30','0,1,30','报警记录',60,'/analysis/alarmHistorys',NULL,NULL,'1',NULL,'1','2017-12-12 09:18:29','1','2017-12-12 09:18:36',NULL,0),
('35','34','0,1,30,34','查看',65536,NULL,NULL,NULL,'0','analysis:alarmHistory:view','1','2017-12-12 09:19:52','1','2017-12-12 09:19:58',NULL,0),
('36','34','0,1,30,34','修改',65536,NULL,NULL,NULL,'0','analysis:alarmHistory:edit','1','2017-12-12 09:20:57','1','2017-12-12 09:21:08',NULL,0)
