package com.fanw.fanwsocialapp.callback;

import com.fanw.fanwsocialapp.model.PhotoGirl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fanw on 2018/2/6.
 */

public class PhotoGirlReceiver<T> implements Serializable {
    private boolean error;
    private List<PhotoGirl> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<PhotoGirl> getResults() {
        return results;
    }

    public void setResults(List<PhotoGirl> results) {
        this.results = results;
    }
}
