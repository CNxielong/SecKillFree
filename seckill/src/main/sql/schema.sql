
-- 慕课网秒杀项目 数据库初始化脚本 --
-- version:5.5.36 WIN10 X64 --

-- 创建数据库 注意把时间戳有默认值的放最前面 --
CREATE DATABASE seckill;
USE seckill;

-- 创建秒杀库存表 --
CREATE TABLE seckill(
`seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
`name` VARCHAR(120) NOT NULL COMMENT '商品名称',
`number` INT NOT NULL COMMENT '库存数量',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
PRIMARY KEY (seckill_id), -- 主键 --
KEY idx_start_time (start_time),-- 索引 --
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 COMMENT '秒杀库存表';

-- 插入数据信息 --
INSERT INTO seckill(NAME,number,start_time,end_time)
VALUES
('1000元秒杀小米MIX2S',100,'2018-11-11 00:00:00','2018-11-11 23:59:59'),
('10元秒杀iPhoneX',100,'2018-1-1 00:00:00','2018-11-11 23:59:59'),
('100元秒杀iPhoneXS',100,'2018-1-1 00:00:00','2018-12-12 00:00:00'),
('1010元秒杀Mac',100,'2018-1-1 00:00:00','2018-12-31 23:59:59');

-- 秒杀成功明细表 --
-- 用户登录认证相关信息(简化为手机号) --
CREATE TABLE success_killed(
seckill_id BIGINT NOT NULL COMMENT '秒杀商品ID',
user_phone BIGINT NOT NULL COMMENT '用户手机号',
state TINYINT NOT NULL DEFAULT -1 COMMENT '状态标识:默认-1:无效 0:成功 1:付款 2：已发货',
create_time TIMESTAMP NOT NULL COMMENT '创建时间',
PRIMARY KEY (seckill_id, user_phone),/* 联合主键 */
KEY idx_create_time(create_time) -- 索引 --
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '秒杀成功明细表';

SHOW CREATE TABLE seckill; #显示表的创建信息
SHOW CREATE TABLE seckill\G; #显示表的创建信息 图形化界面语法报错 命令行模式不报错