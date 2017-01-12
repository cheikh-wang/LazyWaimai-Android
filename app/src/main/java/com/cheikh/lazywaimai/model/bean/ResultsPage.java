package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultsPage<T> {

    @SerializedName("items")
    public List<T> results;

    @SerializedName("_meta")
    public Meta meta;

    public class Meta {
        public int totalCount;
        public int pageCount;
        public int currentPage;
        public int perPage;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}