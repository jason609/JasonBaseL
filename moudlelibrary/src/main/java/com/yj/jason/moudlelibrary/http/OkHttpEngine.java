package com.yj.jason.moudlelibrary.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.yj.jason.baselibrary.http.EngineCallBack;
import com.yj.jason.baselibrary.http.HttpUtils;
import com.yj.jason.baselibrary.http.IHttpEngine;
import com.yj.jason.moudlelibrary.db.DaoSupportFactory;
import com.yj.jason.moudlelibrary.db.IDaoSupport;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//okhttp 引擎
public class OkHttpEngine implements IHttpEngine {

    private OkHttpClient mOkhttpClient=new OkHttpClient();

    private String resultJson;

    @Override
    public void get(Context context, final boolean cache, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String joinUrl=HttpUtils.joinParams(url,params);

        Log.i("Okhttp--GET->请求参数:   ",joinUrl);

        if(cache){//判断需不需要缓存
                resultJson=CacheUtils.getCacheData(joinUrl);
                if(!TextUtils.isEmpty(resultJson)){
                    //需要缓存  数据库有缓存
                    Log.i("Okhttp--->缓存:   ","有缓存，已读到缓存");
                    callBack.onSuccess(resultJson);
                }

        }

        RequestBody requestBody=appendBody(params);

        Request request=new Request.Builder()
                .url(url)
                .tag(context)
                .method("GET",null)
                .build();

        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();


                //每次获取到的数据 先比对上一次内容

                if(cache) {
                    if (result.equals(resultJson)) {//数据一致  直接返回  不刷新界面
                        Log.i("Okhttp--->缓存:   ","缓存数据一致");
                        return;
                    }

                }


                Log.i("Okhttp--->返回结果:   ",result);

                //不一致  成功回调  刷新界面
                callBack.onSuccess(result);


                if(cache){//缓存数据
                     CacheUtils.saveCacheData(joinUrl,result);
                }
            }
        });

    }

    @Override
    public void post(final Context context,final boolean cache, String url, Map<String, Object> params, final EngineCallBack callBack) {

       final String joinUrl=HttpUtils.joinParams(url,params);
       // final String joinUrl="222";


        Log.i("Okhttp--POST->请求参数:   ",joinUrl);

        if(cache){//判断需不需要缓存
            resultJson=CacheUtils.getCacheData(joinUrl);
            if(!TextUtils.isEmpty(resultJson)){
                //需要缓存  数据库有缓存
                Log.i("Okhttp--->缓存:   ","有缓存，已读到缓存");
                callBack.onSuccess(resultJson);
            }

        }


        RequestBody requestBody=appendBody(params);

        Request request=new Request.Builder()
                        .url(url)
                        .tag(context)
                        .post(requestBody)
                        .build();

        mOkhttpClient.newCall(request).enqueue(new Callback() {

            Handler mainHandler = new Handler(context.getMainLooper());

            @Override
            public void onFailure(Call call, IOException e) {
                    callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                  final String result=response.body().string();


                if(cache) {
                    if (result.equals(resultJson)) {//数据一致  直接返回  不刷新界面
                        Log.i("Okhttp--->缓存:   ","缓存数据一致");
                        return;
                    }

                }

                Log.i("Okhttp--->返回结果:   ",result);

                   //每次获取到的数据 先比对上一次内容

                   mainHandler.post(new Runnable() {
                       @Override
                       public void run() {
                           callBack.onSuccess(result);
                       }
                   });

                if(cache){//缓存数据
                    CacheUtils.saveCacheData(joinUrl,result);
                }

            }
        });


    }

    private RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
        appendParams(builder,params);
        return builder.build();
    }


    //添加参数
    private void appendParams(MultipartBody.Builder builder, Map<String, Object> params) {

        if(params!=null&&!params.isEmpty()){
            for(String key:params.keySet()){
               // builder.addFormDataPart(key,params.get(key)+"");
                Object value=params.get(key);

                if(value instanceof File){
                       File file=(File)value;
                       builder.addFormDataPart(key,file.getName(),RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())),file));
                }else if(value instanceof List){//代表提交的是list集合

                       try {
                           List<File>files=(List<File>)value;

                           for(int i=0;i<files.size();i++){
                               File file=files.get(i);
                               builder.addFormDataPart(key,file.getName(),RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())),file));
                           }
                       }catch (Exception e){
                           e.printStackTrace();
                       }

                }else {
                    builder.addFormDataPart(key,value+"");
                }

            }
        }

    }

    //猜测文件的类型
    private String guessMimeType(String path) {
        FileNameMap fileNameMap= URLConnection.getFileNameMap();
        String contTypeFor=fileNameMap.getContentTypeFor(path);

        if(contTypeFor==null){
            contTypeFor="application/octet_stream";
        }

        return contTypeFor;
    }
}
