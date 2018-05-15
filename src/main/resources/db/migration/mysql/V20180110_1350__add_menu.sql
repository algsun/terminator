--修改menu表 添加菜单
--author bai.weixing

INSERT  INTO `sys_menu`(`id`,`parent_id`,`parent_ids`,`name`,`sort`,`href`,`target`,`icon`,`is_show`,`permission`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) VALUES

('37','30','0,1,30','知晓记录',65,'/analysis/awareRecords',NULL,NULL,'1',NULL,'1','2018-01-10 13:56:29','1','2018-01-10 13:56:29',NULL,0),
('38','37','0,1,30,37','查看',65536,NULL,NULL,NULL,'0','analysis:awareRecord:view','1','2018-01-10 13:56:29','1','2018-01-10 13:56:29',NULL,0),
('39','37','0,1,30,37','修改',65536,NULL,NULL,NULL,'0','analysis:awareRecord:edit','1','2018-01-10 13:56:29','1','2018-01-10 13:56:29',NULL,0)
