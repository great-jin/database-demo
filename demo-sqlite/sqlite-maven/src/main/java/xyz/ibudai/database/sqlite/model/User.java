package xyz.ibudai.database.sqlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.database.sqlite.persister.LocalDatePersister;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "tb_user")
public class User {

    @DatabaseField(columnName = "id", generatedId = true)
    private Long id;

    @DatabaseField(columnName = "user_name")
    private String username;

    @DatabaseField(columnName = "create_time", persisterClass = LocalDatePersister.class)
    private LocalDateTime createTime;
}
