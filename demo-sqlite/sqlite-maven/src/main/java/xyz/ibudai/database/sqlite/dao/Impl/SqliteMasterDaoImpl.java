package xyz.ibudai.database.sqlite.dao.Impl;

import xyz.ibudai.database.sqlite.dao.AbstractDao;
import xyz.ibudai.database.sqlite.dao.SqliteMasterDao;
import xyz.ibudai.database.sqlite.enums.Database;
import xyz.ibudai.database.sqlite.model.SqliteMaster;

import java.sql.SQLException;

public class SqliteMasterDaoImpl
        extends AbstractDao<SqliteMaster, String>
        implements SqliteMasterDao {

    public SqliteMasterDaoImpl(Database database) throws SQLException {
        super(database);
    }

    /**
     * 查询数据库元数据
     *
     * @param tableName 表名
     */
    @Override
    public SqliteMaster queryByTable(String tableName) throws SQLException {
        SqliteMaster master = new SqliteMaster();
        master.setType("table");
        master.setName(tableName);
        return this.findFirst(master);
    }
}

