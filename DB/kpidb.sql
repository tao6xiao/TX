use kpidb;
-- 监测设置信息表
drop TABLE IF EXISTS monitorsite;
CREATE TABLE monitorsite(
		siteId INT PRIMARY KEY COMMENT '站点编号', -- 站点编号
		departmentName VARCHAR(200) COMMENT '单位名称' NOT NULL, -- 单位名称
		guarderName VARCHAR(64) COMMENT '保障人员姓名', -- 保障人员姓名
		guarderAccount VARCHAR(50) COMMENT '保障人员登陆账号', -- 保障人员登陆账号
		guarderPhone VARCHAR(50)COMMENT '保障人员电话号码', -- 保障人员电话号码
		indexUrl VARCHAR(300) COMMENT '首页地址' NOT NULL, -- 首页地址
		siteIds VARCHAR(200) COMMENT '监测站点编号，数组' NOT NULL -- 监测站点编号，数组
)COMMENT='监测设置信息表';

INSERT INTO monitorsite VALUES(1,"江州市监察局","张三","zhangsan","13456858676","http://test1.org.cn","1,2");
INSERT INTO monitorsite VALUES(2,"江州市教育局","李四","lisi","12536896521","http://test2.org.cn","3");
INSERT INTO monitorsite VALUES(3,"江州市分析部","王五","wangwu","15789652425","http://test3.org.cn","4");

-- 监测频率表
drop TABLE IF EXISTS monitorfrequency;
CREATE TABLE monitorfrequency(
	siteId int COMMENT '站点编号', -- 站点编号
	typeId TINYINT COMMENT '类型(比如1：首页可用性，2：全站失链接，3：信息错误)', -- 类型(比如1：首页可用性，2：全站失链接，3：信息错误)
	value SMALLINT COMMENT '对应频率的值（比如对应次/天，value为1，表示1次/天）' NOT NULL, -- 对应频率的值（比如对应次/天，value为1，表示1次/天）
	PRIMARY KEY(siteId,typeId) COMMENT '添加复合主键'
)COMMENT='监测频率表';
INSERT INTO monitorfrequency VALUES(1,1,1);
INSERT INTO monitorfrequency VALUES(1,2,2);
INSERT INTO monitorfrequency VALUES(1,3,3);
INSERT INTO monitorfrequency VALUES(2,1,1);
INSERT INTO monitorfrequency VALUES(2,2,2);
INSERT INTO monitorfrequency VALUES(2,3,3);
INSERT INTO monitorfrequency VALUES(3,1,1);
INSERT INTO monitorfrequency VALUES(3,2,2);
INSERT INTO monitorfrequency VALUES(3,3,3);

