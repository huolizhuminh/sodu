package zhu.minhui.com.shudu;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

import zhu.minhui.com.shudu.engin.SoduReader;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/20.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(this,"eaec1ce588",false);
        try {
            SoduReader.getInstance().init(this);
        }catch (Exception e){

        }

    }
}
