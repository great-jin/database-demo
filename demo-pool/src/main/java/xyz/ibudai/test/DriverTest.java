package xyz.ibudai.test;

import org.junit.Test;
import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.DriverEntity;
import xyz.ibudai.model.common.DbType;
import xyz.ibudai.utils.DriverUtil;
import xyz.ibudai.utils.LoaderUtil;

public class DriverTest {

    @Test
    public void demo1() {
        DbEntity dbEntity = LoaderUtil.buildDbInfo(DbType.MYSQL);
        // Filter depended on entity driver class name
        DriverEntity driverEntity = DriverUtil.getDriverEntity(dbEntity);
        System.out.println(driverEntity);
    }
}