-- ----------------------------
-- Table structure for issue
-- ----------------------------
DROP TABLE IF EXISTS `issue`;
CREATE TABLE `issue` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `siteId` int(255) NOT NULL,
  `typeId` tinyint(1) NOT NULL COMMENT '1(可用性问题) 2（信息更新问题）3（信息错误问题）',
  `subTypeId` tinyint(1) NOT NULL,
  `detail` varchar(2050) NOT NULL,
  `issueTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `isResolved` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-->待解决，1-->已处理，2-->已忽略',
  `isDel` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-->未删除，1-->已删除',
  `workOrderStatus` tinyint(1) NOT NULL DEFAULT '0' COMMENT '工单状态 0（未进入工单流程）1（工单处理中）2（工单处理完成）',
  `customer1` varchar(2050) DEFAULT NULL,
  `customer2` varchar(2050) DEFAULT NULL,
  `customer3` varchar(2050) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of issue
-- ----------------------------
INSERT INTO `issue` VALUES ('13854', '11', '1', '1', 'www.baodu.com', '2017-05-10 11:06:29', '0', '0', '0', 'www.baodu.com', null, null);
INSERT INTO `issue` VALUES ('25687', '11', '1', '1', 'www.sohu.com', '2017-05-04 11:06:33', '0', '0', '0', 'www.sohu.com', null, null);
INSERT INTO `issue` VALUES ('32189', '11', '2', '1', 'www.dsf.cn', '2017-05-25 17:06:00', '0', '0', '0', '电影天堂', '3', null);
INSERT INTO `issue` VALUES ('35613', '11', '1', '1', 'www.sina.com', '2017-05-01 11:06:36', '0', '0', '0', 'www.sina.com', null, null);
INSERT INTO `issue` VALUES ('44735', '11', '1', '2', 'www.hao123.com', '2017-05-17 11:06:39', '0', '0', '0', 'www.hao123.com', null, null);
INSERT INTO `issue` VALUES ('54531', '11', '1', '2', 'www.dsa.com', '2017-05-18 11:07:02', '0', '0', '0', 'www.dsa.com', null, null);
INSERT INTO `issue` VALUES ('65872', '11', '1', '1', 'www.dsad.com', '2017-04-30 11:07:22', '0', '0', '0', 'www.dsad.com', null, null);
INSERT INTO `issue` VALUES ('76915', '11', '2', '1', 'www.cdsf.com', '2017-05-12 17:04:30', '0', '0', '0', '音乐天地', '1', null);
INSERT INTO `issue` VALUES ('82816', '11', '2', '1', 'www.ffgh.', '2017-05-18 17:05:31', '0', '0', '0', '电视购物', '2', null);
INSERT INTO `issue` VALUES ('10584', '11', '3', '1', 'www.fdgr.com', '2017-05-15 13:48:26', '0', '0', '0', null, null, null);
INSERT INTO `issue` VALUES ('11786', '11', '3', '2', 'www.163.com', '2017-05-31 13:48:45', '0', '0', '0', null, null, null);
INSERT INTO `issue` VALUES ('12429', '11', '3', '1', 'www.zhihu.com', '2017-04-30 13:49:04', '0', '0', '0', null, null, null);
INSERT INTO `issue` VALUES ('88425', '11', '51', '1', 'www.sohu.com', '2017-05-15 08:54:18', '0', '0', '0', '资讯平台', '10000', null);
INSERT INTO `issue` VALUES ('96842', '11', '51', '1', 'www.4399.com', '2017-04-30 08:55:02', '0', '0', '0', '游戏平台', '4399', null);
INSERT INTO `issue` VALUES ('75175', '11', '51', '1', 'www.baidu.com', '2017-05-11 16:55:55', '0', '0', '0', '搜索引擎', '10086', null);
update issue SET subTypeId = typeId*10+subTypeId;


-- 更新频率及预警初设表
drop TABLE IF EXISTS frequencypreset;
CREATE TABLE frequencypreset(
		id INT NOT NULL auto_increment COMMENT '编号，自增长',
		siteId INT NOT NULL COMMENT '站点编号',
		updateFreq INT NOT NULL COMMENT '预设更新频率/天',
		alertFreq INT NOT NULL COMMENT '预警时限/天',
		PRIMARY KEY(id)
)COMMENT='更新频率及预警初设表';
INSERT INTO frequencypreset VALUES(0,1,1,1);
INSERT INTO frequencypreset VALUES(0,1,1,1);
INSERT INTO frequencypreset VALUES(0,1,1,1);
INSERT INTO frequencypreset VALUES(0,2,2,2);
INSERT INTO frequencypreset VALUES(0,2,2,2);
INSERT INTO frequencypreset VALUES(0,2,2,2);
INSERT INTO frequencypreset VALUES(0,3,3,3);
INSERT INTO frequencypreset VALUES(0,3,3,3);
INSERT INTO frequencypreset VALUES(0,3,3,3);

-- 按需更新的自查提醒周期表
drop TABLE IF EXISTS defaultupdatefreq;
CREATE TABLE defaultupdatefreq(
		siteId INT NOT NULL COMMENT '站点编号',
		value INT NOT NULL COMMENT '周期值',
		setTime datetime NOT NULL COMMENT '设置时间',
		PRIMARY KEY(siteId)
)COMMENT='按需更新的自查提醒周期表';
INSERT INTO defaultupdatefreq VALUES(1,1,'2017-5-17 10:20:20');
INSERT INTO defaultupdatefreq VALUES(2,2,'2017-5-17 10:21:21');
INSERT INTO defaultupdatefreq VALUES(3,3,'2017-5-17 10:22:22');

-- 栏目更新频率表
drop TABLE IF EXISTS frequencysetup;
CREATE TABLE frequencysetup(
		id INT NOT NULL auto_increment COMMENT '编号',
		siteId INT NOT NULL COMMENT '站点编号',
		presetFeqId INT NOT NULL COMMENT '预设频率编号',
		chnlId INT NOT NULL COMMENT '栏目编号',
		setTime datetime NOT NULL COMMENT '设置时间',
		isOpen tinyint NOT NULL COMMIT '当前记录关闭/开启标识',
		PRIMARY KEY(id),
		UNIQUE KEY(siteId,chnlId)
)COMMENT='栏目更新频率表';
INSERT into frequencysetup VALUES(null,1,1,1,'2017-5-22 10:20:20',1);
INSERT into frequencysetup VALUES(null,1,2,2,'2017-5-22 10:20:20',0);
INSERT into frequencysetup VALUES(null,1,3,3,'2017-5-22 10:20:20',0);

-- 栏目分类表
drop TABLE IF EXISTS chnlgroup;
CREATE TABLE chnlgroup(
		id INT NOT NULL auto_increment COMMENT '编号',
		siteId INT NOT NULL COMMENT '站点编号',
		groupId INT NOT NULL COMMENT '分类编号',
		chnlId INT NOT NULL COMMENT '栏目编号',
		PRIMARY KEY(id),
		UNIQUE KEY (siteId,groupId,chnlId)
)COMMENT='栏目分类表';
INSERT into chnlgroup VALUES(null,1,1,1);
INSERT into chnlgroup VALUES(null,1,1,2);
INSERT into chnlgroup VALUES(null,1,1,3);
INSERT into chnlgroup VALUES(null,1,2,1);
INSERT into chnlgroup VALUES(null,1,2,2);
INSERT into chnlgroup VALUES(null,1,2,3);
INSERT into chnlgroup VALUES(null,1,3,1);
INSERT into chnlgroup VALUES(null,1,3,2);
INSERT into chnlgroup VALUES(null,1,3,3);

-- ----------------------------
-- Table structure for webpage
-- ----------------------------
DROP TABLE IF EXISTS `webpage`;
CREATE TABLE `webpage` (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '信息编号',
  `siteId` int(255) NOT NULL COMMENT '站点编号',
  `typeId` tinyint(1) NOT NULL COMMENT '类型编号',
  `chnlId` int(255) NOT NULL COMMENT '所在栏目编号',
  `pageLink` varchar(3024) DEFAULT NULL COMMENT '网页链接',
  `replySpeed` int(19) DEFAULT NULL COMMENT '响应速度',
  `pageSpace` int(19) DEFAULT NULL COMMENT '页面大小',
  `pageDepth` tinyint(2) DEFAULT NULL COMMENT '页面深度',
  `repeatPlace` tinyint(5) DEFAULT NULL COMMENT '冗余位置',
  `repeatDegree` varchar(1) DEFAULT NULL COMMENT '冗余程度：低，中，高',
  `urlLength` varchar(5) DEFAULT NULL COMMENT 'URL长度',
  `updateTime` datetime DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最后操作时间',
  `checkTime` datetime NOT NULL COMMENT '监测时间',
  `isResolved` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0（未解决）1（已处理）2（已忽略）',
  `isDel` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0（未删除）1（已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95479 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of webpage
-- ----------------------------
INSERT INTO `webpage` VALUES ('12345', '11', '1', '11', 'www.zhihu.com', '10s', '5M', null, null, null, null, '2017-06-06 16:44:30', '2017-06-06 16:15:27', '0', '0');
INSERT INTO `webpage` VALUES ('21454', '11', '3', '7', 'www.zhihu.com', null, '5M', '8', null, null, null, '2017-06-06 17:26:46', '2017-06-01 17:23:43', '0', '0');
INSERT INTO `webpage` VALUES ('36542', '11', '2', '11', 'www.ds.com', '10s', '8M', null, null, null, null, '2017-06-06 17:27:32', '2017-06-06 17:22:22', '0', '0');
INSERT INTO `webpage` VALUES ('36554', '11', '2', '10', 'www.zhihu.com', null, '15M', null, null, null, null, '2017-06-06 17:26:27', '2017-06-04 17:23:01', '0', '0');
INSERT INTO `webpage` VALUES ('52469', '11', '1', '10', 'www.baidu.com', '7s', '7M', null, null, null, null, '2017-06-03 16:44:35', '2017-06-03 16:16:07', '0', '0');
INSERT INTO `webpage` VALUES ('54785', '11', '5', '7', 'www.zhihu.com', null, '5M', null, null, null, '120字节', '2017-06-06 17:27:58', '2017-06-03 17:25:05', '0', '0');
INSERT INTO `webpage` VALUES ('58496', '11', '3', '11', 'www.zhihu.com', null, '9M', '9', null, null, null, '2017-06-06 17:26:44', '2017-06-03 17:23:34', '0', '0');
INSERT INTO `webpage` VALUES ('68542', '11', '1', '7', 'www.hao123.com', '9s', '6M', null, null, null, null, '2017-06-02 16:44:39', '2017-06-02 16:17:08', '0', '0');
INSERT INTO `webpage` VALUES ('75146', '11', '5', '11', 'www.zhihu.com', null, '6M', null, null, null, '80字节', '2017-06-06 17:27:55', '2017-06-06 17:24:58', '0', '0');
INSERT INTO `webpage` VALUES ('76544', '11', '4', '11', 'www.zhihu.com', null, null, null, '65', '中', null, '2017-06-07 10:31:46', '2017-06-02 17:24:10', '0', '0');
INSERT INTO `webpage` VALUES ('95478', '11', '4', '7', 'www.zhihu.com', null, null, null, '78', '高', null, '2017-06-07 10:31:48', '2017-06-06 17:24:16', '0', '0');

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `id` int(19) NOT NULL AUTO_INCREMENT,
  `siteId` int(19) NOT NULL,
  `title` varchar(512) NOT NULL,
  `reportTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '报表数据对应的时间',
  `crTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1-天，2-月',
  `path` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;

-- ----------------------------
-- Table structure for performanceindex
-- ----------------------------
DROP TABLE IF EXISTS `performanceindex`;
CREATE TABLE `performanceindex` (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '绩效指数编号',
  `siteId` int(255) NOT NULL COMMENT '站点编号',
  `performance` double(255,2) NOT NULL COMMENT '月末绩效指数',
  `checkTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '监测时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of performanceindex
