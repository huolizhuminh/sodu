package zhu.minhui.com.shudu.engin;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import zhu.minhui.com.shudu.ThreadPoolProxy;


/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/20.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class SoduUtils {
    private static final String TAG = "SoduUtils";

    public static SoduNode[][] getNodes(char[] charArray) {

        SoduNode[][] soduNodes = new SoduNode[9][9];
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                SoduNode soduNode = new SoduNode();
                soduNode.value = charArray[i * 9 + k] - '0';
                soduNode.xPosition = k;
                soduNode.yPosition = i;
                if (soduNode.value != 0) {
                    soduNode.needTobeSolve = false;
                }
                soduNodes[i][k] = soduNode;
            }
        }
        for (int i = 0; i < 9; i++) {
            SoduNode[] listNode = new SoduNode[9];
            for (int k = 0; k < 9; k++) {
                listNode[k] = soduNodes[i][k];
                soduNodes[i][k].listNode = listNode;
            }
            // Log.d("listNode:"+SoduNode.getNodesValue(listNode));
        }
        for (int i = 0; i < 9; i++) {
            SoduNode[] rowNode = new SoduNode[9];
            for (int k = 0; k < 9; k++) {
                rowNode[k] = soduNodes[k][i];
                soduNodes[k][i].rowNode = rowNode;
            }
            // Log.d("rowNode:"+SoduNode.getNodesValue(rowNode));
        }
        for (int i = 0; i <= 2; i++) {
            for (int k = 0; k <= 2; k++) {
                SoduNode[] groupNode = new SoduNode[9];
                int index = 0;
                int middlex = 3 * i + 1;
                int middley = 3 * k + 1;
                for (int j = -1; j <= 1; j++) {
                    for (int l = -1; l <= 1; l++) {
                        groupNode[index] = soduNodes[middlex + j][middley + l];
                        soduNodes[middlex + j][middley + l].groupNode = groupNode;
                        index++;
                    }
                }
                // Log.d("groupNode:"+SoduNode.getNodesValue(groupNode));
            }
        }
        return soduNodes;

    }

    public static void saveGame() {

        ThreadPoolProxy.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                SoduLevel level = SoduLevel.HARD;
                String baseDir = Environment.getExternalStorageDirectory() + "/sodu" + "/hard";
                File fileDir = new File(baseDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                for (int i = 0; i < 2; i++) {
                    File file = new File(baseDir + "/data" + i);
                    if (file.exists()) {
                        file.delete();
                    }
                    if (!file.exists()) {
                        boolean mkdirs = false;
                        try {
                            mkdirs = file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "mkdirs " + mkdirs);
                    }
                    FileOutputStream fileOutputStream = null;
                    OutputStreamWriter outputStreamWriter = null;
                    BufferedWriter bufferedWriter = null;
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                        bufferedWriter = new BufferedWriter(outputStreamWriter);
                        int generate = 0;
                        while (generate < 5) {
                            MySoduGenerator mySoduGenerator = new MySoduGenerator();
                            mySoduGenerator.generateGame(level);
                            if (mySoduGenerator.getNoNeedToSolve() > level.getMaxSum()) {
                                continue;
                            }
                            String dataString = mySoduGenerator.getDataString();
                            bufferedWriter.write(dataString + "\r\n");
                            generate++;
                        }
                        bufferedWriter.flush();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    } finally {

                        try {
                            fileOutputStream.close();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }

                        try {
                            outputStreamWriter.close();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }

                        try {
                            bufferedWriter.close();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }

                    }


                }


            }
        });
    }
}
