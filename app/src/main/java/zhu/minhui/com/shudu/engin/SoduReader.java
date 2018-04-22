package zhu.minhui.com.shudu.engin;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

import zhu.minhui.com.shudu.SoduLevel;
import zhu.minhui.com.shudu.ThreadPoolProxy;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/20.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class SoduReader {
    private static final String SODU_CACHE = "soducache";
    private static final String EASY_NAME = "data_easy";
    private static final String COMMON_NAME = "data_common";
    private static final String HARD_NAME = "data_hard";
    private static final String TAG = "SoduReader";
    Context context;
    private String[] easyLevel;


    private String[] hardLevel;

    private String[] commonLevel;
    private TreeSet<Integer> openHard;
    private TreeSet<Integer> openCommon;
    private TreeSet<Integer> openEasy;

    static class Inner {
        static SoduReader instance = new SoduReader();
    }

    public static SoduReader getInstance() {
        return Inner.instance;
    }

    public void init(Context context) {
        this.context = context;
        openHard = new TreeSet<>();
        openCommon = new TreeSet<>();
        openEasy = new TreeSet<>();
        ThreadPoolProxy.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                easyLevel = getLevelData(EASY_NAME);
                commonLevel = getLevelData(COMMON_NAME);
                hardLevel = getLevelData(HARD_NAME);

            }
        });
    }

    private String[] getLevelData(String fileName) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        ArrayList<String> list = new ArrayList<>();
        try {
            inputStream = context.getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                list.add(line);
            }
            Log.d(TAG, "getLevelData fileName" + fileName + "  size  " + list.size());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            try {
                inputStreamReader.close();
            } catch (Exception e) {

            }
            try {
                bufferedReader.close();
            } catch (Exception e) {

            }
        }
        String[] result=new String[list.size()];
        try {
           list.toArray(result);
        } catch (Exception e) {
            Log.d(TAG, "failed to copy " + e.getMessage());
        }
        return result;
    }


    public SoduNode[][] getCacheNode(SoduLevel level) {
        if (level == SoduLevel.EASY) {
            return getCacheNodes(easyLevel, openEasy);
        } else if (level == SoduLevel.COMMON) {
            return getCacheNodes(commonLevel, openCommon);
        } else {
            return getCacheNodes(hardLevel, openHard);
        }
    }

    private SoduNode[][] getCacheNodes(String[] levelStr, TreeSet<Integer> openLevel) {
        if (levelStr == null) {
            return null;
        }
        int index = 0;
        if (openLevel.size() == levelStr.length) {
            openLevel.clear();
        }
        while (true) {
            index = (int) (levelStr.length * Math.random());
            if (!openLevel.contains(index)) {
                break;
            }
        }
        openLevel.add(index);
        Log.d(TAG,"getCacheNodes "+index);
        char[] chars = levelStr[index].toCharArray();
        Log.d(TAG," chars"+levelStr[index]);

        return SoduUtils.getNodes(chars);
    }


}
