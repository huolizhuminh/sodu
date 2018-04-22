package zhu.minhui.com.shudu;

import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import zhu.minhui.com.shudu.engin.MySoduGenerator;
import zhu.minhui.com.shudu.engin.MySolutionFinder;
import zhu.minhui.com.shudu.engin.SoduNode;
import zhu.minhui.com.shudu.engin.SoduReader;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    private Handler handler;
    private PositionView soduPosition;
    private SoduNode[][] soduNodes;
    SoduLevel level = SoduLevel.COMMON;
    private AndroidSegmentedControlView levelSelector;
    private String[] levelStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        levelSelector = findViewById(R.id.level_selector);
        levelStr = getResources().getStringArray(R.array.level);

        findViewById(R.id.sodu_generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checked = levelSelector.getChecked();
                SoduLevel soduLevel;
                if (checked.equals(levelStr[0])) {
                    soduLevel = SoduLevel.EASY;
                } else if (checked.equals(levelStr[1])) {
                    soduLevel = SoduLevel.COMMON;
                } else {
                    soduLevel = SoduLevel.HARD;
                }
                soduNodes = SoduReader.getInstance().getCacheNode(soduLevel);
                if (soduNodes != null) {
                    soduPosition.setData(soduNodes);
                }
            }
        });
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(1);
            }
        });
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(1);
            }
        });
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(1);
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(2);
            }
        });
        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(3);
            }
        });
        findViewById(R.id.btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(4);
            }
        });
        findViewById(R.id.btn_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(5);
            }
        });
        findViewById(R.id.btn_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(6);
            }
        });
        findViewById(R.id.btn_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(7);
            }
        });
        findViewById(R.id.btn_8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(8);
            }
        });
        findViewById(R.id.btn_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soduPosition.setData(9);
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
                solveProblem();


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

        //  newGames();
    }

    private void remind() {
        ThreadPoolProxy.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                MySolutionFinder mySolutionFinder = new MySolutionFinder();
                final SoduNode[][] result = mySolutionFinder.findResult(soduNodes);
                boolean hasRemind = false;
                //先改正错误
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (soduNodes[i][j].value != 0 && result[i][j].value != soduNodes[i][j].value) {
                            hasRemind = true;
                            soduPosition.backup(soduNodes[i][j], result[i][j].value);

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

    private void solveProblem() {

        ThreadPoolProxy.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                if (soduNodes == null) {
                    return;
                }
                MySolutionFinder mySolutionFinder = new MySolutionFinder();
                final SoduNode[][] result = mySolutionFinder.findResult(soduNodes);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        soduPosition.setResultData(result);
                    }
                });
            }
        });

    }


}
