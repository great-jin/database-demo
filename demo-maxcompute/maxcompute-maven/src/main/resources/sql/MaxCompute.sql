create table test_t1 (id bigint, name string, age int)
partitioned by (year int)
tblproperties ("transactional"="true");

insert into test_t1 partition (year=2020)
values (1, 'Alex', 15), (2, 'Beth', 20);

select * from test_t1 where year=2020;

delete from test_t1 where year = 2020;


insert into test_t1 partition (year=2022)
values (3, 'Mark', 26), (4, 'Jack', 45);

select * from test_t1 where year=2022;

delete from test_t1 where year = 2022;


set odps.sql.allow.fullscan=true;
select * from test_t1;


set odps.sql.allow.fullscan=true;
update test_t1
set id=5, name='Box', age=42
where year = 2020 and id=1;




-- 复制表 无分区、无事务、有数据、无法更新删除
create table test_t2
as
select * from test_t1;

select * from test_t2;


-- 复制表 有分区、无事务、无数据、无法更新删除
create table test_t3 like test_t1;

insert into test_t3
    partition (year=2022)
values (1, 'Alex', 15), (2, 'Beth', 20);

select * from test_t3
where year = 2022;



select 'Alex' as name, '12' as age;


create table put_test (id int, name string, age int)
partitioned by (year int)
tblproperties ("transactional"="true");

insert into put_test partition (year=2020)
values (2, 'Mark', 30);

select * from put_test where year=2020;

delete from put_test where year=2020;



-- Merge操作
MERGE INTO create table test_t1 (id bigint, name string, age int)
partitioned by (year int)
tblproperties ("transactional"="true");

insert into test_t1 partition (year=2020)
values (1, 'Alex', 15), (2, 'Beth', 20);

select * from test_t1 where year=2020;

delete from test_t1 where year = 2020;


insert into test_t1 partition (year=2022)
values (3, 'Mark', 26), (4, 'Jack', 45);

select * from test_t1 where year=2022;

delete from test_t1 where year = 2022;


set odps.sql.allow.fullscan=true;
select * from test_t1;


set odps.sql.allow.fullscan=true;
update test_t1
set id=5, name='Box', age=42
where year = 2020 and id=1;




-- 复制表 无分区、无事务、有数据、无法更新删除
create table test_t2
as
select * from test_t1;

select * from test_t2;


-- 复制表 有分区、无事务、无数据、无法更新删除
create table test_t3 like test_t1;

insert into test_t3
    partition (year=2022)
values (1, 'Alex', 15), (2, 'Beth', 20);

select * from test_t3
where year = 2022;



create table put_test (id int, name string, age int)
    partitioned by (year int)
tblproperties ("transactional"="true");

insert into put_test partition (year=2020)
values (2, 'Mark', 30);

select * from put_test where year=2020;

delete from put_test where year=2020;



-- Merge操作
MERGE INTO test_project.put_test AS t1
USING (SELECT 1 AS id, 'Alex' AS name, 22 AS age, 2020 AS year) AS t2
ON t1.year=t2.year and t1.id=t2.id
WHEN MATCHED THEN UPDATE SET t1.name=t2.name, t1.age=t2.age
WHEN NOT MATCHED THEN INSERT VALUES(t2.id, t2.name, t2.age, t2.year );



-- 无事务、无分区
create table test_t1 (id bigint, name string, age int) ;

insert into test_t1 values (1, 'namw', 2);



-- 有事务、无分区
create table test_t2 (id bigint, name string, age int)
    tblproperties ("transactional"="true");



-- 有事务、有分区
create table test_t3 (id bigint, name string, age int)
    partitioned by (year int)
tblproperties ("transactional"="true");



-- 事务、分区、全字段
create table test_t4(
    _TINYINT TINYINT,
    _SMALLINT SMALLINT,
    _INT INT,
    _BIGINT BIGINT,
    _BINARY BINARY,
    _FLOAT FLOAT,
    _DOUBLE DOUBLE,
    _DECIMAL DECIMAL,
    _VARCHAR_10 VARCHAR(10),
    _CHAR_2 CHAR(2),
    _STRING STRING,
    _DATE DATE,
    _DATETIME DATETIME,
    _TIMESTAMP TIMESTAMP,
    _BOOLEAN BOOLEAN
)
partitioned by (_partition string )
tblproperties ("transactional"="true");

insert into test_t4 partition (_partition='test')
values(1Y, 32767S, 1000, 100000000000L, unhex('FA34E10293CB42848573A4E39937F479'), cast(3.14159261E+7 as float), 3.14159261E+7, 3.5BD,
    'VAR_10', 'C2', 'STRING', DATE'2017-11-11', DATETIME'2017-11-11 00:00:00', TIMESTAMP'2017-11-11 00:00:00.123456789', true);

set odps.sql.allow.fullscan=true;
select * from test_project.test_t4;

delete from test_t4
where _partition='test';

drop table test_t4;



-- 正常字段
create table test_t5(
    _TINYINT TINYINT,
    _SMALLINT SMALLINT,
    _INT INT,
    _BIGINT BIGINT,
    _DOUBLE DOUBLE,
    _DECIMAL DECIMAL,
    _VARCHAR_10 VARCHAR(10),
    _CHAR_2 CHAR(2),
    _STRING STRING,
    _DATETIME DATETIME,
    _TIMESTAMP TIMESTAMP,
    _BOOLEAN BOOLEAN
)
partitioned by (_partition string )
tblproperties ("transactional"="true");

insert into test_t5 partition (_partition='test')
values(1Y, 32767S, 1000, 100000000000L,  3.14159261E+7, 3.5BD, 'VAR_10', 'C2', 'STRING',
    DATETIME'2017-11-11 00:00:00', TIMESTAMP'2017-11-11 00:00:00.123456789', true);

