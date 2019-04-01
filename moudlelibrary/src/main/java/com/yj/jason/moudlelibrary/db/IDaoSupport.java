package com.yj.jason.moudlelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqLiteDatabase, Class<T>clazz);

    //插入
    long insert(T t);

    //批量插入

    void insert(List<T> datas);

    //查询所有
   // List<T>query();

    DaoQuerySupport<T> querySupport();

    //删除
    int delete(String whereClause,String... whereArgs);

    //更新
    int update(T obj,String whereClause,String...whereArgs);
}
