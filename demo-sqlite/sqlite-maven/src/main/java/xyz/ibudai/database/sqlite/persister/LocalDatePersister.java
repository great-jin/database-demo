package xyz.ibudai.database.sqlite.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The type Local date persister.
 */
public class LocalDatePersister extends BaseDataType {

    private volatile static LocalDatePersister instance = null;

    private LocalDatePersister() {
        super(SqlType.LONG);
    }

    /**
     * Instantiates a new Local date persister.
     *
     * @param sqlType the sql type
     * @param classes the classes
     */
    protected LocalDatePersister(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    /**
     * Gets singleton.
     *
     * @return the singleton
     */
    public static LocalDatePersister getSingleton() {
        if (instance == null) {
            synchronized (LocalDatePersister.class) {
                if (instance == null) {
                    instance = new LocalDatePersister();
                }
            }
        }
        return instance;
    }

    @Override
    public Class<?>[] getAssociatedClasses() {
        return new Class[]{LocalDate.class, LocalDateTime.class};
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        return extractMillis(javaObject);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        try {
            return Long.parseLong(defaultStr);
        } catch (NumberFormatException e) {
            throw new SQLException("Problems with field " + fieldType + " parsing default date value: " + defaultStr, e);
        }
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getLong(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        return this.createInstance((Long) sqlArg, fieldType.getType());
    }

    @Override
    public boolean isEscapedValue() {
        return false;
    }

    @Override
    public boolean isAppropriateId() {
        return false;
    }

    @Override
    public Class<?> getPrimaryClass() {
        return LocalDateTime.class;
    }

    @Override
    public boolean isValidForVersion() {
        return true;
    }

    @Override
    public Object moveToNextValue(Object currentValue) throws SQLException {
        long newVal = System.currentTimeMillis();
        if (currentValue == null) {
            return createInstance(newVal, LocalDateTime.class);
        }
        Long currentVal = extractMillis(currentValue);
        if (newVal == currentVal) {
            return createInstance(newVal + 1L, currentValue.getClass());
        } else {
            return createInstance(newVal, currentValue.getClass());
        }
    }

    /**
     * 转化 Long 为时间类型
     *
     * @param sqlArg     时间
     * @param targetType 类型
     * @return 时间
     */
    private Object createInstance(Long sqlArg, Class<?> targetType) {
        if (targetType == LocalDate.class) {
            return Instant.ofEpochMilli(sqlArg)
                    .atZone(this.getZoneId())
                    .toLocalDate();
        }

        return Instant.ofEpochMilli(sqlArg)
                .atZone(this.getZoneId())
                .toLocalDateTime();
    }

    /**
     * 转化时间类型为 Long
     *
     * @param javaObject 时间
     * @return 时间
     */
    private Long extractMillis(Object javaObject) {
        if (javaObject == null) {
            return null;
        }

        if (javaObject instanceof LocalDateTime) {
            return ((LocalDateTime) javaObject)
                    .atZone(this.getZoneId())
                    .toInstant()
                    .toEpochMilli();
        } else if (javaObject instanceof LocalDate) {
            return ((LocalDate) javaObject)
                    .atStartOfDay(this.getZoneId())
                    .toInstant()
                    .toEpochMilli();
        } else {
            throw new IllegalArgumentException("Unsupported date type: " + javaObject.getClass());
        }
    }

    /**
     * 获取系统时区
     *
     * @return 时区
     */
    private ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }
}
