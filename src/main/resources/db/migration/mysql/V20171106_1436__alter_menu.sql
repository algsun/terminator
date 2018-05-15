--修改menu表 添加菜单
--author bai.weixing

INSERT  INTO `sys_menu`(`id`,`parent_id`,`parent_ids`,`name`,`sort`,`href`,`target`,`icon`,`is_show`,`permission`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) VALUES

('28','27','0,1，27,','概览',45,'/sys/overviews',NULL,NULL,'1',NULL,'1','2017-09-29 09:08:30','1','2017-09-29 09:08:36',NULL,0),
('29','28','0,1,27,28','查看',65536,'/sys/overviews/',NULL,NULL,'0','sys:overview:view','1','2017-09-29 09:08:30','1','2017-09-29 09:08:36',NULL,0);

-- 更改所有隐藏按钮的sort为65536
UPDATE `sys_menu` SET sort = 65536 where is_show = 0;