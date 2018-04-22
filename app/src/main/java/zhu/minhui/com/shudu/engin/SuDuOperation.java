package zhu.minhui.com.shudu.engin;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/11.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class SuDuOperation {
    SoduNode soduNode;
    int beforeValue;
    boolean needSolve;

    public SuDuOperation(SoduNode soduNode, int beforeValue) {
        this.soduNode = soduNode;
        this.beforeValue = beforeValue;
        this.needSolve = true;
    }

    public SuDuOperation(SoduNode soduNode, int beforeValue, boolean needSolve) {
        this.soduNode = soduNode;
        this.beforeValue = beforeValue;
        this.needSolve = needSolve;
    }

    public void reSet() {
        soduNode.value = beforeValue;
        soduNode.needTobeSolve=needSolve;
    }
}
