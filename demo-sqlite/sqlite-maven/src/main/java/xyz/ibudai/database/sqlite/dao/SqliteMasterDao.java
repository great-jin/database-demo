package xyz.ibudai.database.sqlite.dao;

import xyz.ibudai.database.sqlite.model.SqliteMaster;

import java.sql.SQLException;

/**
 * The interface Sqlite master dao.
 */
public interface SqliteMasterDao extends BaseDao<SqliteMaster, String> {

    /**
     * Query by table sqlite master.
     *
     * @param tableName the table name
     * @return the sqlite master
     * @throws SQLException the sql exception
     */
    SqliteMaster queryByTable(String tableName) throws SQLException;
}
