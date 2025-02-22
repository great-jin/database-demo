package xyz.ibudai.database.sqlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "sqlite_master")
public class SqliteMaster {

    @DatabaseField(columnName = "type")
    private String type;

    @DatabaseField(columnName = "name")
    private String name;
}

