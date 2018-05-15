--修改`m_location`表
--author sun.cong

ALTER TABLE m_location MODIFY id VARCHAR(64) NOT NULL COMMENT '主键';
ALTER TABLE m_location MODIFY zoneId VARCHAR(64) COMMENT '区域编号';
ALTER TABLE m_location MODIFY siteId VARCHAR(64) NOT NULL COMMENT '站点编号';