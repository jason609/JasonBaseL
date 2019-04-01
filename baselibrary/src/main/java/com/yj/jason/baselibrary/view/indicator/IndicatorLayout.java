package com.yj.jason.baselibrary.view.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.yj.jason.baselibrary.R;

//自定义指示器容器
public class IndicatorLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener{

    private IndicatorAdapter mAdapter;
    private IndicatorGroupView contianer;

    private int mTabNum;//一屏显示数量
    private ViewPager mViewPager;

    private int mItemWidht;

    private int mCurrentIndex=0;//当前位置

    private boolean mIsExecuteScroll=false;//是否执行滚动  解决点击抖动


    public IndicatorLayout(Context context) {
        this(context,null);
    }

    public IndicatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        contianer=new IndicatorGroupView(context);//子布局的容器
        addView(contianer);

        //初始化自定义属性
        initAttribute(context,attrs);

    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.IndicatorLayout);
        mTabNum=array.getInt(R.styleable.IndicatorLayout_tabNum,0);


        array.recycle();
    }


    public void setAdapter(IndicatorAdapter adapter){
        if(adapter==null){
            throw new NullPointerException("adapter 不能为空!");
        }

        this.mAdapter=adapter;

        int count=mAdapter.getCount();

        for(int i=0;i<count;i++){
            View chileView=mAdapter.getView(this,i);
            contianer.addItemView(chileView);
            //设置点击事件
            if(mViewPager!=null) {
                switchItemClickListener(chileView, i);
            }

        }

        mAdapter.highLightIndicator(contianer.getItemView(0));



    }

    //设置点击事件 与viewpager 联动
    private void switchItemClickListener(View chileView, final int position) {
        chileView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(position);
                smoothScrollIndxe(position);
                contianer.scrollBottomTrackView(position);
            }
        });
    }


    //点击移动 带动画
    private void smoothScrollIndxe(int position) {
        //当前总共的位置
        float totalScroll=(position)*mItemWidht;
        //左边的偏移
        int offsetScroll=(getWidth()-mItemWidht)/2;
        //最终的偏移量
        int finalScroll= (int) (totalScroll-offsetScroll);
        //调用自带的滚动方法
        smoothScrollTo(finalScroll,0);
    }

    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager){
          if(viewPager==null){
              throw new NullPointerException("viewpager 为空!");
          }
          this.mViewPager=viewPager;
          setAdapter(adapter);
          mViewPager.addOnPageChangeListener(this);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        if(changed){
            //指定Item的宽度
            mItemWidht=getItemWidth();

            //循环指定item 宽度

            for(int i=0;i<mAdapter.getCount();i++){
                ViewGroup.LayoutParams params = contianer.getItemView(i).getLayoutParams();
                params.width=mItemWidht;
                contianer.getItemView(i).setLayoutParams(params);

            }
            contianer.addBottomTrackView(mAdapter.getBottomTrackView(),mItemWidht);

        }

    }

    private int getItemWidth() {
        int parentWidth=getWidth();
        if(mTabNum!=0){
            return parentWidth/mTabNum;
        }

        int mItemWidth=0;

        int maxItemWidth=0;

        for(int i=0;i<mAdapter.getCount();i++){
            int currentItemWidth=contianer.getItemView(i).getMeasuredWidth();
             maxItemWidth=Math.max(maxItemWidth,currentItemWidth);
        }

        mItemWidth=maxItemWidth;

        int allWidth=mAdapter.getCount()*maxItemWidth;
        //所有条目相加不足一屏的情况
        if(allWidth<parentWidth){
            mItemWidth=parentWidth/mAdapter.getCount();
        }

        return mItemWidth;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int i1) {
        if(mIsExecuteScroll) {
            scrollCurrentIndxe(position, positionOffset);
            contianer.scrollBottomTrackView(position, positionOffset);
            //如果点击  不执行onpagescrolled（）方法
        }

    }

    //不断的滚动当前指示器
    private void scrollCurrentIndxe(int position, float positionOffset) {
        //当前总共的位置
        float totalScroll=(position+positionOffset)*mItemWidht;
        //左边的偏移
        int offsetScroll=(getWidth()-mItemWidht)/2;
        //最终的偏移量
        int finalScroll= (int) (totalScroll-offsetScroll);
        //调用自带的滚动方法
        scrollTo(finalScroll,0);
    }

    @Override
    public void onPageSelected(int position) {
        //当前位置点亮，上一个位置重置

        mAdapter.restoreIndicator(contianer.getItemView(mCurrentIndex));

        mCurrentIndex=position;

        mAdapter.highLightIndicator(contianer.getItemView(mCurrentIndex));

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state==1){
            mIsExecuteScroll=true;
        }

        if(state==0){
            mIsExecuteScroll=false;
        }
    }
}
