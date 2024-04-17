package xyz.ibudai.database.es.model;

public class Condition {

    private QueryType queryType;

    private String fieldName;

    private Object fieldValues;


    public Condition() {
    }

    public Condition(QueryType queryType, String fieldName, Object fieldValues) {
        this.queryType = queryType;
        this.fieldName = fieldName;
        this.fieldValues = fieldValues;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(Object fieldValues) {
        this.fieldValues = fieldValues;
    }
}
