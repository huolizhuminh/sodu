package zhu.minhui.com.shudu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

import zhu.minhui.com.shudu.engin.MySoduGenerator;
import zhu.minhui.com.shudu.engin.MySolutionFinder;
import zhu.minhui.com.shudu.engin.SoduNode;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/20.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class SolveGameActivity extends Activity {
    private Handler handler;
    private PositionView soduPosition;
    private SoduNode[][] soduNodes;
    private boolean canClick = true;
    private AndroidSegmentedControlView styleSelect;
    private String[] styles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_solve_game);
        styleSelect = findViewById(R.id.style_selector);
        findViewById(R.id.sodu_generate).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (!canClick) {
                    return;
                }
                canClick = false;
                newGame();

            }
        });
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(1);
            }
        });
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(1);
            }
        });
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(1);
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(2);
            }
        });
        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(3);
            }
        });
        findViewById(R.id.btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(4);
            }
        });
        findViewById(R.id.btn_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(5);
            }
        });
        findViewById(R.id.btn_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(6);
            }
        });
        findViewById(R.id.btn_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(7);
            }
        });
        findViewById(R.id.btn_8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(8);
            }
        });
        findViewById(R.id.btn_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(9);
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.back();
            }
        });
        findViewById(R.id.btn_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solve();


            }
        });
        findViewById(R.id.btn_remind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soduNodes == null) {
                    return;
                }
                remind();

            }
        });
        soduPosition = findViewById(R.id.sodu_position);
        soduPosition.setMode(PositionView.EDIT_MODE);
        styles = getResources().getStringArray(R.array.node_mode);
        newGame();
    }

    private void remind() {
        ThreadPoolProxy.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                MySolutionFinder mySolutionFinder = new MySolutionFinder();
                SoduNode[][] result = mySolutionFinder.findResult(soduNodes, true);
                boolean hasRemind = false;
                //先改正错误
                if (result == null) {

                    result = mySolutionFinder.findResult(soduNodes);

                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (soduNodes[i][j].value != 0 && result[i][j].value != soduNodes[i][j].value) {
                                hasRemind = true;
                                soduPosition.backup(soduNodes[i][j], result[i][j].value);

                            }
                        }
                    }


                }

                //如果没有错误 则提醒一个数值
                if (hasRemind) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            soduPosition.refreshData();
                        }
                    });

                    return;
                }
                while (!hasRemind) {
                    int random = (int) (Math.random() * 81);
                    int x = random % 9;
                    int y = random / 9;
                    if (soduNodes[y][x].value == 0) {
                        soduPosition.backup(soduNodes[y][x], result[y][x].value);
                        hasRemind = true;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                soduPosition.refreshData();
                            }
                        });

                    }


                }
            }
        });
    }

    private void solve() {
        ThreadPoolProxy.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                if (soduNodes == null) {
                    return;
                }
                MySolutionFinder mySolutionFinder = new MySolutionFinder();
                final SoduNode[][] result = mySolutionFinder.findResult(soduNodes, true);
                if (result == null) {
                    showMessage("This game has no result ! ");
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        soduPosition.setResultData(result);
                    }
                });
            }
        });
    }

    private void setData(int value) {
        String checked = styleSelect.getChecked();
        if (checked.equals(styles[0])) {
            soduPosition.setData(value, false);
        } else {
            soduPosition.setData(value, true);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void newGame() {
        ThreadPoolProxy.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                MySoduGenerator mySoduGenerator = new MySoduGenerator();

                soduNodes = mySoduGenerator.generateEmptyGame();
                if (soduNodes == null) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        soduPosition.setData(soduNodes);
                        canClick = true;
                    }
                });
            }
        });

    }
}
