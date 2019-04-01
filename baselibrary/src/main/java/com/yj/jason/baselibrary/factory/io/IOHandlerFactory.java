package com.yj.jason.baselibrary.factory.io;

public class IOHandlerFactory {

    private static IOHandlerFactory mInstance;
    private SPHandler mSPHandler;

    private IOHandlerFactory(){}

    public static IOHandlerFactory getInstance(){
        if(mInstance==null){
            synchronized (IOHandlerFactory.class){
                if(mInstance==null){
                    mInstance=new IOHandlerFactory();
                }
            }
        }
        return mInstance;
    }

    public IOHandler createIOhandler(Class<? extends IOHandler> clazz){
        IOHandler handler=null;
        try {
            handler=clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handler;
    }


    public IOHandler getSPHandler(){
        if(mSPHandler==null){
            mSPHandler= (SPHandler) createIOhandler(SPHandler.class);
        }
        return mSPHandler;
    }

}
