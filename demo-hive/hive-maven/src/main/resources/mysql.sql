-- 存储 Hive 数据库基本信息
select * from DBS;

select * from DBS
where NAME = 'a_db' and OWNER_NAME = 'hive';


-- 存储数据库的相关参数
select * from DATABASE_PARAMS;


-- 存储 Hive 表、视图、索引等信息
select * from TBLS;

select * from TBLS
where DB_ID =132;


-- 存储存储表/视图的属性信息
select * from TABLE_PARAMS;

select * from TABLE_PARAMS
where TBL_ID = 502695;


-- 存储分区信息
select * from PARTITIONS;

select * from PARTITIONS
where TBL_ID = 502695;


-- 存储表存储表/视图的授权信息
select * from TBL_PRIVS;


-- 查看指定表信息
select
    tb.TBL_NAME as table_name,
    SUM(case when pa.param_key = 'numRows' then pa.param_value else 0 end) as row_num,
    SUM(case when pa.param_key = 'numRows' then 1 else 0 end) as partition_num,
    SUM(case when pa.param_key = 'totalSize' then pa.param_value else 0 end) as total_size,
    SUM(case when pa.param_key = 'numFiles' then pa.param_value else 0 end) as file_num
from
    TABLE_PARAMS pa
left join
    TBLS tb
on
    pa.TBL_ID = tb.TBL_ID
where
    tb.DB_ID in (
        select
            db.DB_ID
        from
            DBS db
        where
            db.NAME = 'a_db'
            and db.OWNER_NAME = 'hive'
    )
    and tb.TBL_NAME = 'tb_test';
