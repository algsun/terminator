--修改`sys_user`表
--author sun.cong

ALTER TABLE sys_user ADD attention INT (11) DEFAULT 0 COMMENT '是否关注';
