package xyz.ibudai.database.jdbc.pool.test;

import org.junit.Test;
import xyz.ibudai.database.jdbc.pool.model.DbEntity;
import xyz.ibudai.database.jdbc.pool.model.DriverEntity;
import xyz.ibudai.database.jdbc.pool.model.common.DbType;
import xyz.ibudai.database.jdbc.pool.utils.DriverUtil;
import xyz.ibudai.database.jdbc.pool.utils.LoaderUtil;

public class DriverTest {

    @Test
    public void demo1() {
        DbEntity dbEntity = LoaderUtil.buildDbInfo(DbType.ORACLE);
        // Filter depended on entity driver class name
        DriverEntity driverEntity = DriverUtil.getDriverEntity(dbEntity);
        System.out.println(driverEntity);
    }
}
