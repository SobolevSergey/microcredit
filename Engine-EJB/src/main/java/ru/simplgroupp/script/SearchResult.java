package ru.simplgroupp.script;

import java.util.List;

public class SearchResult<T> {

    protected List<T> data;
    protected int count;

    protected SearchResult() {

    }

    protected SearchResult(List<T> adata, int acount) {
        data = adata;
        count = acount;
    }

    public List<T> getData() {
        return data;
    }

    public int getCount() {
        return count;
    }
}
