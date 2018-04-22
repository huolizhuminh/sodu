package zhu.minhui.com.shudu;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by minhui.zhu on 2017/8/31.
 * Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class GraphUtils {
    public static final long oneDay = 86400000;
    private static float scale=0.0f;

    public static String getMonthDayFormat(long time) {
        Date date = new Date(time);
        int month = date.getMonth()+1;
        int day = date.getDate();
        return "" + month + "/" + day;
    }

    /**
     * 时间格式，字符串转化成毫秒
     * @param timeFormat  yyyy-MM-dd
     * @return
     */
    public static long getTimeInMill(){
        String timeFormat="yyyy-MM-dd";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
            String dateTime = simpleDateFormat.format(new Date());
            return  simpleDateFormat.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public static int getPeriodDay(long firstTime, long secondTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date secondDate = sdf.parse(sdf.format(new Date(secondTime)));
            Date firstDate = sdf.parse(sdf.format(new Date(firstTime)));
            long t = firstDate.getTime() - secondDate.getTime();
            int d = (int) Math.abs(t / oneDay);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 把密度转换为像素
     */
    public static int dip2px(Context context, float dp) {
        if(Float.compare(scale,0.0f)==0){
            scale = getScreenDensity(context);
        }
        return (int) (dp * scale + 0.5);
    }
    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
