package com.yj.jason.moudlelibrary.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yj.jason.baselibrary.imageloader.ILoaderStrategy;
import com.yj.jason.baselibrary.imageloader.LoaderOptions;

//glide 图片加载框架
public class GlideLoader implements ILoaderStrategy{

   private Context mContext;
   private Glide glide;

   public GlideLoader(Context context){
          this.mContext=context;
          glide=Glide.get(context);
   }

    @Override
    public void loadImage(LoaderOptions options) {
     RequestManager requestManager=Glide.with(mContext);
     DrawableTypeRequest requestCreator=null;
     if (options.url != null) {
      requestCreator= requestManager.load(options.url);
     } else if (options.file != null) {
      requestCreator=requestManager.load(options.file);
     }else if (options.drawableResId != 0) {
      requestCreator=requestManager.load(options.drawableResId);
     } else if (options.uri != null){
      requestCreator= requestManager.load(options.uri);
     }
     if (options.targetHeight > 0 && options.targetWidth > 0) {
      requestCreator.preload(options.targetWidth, options.targetHeight);
     }
     if (options.isCenterInside) {
      requestCreator.fitCenter();
     } else if (options.isCenterCrop) {
      requestCreator.centerCrop();
     }
     if (options.config != null) {
     // requestCreator.c(options.config);
     }
     if (options.errorResId != 0) {
      requestCreator.error(options.errorResId);
     }
     if (options.placeholderResId != 0) {
      requestCreator.placeholder(options.placeholderResId);
     }
     if (options.bitmapAngle != 0) {
     // requestCreator.transform(new PicassoLoader.PicassoTransformation(options.bitmapAngle));
     }
     if (options.skipLocalCache) {
     // requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
     }
     if (options.skipNetCache) {
     // requestCreator.networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE);
     }
     if (options.degrees != 0) {
     // requestCreator.rotate(options.degrees);
     }

     if (options.targetView instanceof ImageView) {
      requestCreator.into(((ImageView)options.targetView));
     } else if (options.callBack != null){
     // requestCreator.into(new PicassoLoader.PicassoTarget(options.callBack));
     }
    }

    @Override
    public void clearMemoryCache() {
         glide.clearMemory();
    }

    @Override
    public void clearDiskCache() {
         glide.clearDiskCache();
    }
}
