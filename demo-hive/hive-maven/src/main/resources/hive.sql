-- 无分区建表
create table a_db.user2
(
    `id`          string COMMENT 'id',
    `name`        string COMMENT 'username',
    `update_time` string COMMENT 'update time'
)
COMMENT "Test table"
STORED AS textfile;


-- 分区建表(单分区)
create table a_db.tb_test2
(
    `id`          string COMMENT 'id',
    `name`        string COMMENT 'username',
    `update_time` string COMMENT 'update time'
)
COMMENT "Test partition table 1"
PARTITIONED BY (`create_time` string)
STORED AS textfile;


-- 分区建表(多分区)
create table a_db.tb_test3
(
    `id`          string COMMENT 'id',
    `name`        string COMMENT 'user name',
    `update_time` string COMMENT 'update time'
)
COMMENT "Test partition table 2"
PARTITIONED BY (
	`year` string,
	`month` string
)
STORED AS textfile;


-- 生成表统计信息(无分区)
analyze table a_db.tb_test compute statistics;

-- 生成表统计信息(有分区需指定)
analyze table a_db.tb_test2 partition(create_time) compute statistics;

-- 生成表统计信息(多分区需全部指定)
analyze table a_db.tb_test3 partition(year, month) compute statistics;

-- 生成表统计信息(指定分区与值)
analyze table a_db.tb_test3 partition(year='2022', month='11') compute statistics;


-- 查看建表语句
show create table a_db.tb_test3;


-- 查看分区结构
show partitions a_db.tb_test3;


-- 查看表信息（未格式化）
desc extended a_db.tb_test;


-- 查看表信息（格式化）
desc formatted a_db.tb_test;


-- 查看字段信息
desc formatted a_db.tb_test id;


-- 查看分区信息
desc formatted a_db.tb_test3 partition(year = '2022',month='11');