-- ----------------------------
INSERT INTO `performanceindex` VALUES ('1', '11', '80.95', '2017-04-30 11:55:38');
INSERT INTO `performanceindex` VALUES ('2', '11', '77.45', '2017-03-31 11:55:34');
INSERT INTO `performanceindex` VALUES ('3', '11', '78.65', '2017-05-31 11:55:30');

-- 设置模块，为栏目与部门关系表
DROP TABLE IF EXISTS dutydept;
CREATE TABLE dutydept (
	chnlId INT NOT NULL COMMENT '栏目编号',
  siteId INT NOT NULL COMMENT '站点编号',
	deptId INT NOT NULL COMMENT '部门编号',
  contain TINYINT(1) NOT NULL COMMENT '是否包含子栏目,0：不包含，1：包含',
  PRIMARY KEY (chnlId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for monitortime
-- ----------------------------
DROP TABLE IF EXISTS `monitortime`;
CREATE TABLE `monitortime` (
  `siteId` int(5) NOT NULL,
  `typeId` int(5) NOT NULL COMMENT '0-->首页可用性；1-->网站可用性；2-->信息更新；3-->信息错误；11-->服务实用',
  `startTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `endTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- -----------------------------------
-- Table structure for quartz start
-- -----------------------------------

drop table if exists qrtz_fired_triggers;
drop table if exists qrtz_paused_trigger_grps;
drop table if exists qrtz_scheduler_state;
drop table if exists qrtz_locks;
drop table if exists qrtz_simple_triggers;
drop table if exists qrtz_simprop_triggers;
drop table if exists qrtz_cron_triggers;
drop table if exists qrtz_blob_triggers;
drop table if exists qrtz_triggers;
drop table if exists qrtz_job_details;
drop table if exists qrtz_calendars;

create table qrtz_job_details(
  sched_name varchar(120) not null,
  job_name varchar(200) not null,
  job_group varchar(200) not null,
  description varchar(250) null,
  job_class_name varchar(250) not null,
  is_durable varchar(1) not null,
  is_nonconcurrent varchar(1) not null,
  is_update_data varchar(1) not null,
  requests_recovery varchar(1) not null,
  job_data blob null,
  primary key (sched_name,job_name,job_group))
  engine=innodb;

create table qrtz_triggers (
  sched_name varchar(120) not null,
  trigger_name varchar(200) not null,
  trigger_group varchar(200) not null,
  job_name varchar(200) not null,
  job_group varchar(200) not null,
  description varchar(250) null,
  next_fire_time bigint(13) null,
  prev_fire_time bigint(13) null,
  priority integer null,
  trigger_state varchar(16) not null,
  trigger_type varchar(8) not null,
  start_time bigint(13) not null,
  end_time bigint(13) null,
  calendar_name varchar(200) null,
  misfire_instr smallint(2) null,
  job_data blob null,
  primary key (sched_name,trigger_name,trigger_group),
  foreign key (sched_name,job_name,job_group)
  references qrtz_job_details(sched_name,job_name,job_group))
  engine=innodb;

create table qrtz_simple_triggers (
  sched_name varchar(120) not null,
  trigger_name varchar(200) not null,
  trigger_group varchar(200) not null,
  repeat_count bigint(7) not null,
  repeat_interval bigint(12) not null,
  times_triggered bigint(10) not null,
  primary key (sched_name,trigger_name,trigger_group),
  foreign key (sched_name,trigger_name,trigger_group)
  references qrtz_triggers(sched_name,trigger_name,trigger_group))
  engine=innodb;

create table qrtz_cron_triggers (
  sched_name varchar(120) not null,
  trigger_name varchar(200) not null,
  trigger_group varchar(200) not null,
  cron_expression varchar(120) not null,
  time_zone_id varchar(80),
  primary key (sched_name,trigger_name,trigger_group),
  foreign key (sched_name,trigger_name,trigger_group)
  references qrtz_triggers(sched_name,trigger_name,trigger_group))
  engine=innodb;

create table qrtz_simprop_triggers
(
  sched_name varchar(120) not null,
  trigger_name varchar(200) not null,
  trigger_group varchar(200) not null,
  str_prop_1 varchar(512) null,
  str_prop_2 varchar(512) null,
  str_prop_3 varchar(512) null,
  int_prop_1 int null,
  int_prop_2 int null,
  long_prop_1 bigint null,
  long_prop_2 bigint null,
  dec_prop_1 numeric(13,4) null,
  dec_prop_2 numeric(13,4) null,
  bool_prop_1 varchar(1) null,
  bool_prop_2 varchar(1) null,
  primary key (sched_name,trigger_name,trigger_group),
  foreign key (sched_name,trigger_name,trigger_group)
  references qrtz_triggers(sched_name,trigger_name,trigger_group))
  engine=innodb;

create table qrtz_blob_triggers (
  sched_name varchar(120) not null,
  trigger_name varchar(200) not null,
  trigger_group varchar(200) not null,
  blob_data blob null,
  primary key (sched_name,trigger_name,trigger_group),
  index (sched_name,trigger_name, trigger_group),
  foreign key (sched_name,trigger_name,trigger_group)
  references qrtz_triggers(sched_name,trigger_name,trigger_group))
  engine=innodb;

create table qrtz_calendars (
  sched_name varchar(120) not null,
  calendar_name varchar(200) not null,
  calendar blob not null,
  primary key (sched_name,calendar_name))
  engine=innodb;

create table qrtz_paused_trigger_grps (
  sched_name varchar(120) not null,
  trigger_group varchar(200) not null,
  primary key (sched_name,trigger_group))
  engine=innodb;

create table qrtz_fired_triggers (
  sched_name varchar(120) not null,
  entry_id varchar(95) not null,
  trigger_name varchar(200) not null,
  trigger_group varchar(200) not null,
  instance_name varchar(200) not null,
  fired_time bigint(13) not null,
  sched_time bigint(13) not null,
  priority integer not null,
  state varchar(16) not null,
  job_name varchar(200) null,
  job_group varchar(200) null,
  is_nonconcurrent varchar(1) null,
  requests_recovery varchar(1) null,
  primary key (sched_name,entry_id))
  engine=innodb;

create table qrtz_scheduler_state (
  sched_name varchar(120) not null,
  instance_name varchar(200) not null,
  last_checkin_time bigint(13) not null,
  checkin_interval bigint(13) not null,
  primary key (sched_name,instance_name))
  engine=innodb;

create table qrtz_locks (
  sched_name varchar(120) not null,
  lock_name varchar(40) not null,
  primary key (sched_name,lock_name))
  engine=innodb;

create index idx_qrtz_j_req_recovery on qrtz_job_details(sched_name,requests_recovery);
create index idx_qrtz_j_grp on qrtz_job_details(sched_name,job_group);

create index idx_qrtz_t_j on qrtz_triggers(sched_name,job_name,job_group);
create index idx_qrtz_t_jg on qrtz_triggers(sched_name,job_group);
create index idx_qrtz_t_c on qrtz_triggers(sched_name,calendar_name);
create index idx_qrtz_t_g on qrtz_triggers(sched_name,trigger_group);
create index idx_qrtz_t_state on qrtz_triggers(sched_name,trigger_state);
create index idx_qrtz_t_n_state on qrtz_triggers(sched_name,trigger_name,trigger_group,trigger_state);
create index idx_qrtz_t_n_g_state on qrtz_triggers(sched_name,trigger_group,trigger_state);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(sched_name,next_fire_time);
create index idx_qrtz_t_nft_st on qrtz_triggers(sched_name,trigger_state,next_fire_time);
create index idx_qrtz_t_nft_misfire on qrtz_triggers(sched_name,misfire_instr,next_fire_time);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers(sched_name,misfire_instr,next_fire_time,trigger_state);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(sched_name,misfire_instr,next_fire_time,trigger_group,trigger_state);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(sched_name,instance_name);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(sched_name,instance_name,requests_recovery);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers(sched_name,job_name,job_group);
create index idx_qrtz_ft_jg on qrtz_fired_triggers(sched_name,job_group);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers(sched_name,trigger_name,trigger_group);
create index idx_qrtz_ft_tg on qrtz_fired_triggers(sched_name,trigger_group);

-- -----------------------------------
-- Table structure for quartz end
-- -----------------------------------




