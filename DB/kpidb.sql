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

--更新频率及预警初设表
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