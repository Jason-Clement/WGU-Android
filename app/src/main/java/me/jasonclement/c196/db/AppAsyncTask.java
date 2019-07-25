package me.jasonclement.c196.db;

import android.os.AsyncTask;

public class AppAsyncTask<T> {

    private AppDao<T> dao;

    public AppAsyncTask(AppDao<T> dao) {
        this.dao = dao;
    }

    public void insert(T t) {
        new InsertAsyncTask<>(dao).execute(t);
    }

    public void delete(T t) {
        new DeleteAsyncTask<>(dao).execute(t);
    }

    private static class InsertAsyncTask<T> extends AsyncTask<T, Void, Void> {
        private AppDao<T> dao;

        public InsertAsyncTask(AppDao<T> dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final T... params) {
            dao.insert(params[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask<T> extends AsyncTask<T, Void, Void> {
        private AppDao<T> dao;

        public DeleteAsyncTask(AppDao<T> dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final T... params) {
            dao.delete(params[0]);
            return null;
        }
    }
}

