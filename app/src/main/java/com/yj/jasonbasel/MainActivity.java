package com.yj.jasonbasel;

import android.Manifest;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yj.jason.baselibrary.base.BaseActivity;
import com.yj.jason.baselibrary.view.dialog.AlertDialog;
import com.yj.jason.moudlelibrary.activity.BaseSkinActivity;
import com.ywl5320.wlmedia.WlMedia;
import com.ywl5320.wlmedia.enums.WlCodecType;
import com.ywl5320.wlmedia.enums.WlMute;
import com.ywl5320.wlmedia.enums.WlPlayModel;
import com.ywl5320.wlmedia.listener.WlOnPreparedListener;
import com.ywl5320.wlmedia.widget.WlSurfaceView;

import java.io.File;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    private WlSurfaceView surface;
    private WlMedia wlMedia;
    private SeekBar seekbar;
    private double position;
    private boolean isSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surface=findViewById(R.id.surface);
        seekbar=findViewById(R.id.seekbar);
        initPlayer();

    }

    @Override
    public void initData() {
        RxPermissions rxPermissions=new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if(aBoolean){

                        }else {
                           // finish();
                        }
                    }
                });

    }


    public void initPlayer(){
        wlMedia = new WlMedia();// 可支持多实例播放（主要对于音频，视频实际验证效果不佳）
        wlMedia.setPlayModel(WlPlayModel.PLAYMODEL_AUDIO_VIDEO);//声音视频都播放
        wlMedia.setCodecType(WlCodecType.CODEC_MEDIACODEC);//优先使用硬解码
        wlMedia.setMute(WlMute.MUTE_CENTER);//立体声
        wlMedia.setVolume(80);//80%音量
        wlMedia.setPlayPitch(1.0f);//正常速度
        wlMedia.setPlaySpeed(1.0f);//正常音调
        wlMedia.setRtspTimeOut(30);//网络流超时时间
        wlMedia.setShowPcmData(true);//回调返回音频pcm数据
        surface.setWlMedia(wlMedia);//给视频surface设置播放器

        //异步准备完成后开始播放
        wlMedia.setOnPreparedListener(new WlOnPreparedListener() {
            @Override
            public void onPrepared() {
                // wlMedia.setVideoScale(WlScaleType.SCALE_16_9);//设置16:9的视频比例
                wlMedia.start();//开始播放
                // double duration = wlMedia.getDuration();//获取时长
            }
        });


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(wlMedia.getDuration()>0&&isSeek) {
                    position = wlMedia.getDuration() * progress / 100;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeek=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeek=false;
                wlMedia.seek(position);
            }
        });

    }

    public void start(View view) {
        wlMedia.setSource(Environment.getExternalStorageDirectory()+ File.separator+"樹大招風國語.mp4");
        wlMedia.prepared();//异步准备
    }

    public void pause(View view) {
        wlMedia.pause();
      //  showPwdDialog();
    }


    public void resume(View view) {
        wlMedia.resume();
    }

    public void stop(View view) {
        wlMedia.stop();
    }


    private void showPwdDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setContentView(R.layout.dialog_pwd_editext).create();

        PayPwdEditText payPwdEditText=dialog.getView(R.id.pay_pwd);

        payPwdEditText.initStyle(R.drawable.shape_pwd_bg, 6, 0.33f, R.color.colorAccent, R.color.colorPrimary, 20);
        payPwdEditText.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {//密码输入完后的回调
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

}
