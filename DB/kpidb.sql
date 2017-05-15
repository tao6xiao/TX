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
  `customer1` varchar(2050) DEFAULT NULL,
  `customer2` varchar(2050) DEFAULT NULL,
  `customer3` varchar(2050) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of issue
-- ----------------------------
INSERT INTO `issue` VALUES ('13854', '100', '1', '1', 'www.baodu.com', '2017-05-10 11:06:29', '0', '0', 'www.baodu.com', null, null);
INSERT INTO `issue` VALUES ('25687', '100', '1', '1', 'www.sohu.com', '2017-05-04 11:06:33', '0', '0', 'www.sohu.com', null, null);
INSERT INTO `issue` VALUES ('32189', '100', '2', '1', 'www.dsf.cn', '2017-05-25 17:06:00', '0', '0', '电影天堂', '3', null);
INSERT INTO `issue` VALUES ('35613', '100', '1', '1', 'www.sina.com', '2017-05-01 11:06:36', '0', '0', 'www.sina.com', null, null);
INSERT INTO `issue` VALUES ('44735', '100', '1', '2', 'www.hao123.com', '2017-05-17 11:06:39', '0', '0', 'www.hao123.com', null, null);
INSERT INTO `issue` VALUES ('54531', '100', '1', '2', 'www.dsa.com', '2017-05-18 11:07:02', '0', '0', 'www.dsa.com', null, null);
INSERT INTO `issue` VALUES ('65872', '100', '1', '1', 'www.dsad.com', '2017-04-30 11:07:22', '0', '0', 'www.dsad.com', null, null);
INSERT INTO `issue` VALUES ('76915', '100', '2', '1', 'www.cdsf.com', '2017-05-12 17:04:30', '0', '0', '音乐天地', '1', null);
INSERT INTO `issue` VALUES ('82816', '100', '2', '1', 'www.ffgh.', '2017-05-18 17:05:31', '0', '0', '电视购物', '2', null);