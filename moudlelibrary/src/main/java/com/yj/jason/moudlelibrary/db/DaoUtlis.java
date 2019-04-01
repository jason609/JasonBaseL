package com.yj.jason.moudlelibrary.db;

public class DaoUtlis {

    //转换属性名称
    public static String getColumType(String type){
        String value=null;

        if(type.contains("String")){
            value=" text";
        }else if(type.contains("int")){
            value=" integer";
        }else if(type.contains("boolean")){
            value=" boolean";
        }else if(type.contains("float")){
            value=" float";
        }else if(type.contains("double")){
            value=" double";
        }else if(type.contains("char")){
            value=" varchar";
        }else if(type.contains("long")){
            value=" long";
        }

        return value;
    }

    //获取数据库表名
    public static String getTableName(Class<?> mClazz) {
        return mClazz.getSimpleName();
    }

    public static String capitalize(String name) {
        String typeName=null;

        switch (name){
            case "int":
                typeName="Int";
                break;
            case "short":
                typeName="Short";
                break;
            case "long":
                typeName="Long";
                break;
            case "Boolean":
                typeName="Boolean";
                break;
            case "double":
                typeName="Double";
                break;
        }

        return typeName;
    }
}