set odps.sql.allow.fullscan=true;
select * from test_project.test_t5;

delete from test_t5
where _pString='test';

drop table test_t5;



-- 异常字段
create table test_t6(
    _BINARY BINARY,
    _FLOAT FLOAT,
    _DATE DATE
)
partitioned by (_partition string )
tblproperties ("transactional"="true");

insert into test_t6 partition (_partition='test')
values(unhex('FA34E10293CB42848573A4E39937F479'), cast(3.14159261E+7 as float), 3.14159261E+7, 'VAR_10', 'C2', DATE'2017-11-11');

set odps.sql.allow.fullscan=true;
select * from test_project.test_t6;

drop table test_t6;



-- FLOAT、DATE 异常
create table test_t7(
    _FLOAT FLOAT,
    _DATE DATE
)
partitioned by (year string)
tblproperties ("transactional"="true");

drop table test_t7;


-- FLOAT 异常
create table test_t8(
    _FLOAT FLOAT
)
partitioned by (year string)
tblproperties ("transactional"="true");


-- Date 异常
create table test_t8(
    _DATE DATE
)
partitioned by (year string)
tblproperties ("transactional"="true");



-- Blob 异常
create table test_t9(
    _BINARY BINARY
)
partitioned by (_partition string )
tblproperties ("transactional"="true");

insert into test_t9 partition (_partition='test')
values(unhex('FA34E10293CB42848573A4E39937F479'));

select * from test_t9 where _partition='test';

delete from test_t9 where _partition='test';



-- 无事务、无分区
create table test_t1 (id bigint, name string, age int) ;

insert into test_t1 values (1, 'namw', 2);



-- 有事务、无分区
create table test_t2 (id bigint, name string, age int)
tblproperties ("transactional"="true");



-- 有事务、有分区
create table test_t3 (id bigint, name string, age int)
partitioned by (year int)
tblproperties ("transactional"="true");



-- 事务、分区、全字段
create table test_t4(
    _TINYINT TINYINT,
    _SMALLINT SMALLINT,
    _INT INT,
    _BIGINT BIGINT,
    _BINARY BINARY,
    _FLOAT FLOAT,
    _DOUBLE DOUBLE,
    _DECIMAL DECIMAL,
    _VARCHAR_10 VARCHAR(10),
    _CHAR_2 CHAR(2),
    _STRING STRING,
    _DATE DATE,
    _DATETIME DATETIME,
    _TIMESTAMP TIMESTAMP,
    _BOOLEAN BOOLEAN
)
partitioned by (_partition string )
tblproperties ("transactional"="true");

insert into test_t4 partition (_partition='test')
values(1Y, 32767S, 1000, 100000000000L, unhex('FA34E10293CB42848573A4E39937F479'), cast(3.14159261E+7 as float), 3.14159261E+7, 3.5BD,
    'VAR_10', 'C2', 'STRING', DATE'2017-11-11', DATETIME'2017-11-11 00:00:00', TIMESTAMP'2017-11-11 00:00:00.123456789', true);

set odps.sql.allow.fullscan=true;
select * from test_project.test_t4;

delete from test_t4
where _partition='test';

drop table test_t4;



-- 正常字段
create table test_t5(
    _TINYINT TINYINT,
    _SMALLINT SMALLINT,
    _INT INT,
    _BIGINT BIGINT,
    _DOUBLE DOUBLE,
    _DECIMAL DECIMAL,
    _VARCHAR_10 VARCHAR(10),
    _CHAR_2 CHAR(2),
    _STRING STRING,
    _DATETIME DATETIME,
    _TIMESTAMP TIMESTAMP,
    _BOOLEAN BOOLEAN
)
partitioned by (_partition string )
tblproperties ("transactional"="true");

insert into test_t5 partition (_partition='test')
values(1Y, 32767S, 1000, 100000000000L,  3.14159261E+7, 3.5BD, 'VAR_10', 'C2', 'STRING',
    DATETIME'2017-11-11 00:00:00', TIMESTAMP'2017-11-11 00:00:00.123456789', true);

set odps.sql.allow.fullscan=true;
select * from test_project.test_t5;

delete from test_t5
where _pString='test';

drop table test_t5;



-- 异常字段
create table test_t6(
    _BINARY BINARY,
    _FLOAT FLOAT,
    _DATE DATE
)
partitioned by (_partition string )
tblproperties ("transactional"="true");

insert into test_t6 partition (_partition='test')
values(unhex('FA34E10293CB42848573A4E39937F479'), cast(3.14159261E+7 as float), 3.14159261E+7, 'VAR_10', 'C2', DATE'2017-11-11');

set odps.sql.allow.fullscan=true;
select * from test_project.test_t6;

drop table test_t6;



-- FLOAT、DATE 异常
create table test_t7(
    _FLOAT FLOAT,
    _DATE DATE
)
partitioned by (year string)
tblproperties ("transactional"="true");

drop table test_t7;



-- FLOAT 异常
create table test_t8(
    _FLOAT FLOAT
)
partitioned by (year string)
tblproperties ("transactional"="true");



-- Date 异常
create table test_t8(
    _DATE DATE
)
partitioned by (year string)
tblproperties ("transactional"="true");



-- Blob 异常
create table test_t9(
    _BINARY BINARY
)
partitioned by (_partition string )
tblproperties ("transactional"="true");

insert into test_t9 partition (_partition='test')
values(unhex('FA34E10293CB42848573A4E39937F479'));

select * from test_t9 where _partition='test';

delete from test_t9 where _partition='test';
