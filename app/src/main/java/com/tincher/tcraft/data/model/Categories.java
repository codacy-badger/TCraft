package com.tincher.tcraft.data.model;

import com.tincher.tcraft.data.base.BaseResponse;

import java.util.List;

/**
 * Created by dks on 2018/9/27.
 */

public class Categories extends BaseResponse {

    private boolean      error;
    private List<Result> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    private static class Result {
        private String _id;
        private String en_name;
        private String name;
        private int    rank;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
}
