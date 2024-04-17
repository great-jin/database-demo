package xyz.ibudai.database.es.model;

import java.util.List;

public class PageDetail<T> {

    private int limit;

    private int offset;

    private int total;

    private List<T> data;

    public PageDetail() {
    }

    public PageDetail(int limit, List<T> data) {
        this.limit = limit;
        this.data = data;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
