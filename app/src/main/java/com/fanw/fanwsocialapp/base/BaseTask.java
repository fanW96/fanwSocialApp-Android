package com.fanw.fanwsocialapp.base;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by fanw on 2018/2/4.
 */

public class BaseTask<T> extends AsyncTask<Integer,Void,List<T>> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<T> ts) {
        super.onPostExecute(ts);
    }

    @Override
    protected List<T> doInBackground(Integer... integers) {
        return null;
    }
}
