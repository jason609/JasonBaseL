package com.yj.jason.baselibrary.view.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.jason.baselibrary.R;
import com.yj.jason.baselibrary.utils.Toaster;


//自定义轮播图
public class BannerView extends RelativeLayout {

    private TextView mTvBanner;
    private LinearLayout mLLDotContainer;
    private RelativeLayout mRlBottom;

    private BannerAdapter mAdapter;
    private BannerViewPager mBannervp;

    private Context mContext;

    private Drawable mFocusDrawable;//被选中的点的drawable
    private Drawable mNormalDrawable;//未被选中的点的drawable

    private int mCurrentPostion=0;//当前位置
    private int mDotGravity=Gravity.RIGHT;//0; 居左  1 居中  2 居右
    private int mDotSize=5;
    private int mDotDistance=5;
    private int mWidthProportion=1;
    private int mHeightProportion=1;

    private int mTextSize=12;
    private int mBottomHeiht=25;

    private int mTextGravity=Gravity.LEFT;

    private int mBottonBG=Color.TRANSPARENT;

    public BannerView(Context context) {
        this(context,null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext=context;
        //加载布局
        inflate(context,R.layout.banner_ui,this);

        initAttribute(attrs);

        initView();

    }


    /**
     * 8.初始化自定义属性
     */
    private void initAttribute(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);

        // 获取点的位置
        mDotGravity = array.getInt(R.styleable.BannerView_dotGravity, 2);
        mTextGravity = array.getInt(R.styleable.BannerView_textGravity, 0);

        switch (mDotGravity){
            case 0:
                mDotGravity=Gravity.LEFT;
                break;
            case 1:
                mDotGravity=Gravity.CENTER;
                break;
            case 2:
                mDotGravity=Gravity.RIGHT;
                break;
        }

        switch (mTextGravity){
            case 0:
                mTextGravity=Gravity.LEFT;
                break;
            case 1:
                mTextGravity=Gravity.CENTER;
                break;
            case 2:
                mTextGravity=Gravity.RIGHT;
                break;
        }

        // 获取点的颜色（默认、选中）
        mFocusDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if(mFocusDrawable == null){
            // 如果在布局文件中没有配置点的颜色  有一个默认值
            mFocusDrawable = new ColorDrawable(Color.RED);
        }
        mNormalDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if(mNormalDrawable == null){
            // 如果在布局文件中没有配置点的颜色  有一个默认值
            mNormalDrawable = new ColorDrawable(Color.WHITE);
        }
        // 获取点的大小和距离
        mDotSize = (int) array.getDimension(R.styleable.BannerView_dotSize,dip2px(mDotSize));
        mDotDistance = (int) array.getDimension(R.styleable.BannerView_dotDistance,dip2px(mDotDistance));

        mTextSize = (int) array.getDimension(R.styleable.BannerView_textSize,dip2px(mTextSize));
        mBottomHeiht = (int) array.getDimension(R.styleable.BannerView_bottomHeight,dip2px(mBottomHeiht));


        mWidthProportion = array.getInt(R.styleable.BannerView_widthProportion, mWidthProportion);
        mHeightProportion = array.getInt(R.styleable.BannerView_heightProportion, mHeightProportion);

        mBottonBG=array.getColor(R.styleable.BannerView_bottomColor,mBottonBG);

        array.recycle();
    }


    private void initView() {
        mBannervp=(BannerViewPager) findViewById(R.id.banner_vps);
        mTvBanner=(TextView) findViewById(R.id.tv_banner);
        mLLDotContainer=(LinearLayout) findViewById(R.id.ll_dot_container);
        mRlBottom=(RelativeLayout) findViewById(R.id.rl_bottom);


        mRlBottom.setBackgroundColor(mBottonBG);

        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)mRlBottom.getLayoutParams();

        params.height=dip2px(mBottomHeiht);

        mRlBottom.setLayoutParams(params);

        mTvBanner.setGravity(mTextGravity);

        mTvBanner.setTextSize(px2sp(mContext,mTextSize));
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }



    public void setAdapter(final BannerAdapter adapter) {

        this.mAdapter=adapter;
        mBannervp.setAdapter(mAdapter);

        initDotIndicator();

        mBannervp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {

               //监听当前选中位置
                pageSelected(position);

            }
        });


        //设置默认描述
        String firstDest=mAdapter.getBannerDesc(0);
        mTvBanner.setText(firstDest);



        Log.i("tag","mWidthProportion==="+mWidthProportion+"mHeightProportion==="+mHeightProportion);

        // 8.自适应高度 动态指定高度
        if(mHeightProportion == 0 || mWidthProportion == 0){
            return;
        }

        this.post(new Runnable() {
            @Override
            public void run() {
                // 动态指定宽高  计算高度
                int width = getMeasuredWidth();


                // 计算高度
                int height = (int) (width*mHeightProportion/mWidthProportion);

                Log.i("tag","width==="+width);
                Log.i("tag","height==="+height);

                ViewGroup.LayoutParams params = getLayoutParams();

                // 指定宽高
                params.height = height;

                setLayoutParams(params);

            }
        });


    }


    //页面切换的回调
    private void pageSelected(int position) {
        //把当前位置的点点亮  之前亮着的点 设置为默认
        DotIndicatorView oldDotIndicatorView=(DotIndicatorView) mLLDotContainer.getChildAt(mCurrentPostion);
        oldDotIndicatorView.setDrawable(mNormalDrawable);


        mCurrentPostion=position%mAdapter.getCount();

        mBannervp.setPostiong(mCurrentPostion);


        DotIndicatorView nowDotIndicatorView=(DotIndicatorView) mLLDotContainer.getChildAt(mCurrentPostion);
        nowDotIndicatorView.setDrawable(mFocusDrawable);


        //设置广告描述

        String desc=mAdapter.getBannerDesc(mCurrentPostion);

        mTvBanner.setText(desc);




    }

    //初始化点的指示器
    private void initDotIndicator() {

        int count =mAdapter.getCount();

        //让点的位置在右边
        mLLDotContainer.setGravity(mDotGravity);


        for(int i=0;i<count;i++){
            //不断往指示器添加数据
            DotIndicatorView dotIndicatorView=new DotIndicatorView(mContext);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(dip2px(mDotSize),dip2px(mDotSize));

            params.leftMargin=dip2px(mDotDistance);
            params.rightMargin=dip2px(mDotDistance);
            
            dotIndicatorView.setLayoutParams(params);

            if(i==0){//默认第一张选中
                dotIndicatorView.setDrawable(mFocusDrawable);
            }else {
                dotIndicatorView.setDrawable(mNormalDrawable);
            }



            mLLDotContainer.addView(dotIndicatorView);
        }

    }

    //把dip 转成px
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,mContext.getResources().getDisplayMetrics());
    }



    //自动轮播
    public void startRoll(){
        mBannervp.startRoll();
    }
}
