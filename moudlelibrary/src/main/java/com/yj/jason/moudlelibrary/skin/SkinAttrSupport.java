package com.yj.jason.moudlelibrary.skin;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yj.jason.moudlelibrary.skin.attr.SkinAttr;
import com.yj.jason.moudlelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

//皮肤属性解析支持类
public class SkinAttrSupport {

    //获取skinAttr属性
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {

        List<SkinAttr>skinAttrs=new ArrayList<>();

        int attrLength=attrs.getAttributeCount();

        for(int i=0;i<attrLength;i++){

            String attrName=attrs.getAttributeName(i);
            String attrValue=attrs.getAttributeValue(i);

            SkinType skinType=getSkinType(attrName);

            if(skinType!=null){

                String resName=getResName(context,attrValue);

                if(TextUtils.isEmpty(resName))continue;

                SkinAttr skinAttr=new SkinAttr(resName,skinType);

                skinAttrs.add(skinAttr);
            }

        }

        return skinAttrs;
    }

    private static String getResName(Context context, String attrValue) {

        if(attrValue.startsWith("@")){
            attrValue=attrValue.substring(1);

            int resId=Integer.parseInt(attrValue);

            return context.getResources().getResourceEntryName(resId);
        }

        return null;
    }

    //通过名称获取skinType
    private static SkinType getSkinType(String attrName) {

        SkinType[] skinTypes=SkinType.values();

        for(SkinType skinType:skinTypes){

            if(skinType.getResName().equals(attrName)){
                  return skinType;
            }

        }

        return null;
    }
}
