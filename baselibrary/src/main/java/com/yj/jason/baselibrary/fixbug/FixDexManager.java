package com.yj.jason.baselibrary.fixbug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;



public class FixDexManager {

    private Context mContext;


    private File mDexDir;

    private String TAG="FixDexManager";

    public FixDexManager(Context context) {

        this.mContext=context;

        //获取应用可以访问的目录
        this.mDexDir=context.getDir("odex",Context.MODE_PRIVATE);

    }

    //修复dex包
    public void fixDex(String fixFilePath) throws Exception {


        //2.获取下载好的补丁dexElement

        //2.1 移动到系统能够访问的 dex目录下

        File srcFile=new File(fixFilePath);

        if(!srcFile.exists()){
            throw new FileNotFoundException(fixFilePath);
        }

        File destFile=new File(mDexDir,srcFile.getName());


        if(destFile.exists()){
            Log.i(TAG,"path has be loaded");
            return;
        }


        copyFile(srcFile,destFile);

        //2.2  classloader 读取fixDex路径
        List<File>fixDexFiles=new ArrayList<>();

        fixDexFiles.add(destFile);

        fixDexFiles(fixDexFiles);

    }

    private void injectDexElemetn(ClassLoader cl, Object dexElement)throws Exception {
        Field pathListFiled= BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object pathList=pathListFiled.get(cl);


        //2.获取pathList 的dexElement
        Field dexElementsFiled=pathList.getClass().getDeclaredField("dexElements");
        dexElementsFiled.setAccessible(true);

        dexElementsFiled.set(pathList,dexElement);
    }


    //复制文件
    private void copyFile(File src,File dest)throws IOException{
        FileChannel inChannel=null;
        FileChannel outChannel=null;


        try {

            if(!dest.exists()){
                dest.createNewFile();
            }

            inChannel=new FileInputStream(src).getChannel();

            outChannel=new FileOutputStream(dest).getChannel();

            inChannel.transferTo(0,inChannel.size(),outChannel);

        }finally {

            if(inChannel!=null){
                inChannel.close();
            }

            if(outChannel!=null){
                outChannel.close();
            }
        }

    }


    //合并数组
    private Object combineArray(Object arrayLhs,Object arrayRhs){
        Class<?>localClass=arrayLhs.getClass().getComponentType();

        int i= Array.getLength(arrayLhs);
        int j= i+Array.getLength(arrayRhs);

        Object result=Array.newInstance(localClass,j);

        for(int k=0;k<j;++k){

            if(k<i){
                Array.set(result,k,Array.get(arrayLhs,k));
            }else {
                Array.set(result,k,Array.get(arrayRhs,k-i));
            }
        }

       return result;
    }


    //从classloader中获取dexelement
    private Object getDexElementByClassLoader(ClassLoader cl) throws Exception {
        //1.先获取pathList

        Field pathListFiled= BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object pathList=pathListFiled.get(cl);


        //2.获取pathList 的dexElement
        Field dexElementsFiled=pathList.getClass().getDeclaredField("dexElements");
        dexElementsFiled.setAccessible(true);

        return   dexElementsFiled.get(pathList);

    }


    public void deleteAllFixFile(){

        File[] fixDexFiles= mDexDir.listFiles();

        for(File file:fixDexFiles){

            if(file.getName().endsWith(".dex")){
                file.delete();
            }

        }
    }

    //加载之前的全部的修复包
    public void loadFixDex() throws Exception{
        File[] fixDexFiles= mDexDir.listFiles();

        List<File>files=new ArrayList<>();

        for(File file:fixDexFiles){

            if(file.getName().endsWith(".dex")){
                files.add(file);
            }

        }
        
        fixDexFiles(files);
    }

    //修复dex
    private void fixDexFiles(List<File> files) throws Exception {

        //1.先获取已经运行的dexElement

        ClassLoader cl=mContext.getClassLoader();

        Object dexElement=getDexElementByClassLoader(cl);


        File zipFile=new File(mDexDir,"odex");

        if(!zipFile.exists()){
            zipFile.mkdirs();
        }

        //修复
        for(File file:files){
            ClassLoader fixDexCL=new BaseDexClassLoader(
                    file.getAbsolutePath(),
                    zipFile,
                    null,
                    cl
            );

            Object fixDexElement=getDexElementByClassLoader(fixDexCL);

            //3.把补丁的dexElement插到已经运行的dexElement的最前面

            dexElement=combineArray(fixDexElement,dexElement);


            //把合并的数组注入到原来的类中
            injectDexElemetn(cl,dexElement);

        }

    }
}
