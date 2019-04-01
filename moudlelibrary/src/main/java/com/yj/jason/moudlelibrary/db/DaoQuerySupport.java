package com.yj.jason.moudlelibrary.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//数据库查询支持类
public class DaoQuerySupport<T> {

    private SQLiteDatabase mSqLiteDatabase;
    private Class<T> mClazz;

    private String[] columns=null;
    private String selection=null;
    private String[] selectionArgs=null;
    private String groupBy=null;
    private String having=null;
    private String orderBy=null;

    public <T> DaoQuerySupport(SQLiteDatabase mSqLiteDatabase, Class clazz) {
        this.mSqLiteDatabase=mSqLiteDatabase;
        this.mClazz=clazz;
    }

    public DaoQuerySupport COLUMNS(String... colums){
        columns=new String[colums.length];
        for(int i=0;i<colums.length;i++){
            columns[i]=colums[i];
        }
        return this;
    }
    public DaoQuerySupport SELECTION(String selection){
        this.selection=selection;
        return this;
    }
    public DaoQuerySupport SELECTIONARGS(String selctionArgs){
        selectionArgs=new String[]{selctionArgs};
        return this;
    }
    public DaoQuerySupport GROUPBY(String groupBy){
        this.groupBy=groupBy;
        return this;
    }
    public DaoQuerySupport HAVING(String having){
        this.having=having;
        return this;
    }
    public DaoQuerySupport ORDERBY(String orderBy){
        this.orderBy=orderBy;
        return this;
    }

    //查询全部
    public List<T> queryAll(){
        clearQueryParam();
        Cursor mCursor=mSqLiteDatabase.query(DaoUtlis.getTableName(mClazz),columns,selection,selectionArgs,groupBy,having,orderBy);
        return  cursorToList(mCursor);
    }

    //根据条件查询
    public List<T> query(){
        Cursor mCursor=mSqLiteDatabase.query(DaoUtlis.getTableName(mClazz),columns,selection,selectionArgs,groupBy,having,orderBy);
        return  cursorToList(mCursor);
    }


    //清除查询条件
    private void clearQueryParam(){
        columns=null;
        selection=null;
        selectionArgs=null;
        groupBy=null;
        having=null;
        orderBy=null;
    }


    private List<T> cursorToList(Cursor mCursor) {

        List<T>list=new ArrayList<>();
        if(mCursor!=null&&mCursor.moveToFirst()){
            do{
                try {

                    T instance=mClazz.newInstance();

                    Field[]fields=mClazz.getDeclaredFields();

                    for(Field field:fields){
                        field.setAccessible(true);
                        String name=field.getName();

                        int index=mCursor.getColumnIndex(name);

                        if(index==-1){
                            continue;
                        }


                        Method cursonMethod=cursorMethod(field.getType());
                        if(cursonMethod!=null){
                            Object value=cursonMethod.invoke(mCursor,index);
                            if(value==null){
                                continue;
                            }

                            if(field.getType()==boolean.class||field.getType()==Boolean.class){
                                if("0".equals(String.valueOf(value))){
                                    value=false;
                                }else if("1".equals(String.valueOf(value))){
                                    value=true;
                                }
                            }else if(field.getType()==char.class||field.getType()==Character.class){
                                value=((String)value).charAt(0);
                            }else if(field.getType()==Date.class){
                                long date=(Long)value;
                                if(date<=0){
                                    value=null;
                                }else {
                                    value=new Date(date);
                                }
                            }

                            field.set(instance,value);

                        }

                    }

                    list.add(instance);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (mCursor.moveToNext());

        }

        mCursor.close();
        return list;

    }

    private Method cursorMethod(Class<?> type) throws Exception {
        String methodName=getColumnMethodName(type);
        Method method=Cursor.class.getMethod(methodName,int.class);

        return method;
    }

    private String getColumnMethodName(Class<?> type) {
        String typeName;
        if(type.isPrimitive()){
            typeName=DaoUtlis.capitalize(type.getName());
        }else {
            typeName=type.getSimpleName();
        }

        String methodName="get"+typeName;
        if("getBoolean".equals(methodName)){
            methodName="getInt";
        }else if("getChar".equals(methodName)||"getCharacter".equals(methodName)){
            methodName="getString";
        }else if("getDate".equals(methodName)){
            methodName="getLong";
        }else if("getInteger".equals(methodName)){
            methodName="getInt";
        }

        return methodName;
    }

}
