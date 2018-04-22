package zhu.minhui.com.shudu.engin;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/23.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class SoduFileGenerator {
    public void startRun(SoduLevel level,String baseDir,String filedir,int sum) {
        File fileDir = new File(baseDir);

        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(baseDir + filedir
                + "" );
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

            System.out.println("mkdirs " + mkdirs);
        }
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            int generate = 0;
            while (generate < sum) {
                MySoduGenerator mySoduGenerator = new MySoduGenerator();
                mySoduGenerator.generateGame(level);
                mySoduGenerator.getNoNeedToSolve();
                if (mySoduGenerator.getNoNeedToSolve() > level.getMaxSum()) {
                    continue;
                }
                if(mySoduGenerator.getNoNeedToSolve()<level.getMinSum()){
                    continue;
                }
                String dataString = mySoduGenerator.getDataString();
                bufferedWriter.write(dataString + "\r\n");
                generate++;
                System.out.println("fileDir " + fileDir.getAbsolutePath());
                System.err.println("generate " + generate);
            }
            bufferedWriter.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            try {
                fileOutputStream.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                outputStreamWriter.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                bufferedWriter.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
}
