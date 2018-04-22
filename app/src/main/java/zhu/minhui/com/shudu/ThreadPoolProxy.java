package zhu.minhui.com.shudu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/20.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class ThreadPoolProxy {

    private final ExecutorService executor;

    static class Inner {
        static ThreadPoolProxy instance = new ThreadPoolProxy();
    }

    private ThreadPoolProxy() {
        executor = Executors.newSingleThreadExecutor();
    }

    public static ExecutorService getExecutor() {
        return Inner.instance.executor;
    }

}
