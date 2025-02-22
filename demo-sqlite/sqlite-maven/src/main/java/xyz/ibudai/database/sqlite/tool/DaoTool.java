package xyz.ibudai.database.sqlite.tool;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Field;
import java.util.*;

public class DaoTool {

    /**
     * 对象转 Map
     *
     * @param obj 目标
     * @return Map
     */
    public static Map<String, Object> objToMap(Object obj) {
        if (Objects.isNull(obj)) {
            return new HashMap<>();
        }

        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        Map<String, Object> objMap = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(DatabaseField.class)) {
                continue;
            }

            try {
                Object val = field.get(obj);
                if (Objects.isNull(val)) {
                    continue;
                }

                String columnName = field.getAnnotation(DatabaseField.class).columnName();
                objMap.put(columnName, val);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return objMap;
    }

    /**
     * 获取表名
     *
     * @param clazz 类型
     */
    public static String getTableName(Class<?> clazz) {
        Class<DatabaseTable> annotation = DatabaseTable.class;
        if (!clazz.isAnnotationPresent(annotation)) {
            throw new IllegalArgumentException("Please specify the table name");
        }

        String tableName = clazz.getAnnotation(annotation).tableName();
        if (Objects.isNull(tableName)) {
            throw new IllegalArgumentException("Table name must not be empty");
        }
        return tableName;
    }

    /**
     * 表字段匹配
     *
     * @param fieldTypes 表字段
     */
    public static boolean matchFields(Class<?> clazz, FieldType[] fieldTypes) {
        Set<String> columnNames = new HashSet<>();
        for (FieldType fieldType : fieldTypes) {
            columnNames.add(fieldType.getFieldName());
        }
        Set<String> clsFields = new HashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (!field.isAnnotationPresent(DatabaseField.class)) {
                continue;
            }
            clsFields.add(field.getName());
        }

        if (columnNames.size() != clsFields.size()) {
            return false;
        }
        columnNames.removeAll(clsFields);
        return columnNames.isEmpty();
    }
}
