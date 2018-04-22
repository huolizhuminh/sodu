package zhu.minhui.com.shudu;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/20.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public enum SoduLevel {
    EASY(32,50) , COMMON(26,32), HARD(22,26);
    int minSum;
    int maxSum;

    SoduLevel(int minSum, int maxSum) {
        this.minSum = minSum;
        this.maxSum = maxSum;
    }

    public int getMinSum() {
        return minSum;
    }

    public int getMaxSum() {
        return maxSum;
    }
}
